package xdptdr.asn.interpretations;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

import xdptdr.asn.OIDS;
import xdptdr.asn.elements.AsnBitString;
import xdptdr.asn.elements.AsnContextSpecific;
import xdptdr.asn.elements.AsnElement;
import xdptdr.asn.elements.AsnInteger;
import xdptdr.asn.elements.AsnObjectIdentifier;
import xdptdr.asn.elements.AsnSequence;
import xdptdr.asn.elements.AsnUtcTime;
import xdptdr.asn.utils.AsnInterpretationUtils;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.exceptions.NotAvailableException;

public class AsnX509Interpretation {

	private AsnElement asn;

	private AsnX509InterpretationType type;

	public AsnX509Interpretation(AsnElement tbsAsn, AsnX509InterpretationType type) {
		this.asn = tbsAsn;
		this.type = type;
	}

	public AsnX509Interpretation(X509Certificate cax) throws CertificateEncodingException {
		this(AsnUtils.parse(cax.getEncoded()), AsnX509InterpretationType.FULL);
	}

	public AsnX509Interpretation(byte[] bytes, AsnX509InterpretationType type) {
		this(AsnUtils.parse(bytes), type);
	}

	public AsnElement getTbs() {
		if (type == AsnX509InterpretationType.TBS) {
			return asn;
		} else {
			return asn.asSequence().get(0);
		}
	}

	public String getIssuerName() {
		AsnSequence issuerNameSequence = getTbs().as(AsnSequence.class).getElements().get(3).as(AsnSequence.class);
		return AsnInterpretationUtils.mapToNameString(issuerNameSequence);
	}

	public String getSubjectName() {
		AsnSequence subjectNameSequence = getTbs().as(AsnSequence.class).getElements().get(5).as(AsnSequence.class);
		return AsnInterpretationUtils.mapToNameString(subjectNameSequence);
	}

	public Date getNotBefore() {
		return getTbs().as(AsnSequence.class).getElements().get(4).as(AsnSequence.class).getElements().get(0)
				.as(AsnUtcTime.class).toDate();
	}

	public Date getNotAfter() {
		return getTbs().as(AsnSequence.class).getElements().get(4).as(AsnSequence.class).getElements().get(1)
				.as(AsnUtcTime.class).toDate();
	}

	public String getSigAlgOID() {
		return getTbs().as(AsnSequence.class).getElements().get(2).as(AsnSequence.class).getElements().get(0)
				.as(AsnObjectIdentifier.class).getValue();
	}

	public String getSigAlgName() {
		return OIDS.getLabel(getSigAlgOID());
	}

	public int getVersion() {
		return (int) (getTbs().as(AsnSequence.class).getElements().get(0).as(AsnContextSpecific.class).get()
				.as(AsnInteger.class).getValue().intValue() + 1);
	}

	public long getSerialNumber() {
		return getTbs().as(AsnSequence.class).getElements().get(1).as(AsnInteger.class).getValue().longValue();
	}

	public byte[] getSignature() {
		if (type == AsnX509InterpretationType.FULL) {
			return asn.as(AsnSequence.class).getElements().get(2).as(AsnBitString.class).toByteArray();
		} else {
			throw new NotAvailableException("Signature requires all the data");
		}
	}

	public AsnContextSpecific getContextSpecific(int i) {
		if (i == 3) {
			return getTbs().asSequence().getContextSpecific(7);
		}
		return null;
	}

	public AsnElement getExtensionBytes(String oid) {
		AsnSequence asnExts = getContextSpecific(3).getSequence();
		for (AsnElement element : asnExts) {
			AsnSequence seq = element.asSequence();
			if (oid.equals(seq.getObjectIdentifier(0).getValue())) {
				return seq.get(1);
			}
		}
		return null;
	}

	public boolean[] getKeyUsage() {

		boolean[] keyUsage = new boolean[9];

		byte[] extensionBytes = getExtensionBytes(OIDS.KEY_USAGE).asOctetString().getValue();
		AsnBitString bits = AsnUtils.parse(extensionBytes).asBitString();

		for (int i = 0; i < keyUsage.length; ++i) {
			keyUsage[i] = bits.get(i);
		}

		return keyUsage;
	}

	public int getBasicConstraints() {

		byte[] extensionBytes = getExtensionBytes(OIDS.BASIC_CONSTRAINTS).asOctetString().getValue();
		AsnSequence seq = AsnUtils.parse(extensionBytes).asSequence();
		boolean set = seq.getBoolean(0).getValue();

		if (set) {
			return Integer.MAX_VALUE;
		} else {
			return -1;
		}
	}

	public boolean isKeyCertSign() {
		return getKeyUsage()[5];
	}

	public byte[] getExtension(String oid) {
		return getExtensionBytes(oid).asOctetString().getValue();
	}

	public byte[] getSubjectKeyIdentifier() {
		return AsnUtils.parse(getExtension(OIDS.SUBJECT_KEY_IDENTIFIER)).asOctetString().getValue();
	}

	public byte[] getAuthorityKeyIdentifier() {
		return AsnUtils.parse(getExtension(OIDS.AUTHORITY_KEY_IDENTIFIER)).asSequence().getContextSpecific(0)
				.getValue();
	}

	public String getExtKeyUsage() {
		return AsnUtils.parse(getExtension(OIDS.EXT_KEY_USAGE)).asSequence().getObjectIdentifier(0).getValue();
	}

	public String getPublicKeyAlgorithmOID() {
		return getTbs().asSequence().getSequence(6).getSequence(0).getObjectIdentifier(0).getValue();
	}

	public String getPublicKeyAlgorithm() {
		return OIDS.getLabel(getPublicKeyAlgorithmOID());
	}

	public byte[] getPublicKeyEncoded() {
		return getTbs().asSequence().getSequence(6).encode();
	}

}
