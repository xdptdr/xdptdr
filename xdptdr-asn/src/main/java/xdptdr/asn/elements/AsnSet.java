package xdptdr.asn.elements;

import java.util.ArrayList;
import java.util.List;

import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnSet extends AsnElement {

	List<AsnElement> elements = new ArrayList<>();

	public AsnSet() {
	}

	public AsnSet(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {

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
		AsnUtils.encodeSequenceOrSet(bytes, AsnTag.SET, elements);
	}

	private AsnElement get(int i) {
		return elements.get(i);
	}

	public AsnSequence getSequence(int i) {
		return (AsnSequence) get(i);
	}

	public static AsnSet of(AsnElement... elements) {
		AsnSet set = new AsnSet();
		for (AsnElement element : elements) {
			set.getElements().add(element);
		}
		return set;
	}

	public AsnOctetString getOctetString(int i) {
		return (AsnOctetString) get(i);
	}

}
