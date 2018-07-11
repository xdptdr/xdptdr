package xdptdr.asn;

public enum AsnClass {

	UNIVERSAL(0), APPLICATION(0x40), CONTEXT_SPECIFIC(0x80), PRIVATE(0xC0);

	private int masked;

	AsnClass(int masked) {
		this.masked = masked;

	}

	public int getMasked() {
		return masked;
	}

	public static AsnClass fromByte(byte b) {

		int masked = b & 0xC0;

		AsnClass[] candidates = values();
		for (int i = 0; i < candidates.length; ++i) {
			AsnClass candidate = candidates[i];
			if (candidate.getMasked() == masked) {
				return candidate;
			}
		}
		return null;

	}

}
