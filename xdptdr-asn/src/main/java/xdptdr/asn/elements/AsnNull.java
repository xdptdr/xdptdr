package xdptdr.asn.elements;

import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnNull extends AsnElement {

	public AsnNull() {
	}

	public AsnNull(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.NULL);
		AsnUtils.addLengthBytes(bytes, 0);
	}

}
