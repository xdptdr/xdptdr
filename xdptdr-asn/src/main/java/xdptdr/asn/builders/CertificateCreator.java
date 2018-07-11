package xdptdr.asn.builders;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Date;

import xdptdr.asn.OIDS;
import xdptdr.asn.utils.X509CertificateUtils;

public class CertificateCreator {

	public static byte[] signCertificateWithAuthority(X509Certificate clientCertificate, X509Certificate caCertificate, int serialNumber,
			Date notBefore, Date notAfter, PrivateKey caPrivateKey)
			throws InvalidKeyException, NoSuchAlgorithmException, SignatureException {

		PublicKey clientPublicKey = clientCertificate.getPublicKey();
		byte[] caSubjectKeyIdentifier = X509CertificateUtils.getSubjectKeyIdentifier(caCertificate);
		byte[] clientSubjectKeyIdentifier = X509CertificateUtils.getSubjectKeyIdentifier(clientCertificate);

		X509Builder b = new X509Builder();
		b.setEncodedPublicKey(clientPublicKey.getEncoded());
		b.setSerial(serialNumber);
		b.setNotBefore(notBefore);
		b.setNotAfter(notAfter);
		b.setAuthorityKeyIdentifier(caSubjectKeyIdentifier);
		b.setSubjectKeyIdentifier(clientSubjectKeyIdentifier);
		b.setExtKeyUsage(OIDS.CLIENT_AUTH);
		b.setIssuerName(caCertificate.getSubjectDN().getName());
		b.setSubjectName(clientCertificate.getSubjectDN().getName());
		return b.encode(caPrivateKey);
	}
	
}
