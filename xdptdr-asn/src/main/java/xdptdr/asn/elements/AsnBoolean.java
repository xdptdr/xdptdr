package xdptdr.asn.elements;

import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnBoolean extends AsnElement {

	private boolean value;

	public AsnBoolean(boolean value) {
		this.value = value;
	}

	public AsnBoolean(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		value = contentBytes.at(0) != 0;
	}

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.BOOLEAN);
		AsnUtils.addLengthBytes(bytes, 1);
		AsnUtils.addBytes(bytes, new byte[] { (byte) (value ? 0 : 0xFF) });
	}

}
