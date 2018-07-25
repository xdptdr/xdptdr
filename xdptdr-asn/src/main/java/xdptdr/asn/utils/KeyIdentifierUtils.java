package xdptdr.asn.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

public class KeyIdentifierUtils {

	public static byte[] getKeyIdentifier(PublicKey publicKey) throws NoSuchAlgorithmException {
		byte[] bytes = publicKey.getEncoded();
		MessageDigest md = MessageDigest.getInstance("SHA");
		return md.digest(AsnUtils.parse(bytes).asSequence().getBitString(1).toByteArray());
	}

}
