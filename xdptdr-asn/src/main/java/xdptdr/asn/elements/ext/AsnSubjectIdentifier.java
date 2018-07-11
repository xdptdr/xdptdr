package xdptdr.asn.elements.ext;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import xdptdr.asn.elements.AsnElement;
import xdptdr.common.Bytes;

public class AsnSubjectIdentifier extends AsnElement {

	private byte[] value;

	public AsnSubjectIdentifier() {
	}

	public AsnSubjectIdentifier(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		value = contentBytes.toByteArray();
	}

	@XmlJavaTypeAdapter(HexBinaryAdapter.class)
	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

}
