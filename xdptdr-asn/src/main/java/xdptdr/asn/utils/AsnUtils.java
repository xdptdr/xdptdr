package xdptdr.asn.utils;

import java.util.ArrayList;
import java.util.List;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.elements.AsnBMPString;
import xdptdr.asn.elements.AsnBitString;
import xdptdr.asn.elements.AsnBoolean;
import xdptdr.asn.elements.AsnContextSpecific;
import xdptdr.asn.elements.AsnElement;
import xdptdr.asn.elements.AsnIA5String;
import xdptdr.asn.elements.AsnInteger;
import xdptdr.asn.elements.AsnNull;
import xdptdr.asn.elements.AsnObjectIdentifier;
import xdptdr.asn.elements.AsnOctetString;
import xdptdr.asn.elements.AsnPrintableString;
import xdptdr.asn.elements.AsnSequence;
import xdptdr.asn.elements.AsnSet;
import xdptdr.asn.elements.AsnUtcTime;
import xdptdr.asn.elements.ext.AsnSubjectIdentifier;
import xdptdr.common.Bytes;
import xdptdr.common.Common;

public class AsnUtils {

	public static Bytes getIdentifierBytes(Bytes bytes) {
		int tag = bytes.at(0) & 0x1F;
		int count = 1;
		if (tag == 0x1F) {
			while ((bytes.at(count) & 0x80) == 0x80) {
				++count;
			}
		}
		return new Bytes(bytes, 0, count);
	}

	public static Bytes getLengthBytes(Bytes bytes) {
		int size = 1;
		boolean highByteSet = (bytes.at(0) & 0x80) == 0x80;
		if (highByteSet) {
			size = 1 + bytes.at(0) & 0x7F;
		}
		return new Bytes(bytes, 0, size);
	}

	public static long parseTag(Bytes idBytes) {
		if (idBytes.length() == 1) {
			return idBytes.at(0) & 0x1F;
		} else {
			long tag = 0;
			for (int i = 1; i < idBytes.length(); ++i) {
				tag = (tag << 8) + (idBytes.at(i) & 0x7F);

			}
			return tag;
		}
	}

	public static int parseLengthBytes(Bytes lBytes) {
		if (lBytes.length() == 1) {
			return lBytes.at(0) & 0x7F;
		} else {
			long length = 0;
			for (int i = 1; i < lBytes.length(); ++i) {
				length = (length << 8) + (lBytes.at(i) & 0xFF);
			}
			return (int) length;
		}
	}

	public static AsnEncoding parseEncoding(Bytes idBytes) {
		return (idBytes.at(0) & 0x20) == 20 ? AsnEncoding.CONSTRUCTED : AsnEncoding.PRIMITIVE;
	}

	public static AsnClass parseClass(Bytes identifierBytes) {
		return AsnClass.fromByte(identifierBytes.at(0));
	}

	public static Bytes getContentBytes(Bytes identifierBytes, Bytes lengthBytes, Bytes allBytes) {
		long length = parseLengthBytes(lengthBytes);
		if (length == 0) {
			throw new IllegalArgumentException("Elements with indefinite length not supproted");
		}
		int headerSize = identifierBytes.length() + lengthBytes.length();
		return new Bytes(allBytes, headerSize, (int) length);
	}

	public static AsnElement parse(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {
		AsnClass asnClass = parseClass(identifierBytes);
		long tag = parseTag(identifierBytes);
		if (asnClass == AsnClass.UNIVERSAL) {
			if (tag == AsnTag.SEQUENCE.getTagNumber()) {
				return new AsnSequence(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.INTEGER.getTagNumber()) {
				return new AsnInteger(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.OBJECT_IDENTIFIER.getTagNumber()) {
				return new AsnObjectIdentifier(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.NULL.getTagNumber()) {
				return new AsnNull(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.SET.getTagNumber()) {
				return new AsnSet(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.PRINTABLE_STRING.getTagNumber()) {
				return new AsnPrintableString(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.UTC_TIME.getTagNumber()) {
				return new AsnUtcTime(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.BIT_STRING.getTagNumber()) {
				return new AsnBitString(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.OCTET_STRING.getTagNumber()) {
				return new AsnOctetString(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.BOOLEAN.getTagNumber()) {
				return new AsnBoolean(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.IA5STRING.getTagNumber()) {
				return new AsnIA5String(identifierBytes, lengthBytes, contentBytes);
			} else if (tag == AsnTag.BMP_STRING.getTagNumber()) {
				return new AsnBMPString(identifierBytes, lengthBytes, contentBytes);
			} else {
				System.out.println("Warning : unknown " + asnClass + " tag " + tag);
				return null;
			}
		} else if (asnClass == AsnClass.CONTEXT_SPECIFIC) {
			return new AsnContextSpecific(identifierBytes, lengthBytes, contentBytes);
		} else if (asnClass == AsnClass.APPLICATION) {
			if (tag == AsnTag.SUBJECT_IDENTIFIER.getTagNumber()) {
				return new AsnSubjectIdentifier(identifierBytes, lengthBytes, contentBytes);
			}
		} else {
			System.out.println("Warning : unknown " + asnClass + " tag " + tag);
		}
		return null;
	}

	public static AsnElement parse(Bytes bytes) {
		Bytes identifierBytes = getIdentifierBytes(bytes);
		Bytes lengthBytes = getLengthBytes(bytes.offset(identifierBytes));
		int length = parseLengthBytes(lengthBytes);
		Bytes contentBytes = bytes.subStartLen(identifierBytes.length() + lengthBytes.length(), length);
		return parse(identifierBytes, lengthBytes, contentBytes);

	}

	public static byte[] encode(AsnElement element) {
		List<Byte> bytes = new ArrayList<>();
		element.encode(bytes);
		return Common.toArray(bytes);
	}

	public static void addIdentifierBytes(List<Byte> bytes, AsnClass asnClass, AsnEncoding asnEncoding, AsnTag asnTag) {
		addIdentifierBytes(bytes, asnClass, asnEncoding, asnTag.getTagNumber());
	}

	public static void addIdentifierBytes(List<Byte> bytes, AsnClass asnClass, AsnEncoding asnEncoding,
			long tagNumber) {
		bytes.add(Common.bit(asnClass.getMasked() + asnEncoding.getMasked() + tagNumber));
	}

	public static void addLengthBytes(List<Byte> bytes, long length) {
		if (length < 0x80) {
			bytes.add(Common.bit(length));
		} else {
			List<Long> parts = new ArrayList<>();
			while (length > 0xFF) {
				parts.add(length & 0xFF);
				length >>= 8;
			}
			parts.add(length);
			bytes.add((byte) (parts.size() | 0x80));
			for (int i = parts.size() - 1; i >= 0; --i) {
				bytes.add((byte) (parts.get(i).intValue()));
			}
		}
	}

	public static void addBytes(List<Byte> list, byte[] array) {
		for (int i = 0; i < array.length; ++i) {
			list.add(array[i]);
		}
	}

	public static void addBytes(List<Byte> bytes, List<Byte> payloadBytes) {
		bytes.addAll(payloadBytes);
	}

	public static void encodeSequenceOrSet(List<Byte> bytes, AsnTag tag, List<AsnElement> elements) {

		List<byte[]> encodedElements = new ArrayList<>();
		long[] encodedElementsSize = new long[] { 0 };

		elements.forEach(element -> {
			byte[] elementBytes = AsnUtils.encode(element);
			encodedElements.add(elementBytes);
			encodedElementsSize[0] += elementBytes.length;
		});

		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.CONSTRUCTED, tag);
		AsnUtils.addLengthBytes(bytes, encodedElementsSize[0]);
		encodedElements.forEach(elementBytes -> {
			AsnUtils.addBytes(bytes, elementBytes);
		});

	}

	public static AsnElement parse(byte[] bytes) {
		return parse(new Bytes(bytes));
	}

}
