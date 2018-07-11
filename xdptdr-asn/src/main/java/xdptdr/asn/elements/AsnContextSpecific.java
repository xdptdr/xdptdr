package xdptdr.asn.elements;

import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnContextSpecific extends AsnElement {

	private long tag;
	private byte[] value;
	private AsnEncoding encoding = AsnEncoding.CONSTRUCTED;

	public AsnContextSpecific(long tag) {
		this.tag = tag;
	}

	public AsnContextSpecific(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		tag = AsnUtils.parseTag(identifierBytes);
		value = contentBytes.toByteArray();
	}

	public AsnContextSpecific(int tag, byte[] value) {
		this.value = value;
		this.tag = tag;
	}

	public AsnContextSpecific(int tag, byte[] value, AsnEncoding encoding) {
		this(tag,value);
		this.encoding = encoding;
	}

	public long getTag() {
		return tag;
	}

	public void setTag(long tag) {
		this.tag = tag;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.addIdentifierBytes(bytes, AsnClass.CONTEXT_SPECIFIC, encoding, tag);
		AsnUtils.addLengthBytes(bytes, value.length);
		AsnUtils.addBytes(bytes, value);
	}

	public AsnElement get() {
		return AsnUtils.parse(value);
	}

	public AsnSequence getSequence() {
		return (AsnSequence) get();
	}

	public AsnElement parse() {
		return AsnUtils.parse(value);
	}
}
