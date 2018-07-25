package xdptdr.asn.pem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.util.Base64;

import org.apache.commons.io.IOUtils;

public class PEMUtils {

	public static byte[] getCertificateSigningRequestBytes(InputStream istream) throws IOException {
		return getBase64Bytes(istream, "-----BEGIN NEW CERTIFICATE REQUEST-----",
				"-----END NEW CERTIFICATE REQUEST-----");
	}

	public static byte[] getCertificateBytes(InputStream istream) throws IOException {
		return getBase64Bytes(istream, "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
	}

	public static String getCertificatePEM(byte[] bytes) {
		return getPEM(bytes, "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
	}

	public static byte[] getPrivateKeyBytes(InputStream istream) throws IOException {
		return getBase64Bytes(istream, "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
	}

	public static String getPrivateKeyPEM(PrivateKey privateKey) {
		return getPEM(privateKey.getEncoded(), "-----BEGIN PRIVATE KEY-----", "-----END PRIVATE KEY-----");
	}

	public static byte[] getBase64Bytes(InputStream istream, String firstLine, String lastLine) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(istream, baos);
		byte[] bytes = baos.toByteArray();
		String str = new String(bytes, Charset.forName("UTF-8"));
		String[] lines = str.split("[\\r\\n]");
		StringWriter sw = new StringWriter();
		for (int i = 1; i < lines.length - 1; ++i) {
			sw.append(lines[i]);
		}
		return Base64.getDecoder().decode(sw.toString());
	}

	private static String getPEM(byte[] bytes, String header, String footer) {
		String b64 = Base64.getEncoder().encodeToString(bytes);
		StringWriter sw = new StringWriter();
		sw.append(header);
		sw.append("\r\n");
		for (int i = 0; i < b64.length(); i += 64) {
			int e = i + 64;
			if (e > b64.length()) {
				e = b64.length();
			}
			sw.append(b64.substring(i, e));
			sw.append("\r\n");
		}
		sw.append(footer);
		sw.append("\r\n");
		return sw.toString();
	}

}
