
package xdptdr.asn.pkcs12;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import xdptdr.asn.Asn;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.OIDS;
import xdptdr.asn.elements.AsnElement;
import xdptdr.asn.elements.AsnSequence;
import xdptdr.common.Common;

public class PKCS12Builder {

	public byte[] privateKeyEncryptionSalt = Common.bytes("8FD2439C4F78EB6612D94F642C7B75FBBF5FF371");
	private int privateKeyEncryptionIterationCount = 50000;

	public byte[] certificateEncryptionSalt = Common.bytes("1B9DCA53442A0222F5DA254DBBC62C7C6FD2E60E");
	private int certificateEncryptionIterationCount = 50000;

	public String friendlyName = "clientca";

	public byte[] macSalt = Common.bytes("6CB867698D2BF58AC0ECA315C192DEC3C76AF213");
	public int macIterationCount = 100000;

	public byte[] certificateBytes;

	public byte[] privateKeyBytes;
	private String privateKeyCipher = OIDS.PBE_WITH_SHA_AND_3_KEY_TRIPLE_DES_CBC;
	private String certificateCipher = OIDS.PBE_WITH_SHA_AND_40_BIT_RC2_CBC;
	private String certificatePassword = "password";
	private String privateKeyPassword = "password";
	private String macAlgo = "HmacPBESHA1";

	public String localKeyId = "Time 1527240679023";

	public PKCS12Builder(X509Certificate x, PrivateKey k) throws CertificateEncodingException {
		certificateBytes = x.getEncoded();
		privateKeyBytes = k.getEncoded();
	}

	public void setPrivateKeyEncryptionSalt(byte[] privateKeyEncryptionSalt) {
		this.privateKeyEncryptionSalt = privateKeyEncryptionSalt;
	}

	public void setPrivateKeyEncryptionIterationCount(int privateKeyEncryptionIterationCount) {
		this.privateKeyEncryptionIterationCount = privateKeyEncryptionIterationCount;
	}

	public void setCertificateEncryptionSalt(byte[] certificateEncryptionSalt) {
		this.certificateEncryptionSalt = certificateEncryptionSalt;
	}

	public void setCertificateEncryptionIterationCount(int certificateEncryptionIterationCount) {
		this.certificateEncryptionIterationCount = certificateEncryptionIterationCount;
	}

	public void setFriendlyName(String friendlyName) {
		this.friendlyName = friendlyName;
	}

	public void setMacSalt(byte[] macSalt) {
		this.macSalt = macSalt;
	}

	public void setMacIterationCount(int macIterationCount) {
		this.macIterationCount = macIterationCount;
	}

	public void setPrivateKeyCipher(String privateKeyCipher) {
		this.privateKeyCipher = privateKeyCipher;
	}

	public void setCertificateCipher(String certificateCipher) {
		this.certificateCipher = certificateCipher;
	}

	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}

	public void setPrivateKeyPassword(String privateKeyPassword) {
		this.privateKeyPassword = privateKeyPassword;
	}

	public void setMacAlgo(String macAlgo) {
		this.macAlgo = macAlgo;
	}

	public void setLocalKeyId(String localKeyId) {
		this.localKeyId = localKeyId;
	}

	public byte[] getPrivateKeyBytes() {
		return privateKeyBytes;
	}

	public byte[] getCertificateBytes() {
		return certificateBytes;
	}

	public byte[] getMacSalt() {
		return macSalt;
	}

	public int getMacIterationCount() {
		return macIterationCount;
	}

	public byte[] build()
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		return getAsn().encode();
	}

	public AsnElement getAsn()
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {

		byte[] contentValue = getContentBytes();

		return Asn.seq(

				Asn.integer(3),

				Asn.seq(

						Asn.oid(OIDS.DATA),

						Asn.cs(0, Asn.os(contentValue))

				),

				getMacAsn(contentValue)

		);
	}

	public byte[] getContentBytes()
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {

		return Asn.seq(

				Asn.seq(Asn.oid(OIDS.DATA), Asn.cs(0, Asn.os(getPrivateKeyAsn()))),

				Asn.seq(Asn.oid(OIDS.ENCRYPTED_DATA), Asn.cs(0, getEncryptedCertificateAsn()))

		).encode();
	}

	public AsnElement getMacAsn(byte[] content) throws InvalidKeyException, InvalidKeySpecException,
			NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException, IOException {

		return Asn.seq(

				Asn.seq(

						Asn.seq(Asn.oid(OIDS.HASH_ALGORITHM_IDENTIFIER), Asn.n()),

						Asn.os(getMacHash(content))

				),

				Asn.os(macSalt),

				Asn.integer(macIterationCount)

		);
	}

	public AsnSequence getEncryptedCertificateAsn()
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {

		return Asn.seq(

				Asn.integer(0),

				Asn.seq(

						Asn.oid(OIDS.DATA),

						Asn.seq(Asn.oid(certificateCipher), getCertificateCipherParameters()),

						Asn.cs(0, getEncryptedCertificateBytes(), AsnEncoding.PRIMITIVE)

				)

		);
	}

	public AsnSequence getPrivateKeyAsn()
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		return Asn.seq(

				Asn.seq(

						Asn.oid(OIDS.PKCS_8_SHROUDED_KEY_BAG),

						Asn.cs(0, Asn.seq(

								Asn.seq(Asn.oid(privateKeyCipher), getPrivateKeyCipherParameters()),

								Asn.os(getEncryptedPrivateKeyBytes())

						)),

						Asn.set(

								Asn.seq(Asn.oid(OIDS.FRIENDLY_NAME), Asn.set(Asn.bmpstring(friendlyName))),

								Asn.seq(Asn.oid(OIDS.LOCAL_KEY_ID), Asn.set(Asn.os(localKeyId.getBytes())))

						)

				));
	}

	public AsnSequence getToBeEncryptedCertificateAsn() {

		return Asn.seq(

				Asn.seq(

						Asn.oid(OIDS.CERT_BAG),

						Asn.cs(0, Asn.seq(Asn.oid(OIDS.X509_CERTIFICATE), Asn.cs(0, Asn.os(certificateBytes)))),

						Asn.set(

								Asn.seq(

										Asn.oid(OIDS.FRIENDLY_NAME),

										Asn.set(Asn.bmpstring(friendlyName))

								),

								Asn.seq(

										Asn.oid(OIDS.LOCAL_KEY_ID),

										Asn.set(Asn.os(localKeyId.getBytes()))

								)

						)

				)

		);
	}

	public byte[] getEncryptedCertificateBytes()
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {

		byte[] plain = getToBeEncryptedCertificateAsn().encode();
		byte[] input = plain;
		byte[] parameters = getCertificateCipherParameters().encode();
		return encryptCertificate(certificatePassword, input, parameters);
	}

	public byte[] getEncryptedPrivateKeyBytes()
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException {
		byte[] input = privateKeyBytes;
		byte[] parameters = getPrivateKeyCipherParameters().encode();
		return encryptPrivateKey(privateKeyPassword, input, parameters);
	}

	public AsnSequence getCertificateCipherParameters() {
		return Asn.seq(Asn.os(certificateEncryptionSalt), Asn.integer(certificateEncryptionIterationCount));
	}

	public AsnSequence getPrivateKeyCipherParameters() {
		return Asn.seq(Asn.os(privateKeyEncryptionSalt), Asn.integer(privateKeyEncryptionIterationCount));
	}

	private byte[] encryptCertificate(String password, byte[] input, byte[] parameters)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		SecretKey key = SecretKeyFactory.getInstance("PBE").generateSecret(new PBEKeySpec(password.toCharArray()));

		AlgorithmParameters ap = AlgorithmParameters.getInstance("PBE");
		ap.init(parameters);

		Cipher cipher = Cipher.getInstance(certificateCipher);
		cipher.init(Cipher.ENCRYPT_MODE, key, ap);
		return cipher.doFinal(input);
	}

	private byte[] encryptPrivateKey(String password, byte[] input, byte[] parameters)
			throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, NoSuchPaddingException,
			InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		SecretKey key = SecretKeyFactory.getInstance("PBE").generateSecret(new PBEKeySpec(password.toCharArray()));

		AlgorithmParameters ap = AlgorithmParameters.getInstance("PBE");
		ap.init(parameters);

		Cipher cipher = Cipher.getInstance(privateKeyCipher);
		cipher.init(Cipher.ENCRYPT_MODE, key, ap);
		return cipher.doFinal(input);
	}

	public byte[] getMacHash(byte[] content) throws InvalidKeySpecException, NoSuchAlgorithmException,
			InvalidKeyException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, IOException {

		SecretKey key = SecretKeyFactory.getInstance("PBE")
				.generateSecret(new PBEKeySpec(certificatePassword.toCharArray()));
		PBEParameterSpec params = new PBEParameterSpec(macSalt, macIterationCount);

		Mac mac = Mac.getInstance(macAlgo);
		mac.init(key, params);
		mac.update(content);
		return mac.doFinal();

	}
}
