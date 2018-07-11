package xdptdr.asn.elements;

import java.util.ArrayList;
import java.util.List;

import xdptdr.common.exceptions.NotSupportedException;

public class AsnElement {

	@SuppressWarnings("unchecked")
	public <T> T as(Class<T> clazz) {
		return (T) this;
	}

	public AsnSequence asSequence() {
		return (AsnSequence) this;
	}

	public AsnContextSpecific asContextSpecific() {
		return (AsnContextSpecific) this;
	}

	public AsnObjectIdentifier asObjectIdentifier() {
		return (AsnObjectIdentifier) this;
	}

	public AsnBitString asBitString() {
		return (AsnBitString) this;
	}

	public AsnOctetString asOctetString() {
		return (AsnOctetString) this;
	}

	public void encode(List<Byte> bytes) {
		throw new NotSupportedException(this.getClass().getName());
	}

	public final byte[] encode() {
		List<Byte> bytes = new ArrayList<>();
		encode(bytes);
		byte[] b = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); ++i) {
			b[i] = bytes.get(i);
		}
		return b;
	}
}
