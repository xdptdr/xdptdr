package xdptdr.acme.jw;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;

import xdptdr.acme.AcmeException;
import xdptdr.acme.crypto.ECCurves;
import xdptdr.common.Common;

public class KeyPairWithJWK {

	private KeyPair keyPair;
	private Map<String, String> publicJwk = new HashMap<>();
	private Map<String, String> privateJwk = new HashMap<>();

	private KeyPairWithJWK(KeyPair keyPair, Map<String, String> publicJwk, Map<String, String> privateJwk) {
		this.keyPair = keyPair;
		this.publicJwk = publicJwk;
		this.privateJwk = privateJwk;
	}

	public KeyPairWithJWK(Map<String, String> jwk) {
		this.publicJwk = jwk;
	}

	public KeyPair getKeyPair() {
		return keyPair;
	}

	public Map<String, String> getPublicJwk() {
		return publicJwk;
	}

	public Map<String, String> getPrivateJwk() {
		return privateJwk;
	}

	public static KeyPairWithJWK fromEllipticKeyPair(KeyPair keyPair, String javaName) {

		Map<String, String> publicJwk = getPublicEllipticJWK(keyPair.getPublic(), javaName);
		Map<String, String> privateJwk = getPrivateEllipticJWK(keyPair.getPrivate(), javaName);

		return new KeyPairWithJWK(keyPair, publicJwk, privateJwk);
	}

	public static KeyPairWithJWK fromRSAKeyPair(KeyPair keyPair, int keySize) {

		Map<String, String> publicJwk = getPublicRSAJWK(keyPair.getPublic(), keySize);
		Map<String, String> privateJwk = getPrivateRSAJWK(keyPair.getPrivate(), keySize);

		return new KeyPairWithJWK(keyPair, publicJwk, privateJwk);
	}

	public static KeyPairWithJWK fromJWK(Map<String, String> jwk) throws AcmeException {

		Map<String, String> publicProps = new HashMap<>();
		Map<String, String> privateProps = new HashMap<>();

		jwk.forEach((p, v) -> {
			if ("x".equals(p) || "y".equals(p)) {
				publicProps.put(p, v);
			} else if ("d".equals(p)) {
				privateProps.put(p, v);
			} else {
				publicProps.put(p, v);
				privateProps.put(p, v);
			}
		});

		return new KeyPairWithJWK(createKeyPair(jwk), publicProps, privateProps);
	}

	private static Map<String, String> getPublicEllipticJWK(PublicKey publicKey, String javaName) {

		Map<String, String> jwk = new HashMap<>();

		ECPublicKey key = (ECPublicKey) publicKey;
		String x64 = JWBase64.encode(Common.bigIntegerToBytes(key.getW().getAffineX()));
		String y64 = JWBase64.encode(Common.bigIntegerToBytes(key.getW().getAffineY()));

		jwk.put("kty", "EC");
		jwk.put("crv", ECCurves.jwName(javaName));
		jwk.put("x", x64);
		jwk.put("y", y64);

		return jwk;
	}

	private static Map<String, String> getPublicRSAJWK(PublicKey publicKey, int keySize) {

		Map<String, String> jwk = new HashMap<>();

		RSAPublicKey key = (RSAPublicKey) publicKey;

		String m64 = JWBase64.encode(Common.bigIntegerToBytes(key.getModulus()));
		String e64 = JWBase64.encode(Common.bigIntegerToBytes(key.getPublicExponent()));

		jwk.put("kty", "RSA");
		jwk.put("n", m64);
		jwk.put("e", e64);

		return jwk;
	}

	private static Map<String, String> getPrivateEllipticJWK(PrivateKey privateKey, String javaName) {

		Map<String, String> jwk = new HashMap<>();

		ECPrivateKey key = (ECPrivateKey) privateKey;

		String d64 = JWBase64.encode(Common.bigIntegerToBytes(key.getS()));

		jwk.put("kty", "EC");
		jwk.put("crv", ECCurves.jwName(javaName));
		jwk.put("d", d64);
		
		return jwk;
	}

	private static Map<String, String> getPrivateRSAJWK(PrivateKey privateKey, int keySize) {

		Map<String, String> jwk = new HashMap<>();

		RSAPrivateKey key = (RSAPrivateKey) privateKey;

		String m64 = JWBase64.encode(Common.bigIntegerToBytes(key.getModulus()));
		String pe64 = JWBase64.encode(Common.bigIntegerToBytes(key.getPrivateExponent()));

		jwk.put("kty", "RSA");
		jwk.put("d", pe64);
		jwk.put("n", m64);

		return jwk;
	}

	public Map<String, String> getFullJWK() {
		Map<String, String> map = new HashMap<>();
		map.putAll(publicJwk);
		map.putAll(privateJwk);
		return map;
	}

	private static KeyPair createKeyPair(Map<String, String> jwk) throws AcmeException {
		try {

			if ("EC".equals(jwk.get("kty"))) {

				BigInteger d = Common.bigInteger(JWBase64.decode(jwk.get("d")));
				BigInteger x = Common.bigInteger(JWBase64.decode(jwk.get("x")));
				BigInteger y = Common.bigInteger(JWBase64.decode(jwk.get("y")));

				AlgorithmParameters parametersFactory = AlgorithmParameters.getInstance("EC");
				parametersFactory.init(new ECGenParameterSpec(ECCurves.JAVA_NIST_P_256));
				ECParameterSpec parameters = parametersFactory.getParameterSpec(ECParameterSpec.class);

				KeyFactory keyFactory = KeyFactory.getInstance("EC");
				PrivateKey generatePrivate = keyFactory.generatePrivate(new ECPrivateKeySpec(d, parameters));
				PublicKey generatePublic = keyFactory
						.generatePublic(new ECPublicKeySpec(new ECPoint(x, y), parameters));

				return new KeyPair(generatePublic, generatePrivate);
			} else if ("RSA".equals(jwk.get("kty"))) {
				BigInteger modulus = Common.bigInteger(JWBase64.decode(jwk.get("n")));
				BigInteger privateExponent = Common.bigInteger(JWBase64.decode(jwk.get("d")));
				BigInteger publicExponent = Common.bigInteger(JWBase64.decode(jwk.get("e")));

				KeyFactory keyFactory = KeyFactory.getInstance("RSA");
				PrivateKey generatePrivate = keyFactory
						.generatePrivate(new RSAPrivateKeySpec(modulus, privateExponent));
				PublicKey generatePublic = keyFactory.generatePublic(new RSAPublicKeySpec(modulus, publicExponent));

				return new KeyPair(generatePublic, generatePrivate);
			} else {
				throw new IllegalArgumentException(
						"Invalid kty : " + (jwk.containsKey("kty") ? jwk.get("kty") : "null"));
			}

		} catch (Exception ex) {
			throw new AcmeException(ex);
		}
	}
}
