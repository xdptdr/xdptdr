package xpdtr.acme.gui.fiddling;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class CertificateRetriever {

	public static X509Certificate getCertificate(String hostname)
			throws NoSuchAlgorithmException, KeyManagementException, IOException {

		CollectingTrustManager collectingTrustManager = new CollectingTrustManager();

		KeyManager[] keyManagers = new KeyManager[0];
		TrustManager[] trustManagers = new TrustManager[] { collectingTrustManager };
		SecureRandom secureRandom = new SecureRandom();

		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(keyManagers, trustManagers, secureRandom);

		SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

		SSLSocket s = (SSLSocket) sslSocketFactory.createSocket(hostname, 443);
		s.startHandshake();
		s.close();

		return collectingTrustManager.getFirst();

	}

}
