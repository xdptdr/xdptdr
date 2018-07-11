package xdptdr.acme.crypto;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ECCurves {

	public static final String JAVA_NIST_P_256 = "NIST P-256";
	public static final String JAVA_NIST_P_384 = "NIST P-384";
	public static final String JAVA_NIST_P_521 = "NIST P-521";

	public static final String JW_P_256 = "P-256";
	public static final String JW_P_384 = "P-384";
	public static final String JW_P_521 = "P-521";

	public static final String DISPLAY_P_256 = "P 256";
	public static final String DISPLAY_P_384 = "P 384";
	public static final String DISPLAY_P_521 = "P 521";

	private static final Map<String, String> jwNames = new HashMap<>();
	private static final Map<String, String> displayNames = new HashMap<>();

	static {
		jwNames.put(JAVA_NIST_P_256, JW_P_256);
		jwNames.put(JAVA_NIST_P_384, JW_P_384);
		jwNames.put(JAVA_NIST_P_521, JW_P_521);

		displayNames.put(JAVA_NIST_P_256, DISPLAY_P_256);
		displayNames.put(JAVA_NIST_P_384, DISPLAY_P_384);
		displayNames.put(JAVA_NIST_P_521, DISPLAY_P_521);
	}

	private ECCurves() {
	}

	public static String jwName(String javaName) {
		return jwNames.get(javaName);
	}

	public static String[] javaNames() {
		return new String[] { JAVA_NIST_P_256, JAVA_NIST_P_384, JAVA_NIST_P_521 };
	}

	public static String javaName(String jwName) {
		for (Entry<String, String> entry : jwNames.entrySet()) {
			if (entry.getValue().equals(jwName)) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static String displayName(String javaName) {
		return displayNames.get(javaName);
	}

}
