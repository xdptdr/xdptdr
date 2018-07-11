package xdptdr.asn.utils;

import java.util.HashMap;
import java.util.Map;

import xdptdr.asn.Asn;
import xdptdr.asn.OIDS;
import xdptdr.asn.elements.AsnSequence;

public class DistinguishedNameUtils {

	private static final String[] keys = new String[] { "C", "ST", "L", "O", "OU", "CN" };
	private static final Map<String, String> oids = new HashMap<>();
	static {
		oids.put("C", OIDS.COUNTRY_NAME);
		oids.put("ST", OIDS.STATE_OR_PROVINCE_NAME);
		oids.put("L", OIDS.LOCALITY_NAME);
		oids.put("O", OIDS.ORGANIZATION_NAME);
		oids.put("OU", OIDS.ORGANIZATIONAL_UNIT_NAME);
		oids.put("CN", OIDS.COMMON_NAME);
	}

	public static Map<String, String> parse(String name) {
		Map<String, String> map = new HashMap<>();
		String[] nameParts = name.split(", ");
		for (int i = 0; i < nameParts.length; ++i) {
			String[] kvParts = nameParts[i].split("=");
			map.put(kvParts[0], kvParts[1]);
		}
		return map;
	}

	public static AsnSequence toSequence(Map<String, String> map) {

		AsnSequence seq = new AsnSequence();

		for (String key : keys) {
			if (map.containsKey(key)) {
				seq.getElements().add(Asn.set(Asn.seq(Asn.oid(oids.get(key)), Asn.str(map.get(key)))));
			}
		}

		return seq;
	}

}
