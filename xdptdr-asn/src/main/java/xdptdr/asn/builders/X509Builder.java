package xdptdr.asn.builders;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.Date;

import xdptdr.asn.Asn;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.OIDS;
import xdptdr.asn.elements.AsnSequence;
import xdptdr.asn.elements.AsnUtcTime;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.asn.utils.DistinguishedNameUtils;

public class X509Builder {

	private byte[] encodedPublicKey;

	private int version = 3;

	private int serial;

	private String issuerName;
	private String subjectName;

	private String notBefore;
	private String notAfter;

	private String signatureOid = OIDS.SHA256RSA;

	private byte[] authorityKeyIdentifier;
	private String extKeyUsage;
	private byte[] subjectKeyIdentifier;

	private byte[] acmeExtensionContent;

	private String subjectAlternativeType;
	private String subjectAlternativeName;

	public byte[] getEncodedPublicKey() {
		return encodedPublicKey;
	}

	public void setEncodedPublicKey(byte[] encodedPublicKey) {
		this.encodedPublicKey = encodedPublicKey;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public String getSignatureOid() {
		return signatureOid;
	}

	public void setSignatureOid(String signatureOid) {
		this.signatureOid = signatureOid;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getNotBefore() {
		return notBefore;
	}

	public void setNotBefore(String notBefore) {
		this.notBefore = notBefore;
	}

	public void setNotBefore(Date date) {
		setNotBefore(AsnUtcTime.toString(date));
	}

	public String getNotAfter() {
		return notAfter;
	}

	public void setNotAfter(String notAfter) {
		this.notAfter = notAfter;
	}

	public void setNotAfter(Date date) {
		setNotAfter(AsnUtcTime.toString(date));
	}

	public byte[] getAuthorityKeyIdentifier() {
		return authorityKeyIdentifier;
	}

	public void setAuthorityKeyIdentifier(byte[] authorityKeyIdentifier) {
		this.authorityKeyIdentifier = authorityKeyIdentifier;
	}

	public String getExtKeyUsage() {
		return extKeyUsage;
	}

	public void setExtKeyUsage(String extKeyUsage) {
		this.extKeyUsage = extKeyUsage;
	}

	public byte[] getSubjectKeyIdentifier() {
		return subjectKeyIdentifier;
	}

	public void setSubjectKeyIdentifier(byte[] subjectKeyIdentifier) {
		this.subjectKeyIdentifier = subjectKeyIdentifier;
	}

	public byte[] encode(PrivateKey privateKey)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		AsnSequence extensions = Asn.seq(

				Asn.seq(

						Asn.oid(OIDS.AUTHORITY_KEY_IDENTIFIER),

						Asn.os(Asn.seq(Asn.cs(0, authorityKeyIdentifier, AsnEncoding.PRIMITIVE)))

				),

				Asn.seq(

						Asn.oid(OIDS.EXT_KEY_USAGE),

						Asn.os(Asn.seq(Asn.oid(extKeyUsage)))

				),

				Asn.seq(

						Asn.oid(OIDS.SUBJECT_KEY_IDENTIFIER),

						Asn.os(Asn.os(subjectKeyIdentifier))

				)

		);

		if (acmeExtensionContent != null) {
			extensions.getElements().add(

					Asn.seq(

							Asn.oid(OIDS.ID_PE_ACME_IDENTIFIER_V1),

							Asn.bool(true),

							Asn.os(Asn.os(acmeExtensionContent))

					));
		}

		if (subjectAlternativeName != null) {
			if ("DNS".equals(subjectAlternativeType)) {

				extensions.getElements().add(

						Asn.seq(

								Asn.oid(OIDS.SUBJECT_ALTERNATIVE_NAME),

								Asn.os(Asn.seq(Asn.cs(2, Asn.ia5str(subjectAlternativeName)))

								)

						)

				);

			}
		}

		AsnSequence tbsRoot = Asn.seq(

				Asn.contextSpecific(0, Asn.integer(version - 1)),

				Asn.integer(serial),

				Asn.seq(Asn.oid(signatureOid), Asn.n()),

				DistinguishedNameUtils.toSequence(DistinguishedNameUtils.parse(issuerName)),

				Asn.seq(Asn.time(notBefore), Asn.time(notAfter)),

				DistinguishedNameUtils.toSequence(DistinguishedNameUtils.parse(subjectName)),

				AsnUtils.parse(encodedPublicKey),

				Asn.contextSpecific(3, extensions)

		);

		Signature s = Signature.getInstance(OIDS.getLabel(signatureOid));
		s.initSign(privateKey);
		s.update(tbsRoot.encode());
		byte[] signature = s.sign();

		AsnSequence root = Asn.seq(

				tbsRoot,

				Asn.seq(Asn.oid(signatureOid), Asn.n()),

				Asn.bitstring(signature)

		);

		return root.encode();
	}

	public void setAcmeExtension(byte[] acmeExtensionContent) {
		this.acmeExtensionContent = acmeExtensionContent;
	}

	public void addSubjectAlternativeNameExtension(String sanType, String sanContent) {
		this.subjectAlternativeType = sanType;
		this.subjectAlternativeName = sanContent;

	}

}
