package xdptdr.asn.elements;

import java.util.List;

import xdptdr.asn.elements.AsnElement;

public class AsnRaw extends AsnElement {

	private byte[] raw;

	public AsnRaw(byte[] raw) {
		this.raw = raw;
	}

	@Override
	public void encode(List<Byte> bytes) {
		for (int i = 0; i < raw.length; ++i) {
			bytes.add(raw[i]);
		}
	}

}
