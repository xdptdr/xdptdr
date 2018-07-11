package xdptdr.asn.elements;

import java.nio.charset.Charset;
import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnPrintableString extends AsnElement {

	private String value;

	public AsnPrintableString() {
	}

	public AsnPrintableString(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		value = new String(contentBytes.toByteArray(), Charset.forName("UTF-8"));
	}

	public AsnPrintableString(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.PRINTABLE_STRING);
		AsnUtils.addLengthBytes(bytes, value.length());
		AsnUtils.addBytes(bytes, value.getBytes());
	}

}
