package xdptdr.acme.crypto;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class RSASigner {

	public static byte[] sign(byte[] input, BigInteger modulus, BigInteger privateExponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

		RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, privateExponent);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PrivateKey privateKey = keyFactory.generatePrivate(rsaPrivateKeySpec);

		Signature signer = Signature.getInstance("SHA256withRSA");
		signer.initSign(privateKey);
		signer.update(input);
		return signer.sign();
	}

	public static void verify(byte[] signature, byte[] input, BigInteger modulus, BigInteger publicExponent)
			throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {

		RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, publicExponent);

		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(rsaPublicKeySpec);

		Signature signer = Signature.getInstance("SHA256withRSA");
		signer.initVerify(publicKey);
		signer.update(input);
		signer.verify(signature);

	}
	
}
