package xdptdr.asn;

public enum AsnEncoding {

	PRIMITIVE(0), CONSTRUCTED(0x20);

	private int masked;

	AsnEncoding(int masked) {
		this.masked = masked;
	}

	public int getMasked() {
		return masked;
	}

	public static AsnEncoding fromByte(byte b) {

		int masked = b & 0x20;

		AsnEncoding[] candidates = values();
		for (int i = 0; i < candidates.length; ++i) {
			AsnEncoding candidate = candidates[i];
			if (candidate.getMasked() == masked) {
				return candidate;
			}
		}

		return null;
	}

}
