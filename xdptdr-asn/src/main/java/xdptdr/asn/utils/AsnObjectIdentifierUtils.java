package xdptdr.asn.utils;

import java.util.ArrayList;
import java.util.List;

import xdptdr.common.Bytes;

public class AsnObjectIdentifierUtils {

	public static String parsePayload(Bytes payload) {

		List<Long> values = new ArrayList<>();

		long value = 0;
		for (int i = 0; i < payload.length(); ++i) {
			byte b = payload.at(i);
			value = (value << 7) + (b & 0x7F);
			if ((b & 0x80) != 0x80) {
				values.add(value);
				value = 0;
			}
		}

		StringBuilder b = new StringBuilder();
		boolean first = true;
		for (Long v : values) {
			String p = "";
			if (first) {
				if (v <= 39) {
					p = "0";
				} else if (v <= 79) {
					p = "1";
					v -= 40;
				} else {
					p = "2";
					v -= 80;
				}
			}
			b.append(p);
			b.append(".");
			b.append(v);
			first = false;
		}

		return b.toString();

	}

	public static void encodeSubIdentifier(List<Byte> bytes, int value) {
		List<Integer> parts = new ArrayList<>();
		while (value > 0x7F) {
			parts.add((value & 0x7F));
			value >>= 7;
		}
		parts.add(value);
		for (int i = parts.size() - 1; i >= 0; --i) {
			bytes.add((byte) (parts.get(i) + (i == 0 ? 0 : 0x80)));
		}
	}
}
