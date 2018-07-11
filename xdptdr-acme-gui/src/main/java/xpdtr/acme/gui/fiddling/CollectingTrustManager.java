package xpdtr.acme.gui.fiddling;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.X509TrustManager;

public class CollectingTrustManager implements X509TrustManager {

	private List<X509Certificate> certificates = new ArrayList<>();

	@Override
	public void checkClientTrusted(X509Certificate[] certificates, String str) throws CertificateException {
		collect(certificates);
	}

	@Override
	public void checkServerTrusted(X509Certificate[] certificates, String str) throws CertificateException {
		collect(certificates);
	}

	private void collect(X509Certificate[] certificates) {
		if (certificates != null) {
			for (X509Certificate y : certificates) {
				this.certificates.add(y);
			}
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return certificates.toArray(new X509Certificate[] {});
	}

	public X509Certificate getFirst() {
		return certificates.size() > 0 ? certificates.get(0) : null;
	}

}
