package xdptdr.asn;

public enum AsnTag {
	BOOLEAN(0x1), INTEGER(0x2), BIT_STRING(0x3), OCTET_STRING(0x4), NULL(0x5), OBJECT_IDENTIFIER(0x6), SEQUENCE(
			0x10), SET(0x11), PRINTABLE_STRING(0x13), IA5STRING(0x16), UTC_TIME(0x17),

	SUBJECT_IDENTIFIER(0xD), BMP_STRING(0x1E);

	private int tagNumber;

	private AsnTag(int tagNumber) {
		this.tagNumber = tagNumber;
	}

	public int getTagNumber() {
		return tagNumber;
	}

	public static AsnTag fromTagNumber(int tagNumber) {
		AsnTag[] candidates = values();
		for (int i = 0; i < candidates.length; ++i) {
			AsnTag candidate = candidates[i];
			if (candidate.getTagNumber() == tagNumber) {
				return candidate;
			}
		}
		return null;
	}

}
