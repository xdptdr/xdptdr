package xdptdr.asn.elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnSequence extends AsnElement implements Iterable<AsnElement> {

	List<AsnElement> elements = new ArrayList<>();

	public AsnSequence() {
	}

	public AsnSequence(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {

		int totalLength = identifierBytes.length() + lengthBytes.length() + contentBytes.length();

		Bytes nextElementBytes = contentBytes;
		while (true) {
			if (nextElementBytes.length() == 0) {
				break;
			}
			Bytes elementIdentifierBytes = AsnUtils.getIdentifierBytes(nextElementBytes);
			Bytes elementLengthBytes = AsnUtils.getLengthBytes(nextElementBytes.offset(elementIdentifierBytes));
			int elementLength = AsnUtils.parseLengthBytes(elementLengthBytes);
			Bytes elementContentBytes = nextElementBytes
					.subStartLen(elementIdentifierBytes.length() + elementLengthBytes.length(), elementLength);
			AsnElement element = AsnUtils.parse(elementIdentifierBytes, elementLengthBytes, elementContentBytes);
			if (element == null) {
				break;
			}
			elements.add(element);

			int elementSize = elementIdentifierBytes.length() + elementLengthBytes.length()
					+ elementContentBytes.length();
			if (elementSize >= totalLength) {
				break;
			} else {
				nextElementBytes = nextElementBytes.offset(elementSize);
			}
		}
	}

	public List<AsnElement> getElements() {
		return elements;
	}

	public void setElements(List<AsnElement> elements) {
		this.elements = elements;
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.encodeSequenceOrSet(bytes, AsnTag.SEQUENCE, elements);
	}

	public AsnElement get(int i) {
		return elements.get(i);
	}

	public AsnContextSpecific getContextSpecific(int i) {
		return (AsnContextSpecific) get(i);
	}

	@Override
	public Iterator<AsnElement> iterator() {
		return elements.iterator();
	}

	public AsnObjectIdentifier getObjectIdentifier(int i) {
		return (AsnObjectIdentifier) get(i);
	}

	public AsnOctetString getOctetString(int i) {
		return (AsnOctetString) get(i);
	}

	public AsnBoolean getBoolean(int i) {
		return (AsnBoolean) get(0);
	}

	public AsnSequence getSequence(int i) {
		return (AsnSequence) get(i);
	}

	public AsnInteger getInteger(int i) {
		return (AsnInteger) get(i);
	}

	public AsnBitString getBitString(int i) {
		return (AsnBitString) get(i);
	}

	public AsnSet getSet(int i) {
		return (AsnSet) get(i);
	}

	public static AsnSequence of(AsnElement... elements) {
		AsnSequence seq = new AsnSequence();
		for (AsnElement element : elements) {
			seq.getElements().add(element);
		}
		return seq;
	}
}
