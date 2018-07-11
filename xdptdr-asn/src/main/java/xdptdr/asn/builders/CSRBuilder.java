package xdptdr.asn.builders;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import xdptdr.asn.Asn;
import xdptdr.asn.OIDS;
import xdptdr.asn.elements.AsnBitString;
import xdptdr.asn.elements.AsnSequence;
import xdptdr.asn.elements.AsnSet;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.asn.utils.DistinguishedNameUtils;

public class CSRBuilder {

	private int version;
	private String subjectName;
	private PublicKey publicKey;
	private byte[] subjectKeyIdentifier;

	public void setVersion(int version) {
		this.version = version;
	}

	public void setSubjectName(String name) {
		this.subjectName = name;
	}

	public void setPublickKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public void setSubjectKeyIdentifier(byte[] subjectKeyIdentifier) {
		this.subjectKeyIdentifier = subjectKeyIdentifier;
	}

	public byte[] encode(PrivateKey privateKey)
			throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {

		AsnSequence ski = null;

		if (subjectKeyIdentifier != null) {
			ski = Asn.seq(Asn.seq(Asn.oid(OIDS.SUBJECT_KEY_IDENTIFIER), Asn.os(subjectKeyIdentifier)));
		}

		AsnSet extensions = Asn.set();

		if (ski != null) {
			extensions.getElements().add(ski);
		}

		AsnSequence tbs = Asn.seq(

				Asn.integer(version),

				DistinguishedNameUtils.toSequence(DistinguishedNameUtils.parse(subjectName)),

				Asn.raw(publicKey.getEncoded())

		);

		if (extensions.getElements().isEmpty()) {
			tbs.getElements().add(Asn.cs(0, Asn.seq(Asn.oid(OIDS.EXTENSION_REQUEST), extensions)));
		}

		Signature s = Signature.getInstance(OIDS.getLabel(OIDS.SHA256RSA));
		s.initSign(privateKey);
		s.update(AsnUtils.encode(tbs));
		byte[] signature = s.sign();

		return Asn.seq(tbs, Asn.seq(Asn.oid(OIDS.SHA256RSA), Asn.n()), new AsnBitString(signature)).encode();
	}

}
