package xdptdr.acme.jw;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

public class JWBase64 {

	private static Decoder decoder = Base64.getDecoder();
	private static Encoder encoder = Base64.getEncoder();

	public static byte[] decode(String s) {
		s = s.replaceAll("-", "+");
		s = s.replaceAll("_", "/");
		int m = s.length() % 4;
		if (m == 2) {
			s += "==";
		} else if (m == 3) {
			s += "=";
		}
		return decoder.decode(s);
	}

	public static String encode(byte[] bytes) {
		String e = encoder.encodeToString(bytes);
		e = e.split("=")[0];
		e = e.replaceAll("/", "_");
		e = e.replaceAll("\\+", "-");
		return e;
	}

}
