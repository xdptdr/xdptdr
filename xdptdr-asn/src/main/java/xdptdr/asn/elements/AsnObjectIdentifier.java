package xdptdr.asn.elements;

import java.util.ArrayList;
import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnObjectIdentifierUtils;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnObjectIdentifier extends AsnElement {

	private String value;

	public AsnObjectIdentifier(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		this.value = AsnObjectIdentifierUtils.parsePayload(contentBytes);

	}

	public AsnObjectIdentifier(String value) {
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

		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.OBJECT_IDENTIFIER);

		List<Integer> subIdentifiers = new ArrayList<>();
		String[] parts = value.split("\\.");
		for (int i = 0; i < parts.length; ++i) {
			subIdentifiers.add(Integer.valueOf(parts[i]));
		}
		int firstSubIdentifier = subIdentifiers.get(0) * 40 + subIdentifiers.get(1);

		List<Byte> contentBytes = new ArrayList<>();
		AsnObjectIdentifierUtils.encodeSubIdentifier(contentBytes, firstSubIdentifier);
		for (int i = 2; i < subIdentifiers.size(); ++i) {
			AsnObjectIdentifierUtils.encodeSubIdentifier(contentBytes, subIdentifiers.get(i));
		}

		AsnUtils.addLengthBytes(bytes, contentBytes.size());
		AsnUtils.addBytes(bytes, contentBytes);
	}
}
