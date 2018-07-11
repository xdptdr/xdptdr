package xdptdr.asn.elements;

import java.nio.charset.Charset;
import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnIA5String extends AsnElement {

	private String value;

	public AsnIA5String(String value) {
		this.value = value;
	}

	public AsnIA5String(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		value = new String(contentBytes.toByteArray(), Charset.forName("UTF8"));
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.IA5STRING);
		AsnUtils.addLengthBytes(bytes, value.length());
		AsnUtils.addBytes(bytes, value.getBytes());
	}

}
