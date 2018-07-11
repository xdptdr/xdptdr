package xdptdr.asn.elements;

import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnOctetString extends AsnElement {

	private byte[] value;

	public AsnOctetString() {
	}

	public AsnOctetString(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		value = contentBytes.toByteArray();
	}

	public AsnOctetString(byte[] value) {
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.OCTET_STRING);
		AsnUtils.addLengthBytes(bytes, value.length);
		AsnUtils.addBytes(bytes, value);
	}

	public AsnElement parse() {
		return AsnUtils.parse(value);
	}
}
