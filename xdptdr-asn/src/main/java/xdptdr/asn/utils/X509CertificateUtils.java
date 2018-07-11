package xdptdr.asn.utils;

import java.security.cert.X509Certificate;

import xdptdr.asn.OIDS;
import xdptdr.asn.utils.AsnUtils;

public class X509CertificateUtils {

	public static byte[] getSubjectKeyIdentifier(X509Certificate caCertificate) {
		return AsnUtils.parse(
				AsnUtils.parse(caCertificate.getExtensionValue(OIDS.SUBJECT_KEY_IDENTIFIER)).asOctetString().getValue())
				.asOctetString().getValue();
	}

}
