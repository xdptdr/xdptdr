package xdptdr.asn.elements;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;
import xdptdr.common.Common;

public class AsnInteger extends AsnElement {

	private BigInteger value=BigInteger.ZERO;

	public AsnInteger(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		byte first = contentBytes.at(0);
		for (int i = 0; i < contentBytes.length(); ++i) {
			if (i == 0 && (first & 0x80) == 0x80) {
				value = BigInteger.ZERO.subtract(BigInteger.ONE);
			}
			value = value.shiftLeft(8).add(BigInteger.valueOf(contentBytes.at(i) & 0xFF));
		}
	}

	public AsnInteger(BigInteger value) {
		this.value = value;
	}

	public AsnInteger(long value) {
		this.value = BigInteger.valueOf(value);
	}

	public BigInteger getValue() {
		return value;
	}

	public void setValue(BigInteger value) {
		this.value = value;
	}

	@Override
	public void encode(List<Byte> bytes) {

		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.INTEGER);

		List<Byte> cb = new ArrayList<>();

		BigInteger v = value;

		while (v.compareTo(new BigInteger("FF", 16)) == 1) {

			cb.add(Common.bit(v.and(new BigInteger("FF", 16)).intValue()));
			v = v.shiftRight(8);
		}

		int lastByte = v.and(new BigInteger("FF", 16)).intValue();
		cb.add(Common.bit(lastByte));

		if (value.compareTo(BigInteger.ZERO) == 1 && (lastByte & 0x80) == 0x80) {
			cb.add(Common.bit(0));
		} else if (value.compareTo(BigInteger.ZERO) == -1 && v.and(new BigInteger("FF", 16)).intValue() < 0x80) {
			cb.add(Common.bit(0xFF));
		}

		AsnUtils.addLengthBytes(bytes, cb.size());

		for (int i = cb.size() - 1; i >= 0; --i) {
			bytes.add(cb.get(i));
		}

	}

}
