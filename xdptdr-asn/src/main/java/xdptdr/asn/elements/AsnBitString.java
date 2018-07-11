package xdptdr.asn.elements;

import java.util.BitSet;
import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnBitString extends AsnElement {

	private BitSet bits = new BitSet();

	private int size;

	public AsnBitString() {
	}

	public AsnBitString(boolean[] booleans) {
		size = booleans.length;
		bits = new BitSet(size);
		for (int i = 0; i < booleans.length; ++i) {
			bits.set(i, booleans[i]);
		}
	}

	public AsnBitString(byte[] bytes) {
		init(bytes, 0);
	}

	public AsnBitString(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		int unused = contentBytes.at(0);
		Bytes cb = contentBytes.offset(1);
		init(cb.toByteArray(), unused);
	}

	private void init(byte[] bytes, int unused) {
		size = bytes.length * 8 - unused;
		bits = new BitSet(size);
		bits = new BitSet(size);
		for (int i = 0; i < bytes.length; ++i) {
			boolean isset = (bytes[i] & 0x80) == 0x80;
			bits.set(8 * i, isset);
			if (i < bytes.length - 1 || unused <= 6) {
				bits.set(8 * i + 1, (bytes[i] & 0x40) == 0x40);
			}
			if (i < bytes.length - 1 || unused <= 5) {
				bits.set(8 * i + 2, (bytes[i] & 0x20) == 0x20);
			}
			if (i < bytes.length - 1 || unused <= 4) {
				bits.set(8 * i + 3, (bytes[i] & 0x10) == 0x10);
			}
			if (i < bytes.length - 1 || unused <= 3) {
				bits.set(8 * i + 4, (bytes[i] & 0x8) == 0x8);
			}
			if (i < bytes.length - 1 || unused <= 2) {
				bits.set(8 * i + 5, (bytes[i] & 0x4) == 0x4);
			}
			if (i < bytes.length - 1 || unused <= 1) {
				bits.set(8 * i + 6, (bytes[i] & 0x2) == 0x2);
			}
			if (i < bytes.length - 1 || unused == 0) {
				bits.set(8 * i + 7, (bytes[i] & 0x1) == 0x1);
			}
		}
	}

	public byte[] toByteArray() {
		int sz = size / 8 + (size % 8 == 0 ? 0 : 1);
		byte[] bytes = new byte[sz];
		for (int i = 0; i < sz; ++i) {
			int a = bits.get(8 * i + 0) ? 0x80 : 0x0;
			int b = bits.get(8 * i + 1) ? 0x40 : 0x0;
			int c = bits.get(8 * i + 2) ? 0x20 : 0x0;
			int d = bits.get(8 * i + 3) ? 0x10 : 0x0;
			int e = bits.get(8 * i + 4) ? 0x8 : 0x0;
			int f = bits.get(8 * i + 5) ? 0x4 : 0x0;
			int g = bits.get(8 * i + 6) ? 0x2 : 0x0;
			int h = bits.get(8 * i + 7) ? 0x1 : 0x0;
			int val = a + b + c + d + e + f + g + h;
			bytes[i] = (byte) val;
		}
		return bytes;
	}

	public int getSize() {
		return size;
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.BIT_STRING);
		AsnUtils.addLengthBytes(bytes, 1 + size / 8 + (size % 8 == 0 ? 0 : 1));
		AsnUtils.addBytes(bytes, new byte[] { (byte) ((8 - (size % 8)) % 8) });
		AsnUtils.addBytes(bytes, toByteArray());
	}

	public boolean get(int i) {
		return bits.get(i);
	}

	public AsnElement parse() {
		return AsnUtils.parse(toByteArray());
	}

}