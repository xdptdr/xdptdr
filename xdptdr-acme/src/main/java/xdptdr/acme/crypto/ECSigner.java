package xdptdr.acme.crypto;

import java.math.BigInteger;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import xdptdr.asn.Asn;
import xdptdr.asn.elements.AsnElement;
import xdptdr.asn.elements.AsnSequence;
import xdptdr.asn.utils.AsnUtils;

public class ECSigner {
	public static ECSignature sign(byte[] bytes, String curve, BigInteger d) throws NoSuchAlgorithmException,
			InvalidParameterSpecException, InvalidKeySpecException, InvalidKeyException, SignatureException {

		ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curve);

		AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("EC");
		algorithmParameters.init(ecGenParameterSpec);

		ECParameterSpec ecParameterSpec = algorithmParameters.getParameterSpec(ECParameterSpec.class);

		ECPrivateKeySpec ecPrivateKeySpec = new ECPrivateKeySpec(d, ecParameterSpec);

		KeyFactory keyFactory = KeyFactory.getInstance("EC");

		PrivateKey privateKey = keyFactory.generatePrivate(ecPrivateKeySpec);

		Signature signer = Signature.getInstance("SHA256withECDSA");
		signer.initSign(privateKey);
		signer.update(bytes);
		byte[] signature = signer.sign();

		AsnElement signatureAsn = AsnUtils.parse(signature);

		BigInteger r = signatureAsn.asSequence().getInteger(0).getValue();
		BigInteger s = signatureAsn.asSequence().getInteger(1).getValue();

		return new ECSignature(r, s);

	}

	public static void verify(ECSignature signature, byte[] input, String curve, BigInteger x, BigInteger y)
			throws NoSuchAlgorithmException, InvalidParameterSpecException, InvalidKeySpecException,
			InvalidKeyException, SignatureException {

		ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curve);

		AlgorithmParameters algorithmParameters = AlgorithmParameters.getInstance("EC");
		algorithmParameters.init(ecGenParameterSpec);

		ECParameterSpec ecParameterSpec = algorithmParameters.getParameterSpec(ECParameterSpec.class);

		ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(new ECPoint(x, y), ecParameterSpec);

		KeyFactory keyFactory = KeyFactory.getInstance("EC");
		PublicKey publicKey = keyFactory.generatePublic(ecPublicKeySpec);

		AsnSequence asn = Asn.seq(Asn.integer(signature.getR()), Asn.integer(signature.getS()));

		Signature si = Signature.getInstance("SHA256withECDSA");
		si.initVerify(publicKey);
		si.update(input);
		si.verify(asn.encode());

	}

}
