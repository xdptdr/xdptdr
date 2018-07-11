package xdptdr.common;

public class Bytes {

	private byte[] bytes;
	private int startOffset;
	private int stopOffset;

	public Bytes(byte[] bytes) {
		this(bytes, 0, bytes.length);
	}

	public Bytes(byte[] bytes, int startOffset) {
		this(bytes, startOffset, bytes.length);
	}

	public Bytes(byte[] bytes, int startOffset, int stopOffset) {
		this.bytes = bytes;
		this.startOffset = startOffset;
		this.stopOffset = stopOffset;
	}

	public Bytes(Bytes bytes) {
		this(bytes, 0, bytes.length());
	}

	public Bytes(Bytes bytes, int startOffset) {
		this(bytes, startOffset, bytes.length());
	}

	public Bytes(Bytes bytes, int startOffset, int stopOffset) {
		this.bytes = bytes.bytes;
		this.startOffset = startOffset + bytes.startOffset;
		this.stopOffset = stopOffset + bytes.startOffset;
	}

	public byte at(int idx) {
		if (idx < 0 || idx >= length()) {
			return bytes[bytes.length];
		}
		return bytes[startOffset + idx];
	}

	public int length() {
		return stopOffset - startOffset;
	}

	public Bytes offset(int offset) {
		return new Bytes(this, offset);
	}

	public Bytes offset(Bytes bytes) {
		return offset(bytes.length());
	}

	public Bytes subStartLen(int offset, int length) {
		return new Bytes(this, offset, offset + length);
	}

	public byte[] toByteArray() {
		byte[] bytes = new byte[length()];
		for (int i = 0; i < length(); ++i) {
			bytes[i] = at(i);
		}
		return bytes;
	}
}
