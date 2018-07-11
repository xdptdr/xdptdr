package xdptdr.acme.jw;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import xdptdr.acme.crypto.ECCurves;
import xdptdr.acme.crypto.ECSignature;
import xdptdr.acme.crypto.ECSigner;
import xdptdr.acme.crypto.RSASigner;
import xdptdr.acme.v2.AcmeSession;
import xdptdr.common.Common;

public class JWSBuilder {

	public static Map<String, Object> build(AcmeSession session, Object payload, String url) throws Exception {

		String nonce = session.getNonce();
		ObjectMapper om = session.getOm();
		PrivateKey privateKey = session.getKeyPairWithJWK().getKeyPair().getPrivate();

		Map<String, Object> protekted = new HashMap<>();
		protekted.put("alg", getAlg(privateKey));
		if (session.getAccount() == null) {
			protekted.put("jwk", session.getKeyPairWithJWK().getPublicJwk());
		} else {
			String kid = session.getAccount().getUrl();
			protekted.put("kid", kid);
		}
		protekted.put("nonce", nonce);
		protekted.put("url", url);
		String protected64 = JWBase64.encode(om.writeValueAsBytes(protekted));

		String payload64 = JWBase64.encode(om.writeValueAsBytes(payload));

		byte[] tbs = (protected64 + "." + payload64).getBytes();

		byte[] signatureBytes = sign(privateKey, tbs);

		String signature64 = JWBase64.encode(signatureBytes);

		Map<String, Object> jws = new HashMap<>();
		jws.put("protected", protected64);
		jws.put("payload", payload64);
		jws.put("signature", signature64);
		return jws;
	}

	private static String getAlg(PrivateKey privateKey) {
		if (privateKey instanceof ECPrivateKey) {
			return JWA.ES256;
		} else if (privateKey instanceof RSAPrivateKey) {
			return JWA.RS256;
		} else {
			throw new IllegalArgumentException(
					"Unknown private key type : " + (privateKey == null ? "null" : privateKey.getClass().getName()));
		}
	}

	private static byte[] sign(PrivateKey privateKey, byte[] tbs) throws Exception {
		if (privateKey instanceof ECPrivateKey) {
			ECPrivateKey key = (ECPrivateKey) privateKey;

			BigInteger s = key.getS();
			ECSignature signature = ECSigner.sign(tbs, ECCurves.JAVA_NIST_P_256, s);

			byte[] rBytes = Common.bigIntegerToBytes(signature.getR());
			byte[] sBytes = Common.bigIntegerToBytes(signature.getS());

			return Common.concatenate(rBytes, sBytes);
		} else if (privateKey instanceof RSAPrivateKey) {
			RSAPrivateKey key = (RSAPrivateKey) privateKey;
			return RSASigner.sign(tbs, key.getModulus(), key.getPrivateExponent());
		} else {
			throw new IllegalArgumentException(
					"Unknown private key type : " + (privateKey == null ? "null" : privateKey.getClass().getName()));
		}
	}
}
