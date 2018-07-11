package xdptdr.asn.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import xdptdr.asn.OIDS;
import xdptdr.asn.elements.AsnObjectIdentifier;
import xdptdr.asn.elements.AsnPrintableString;
import xdptdr.asn.elements.AsnSequence;
import xdptdr.asn.elements.AsnSet;

public class AsnInterpretationUtils {
	public static String mapToNameString(AsnSequence nameSeq) {

		Map<String, String> parts = new HashMap<>();

		nameSeq.getElements().forEach(issuerSetElement -> {
			AsnSequence seq = issuerSetElement.as(AsnSet.class).getElements().get(0).as(AsnSequence.class);
			String oid = seq.getElements().get(0).as(AsnObjectIdentifier.class).getValue();
			String value = seq.getElements().get(1).as(AsnPrintableString.class).getValue();
			parts.put(oid, value);
		});

		String c = parts.get(OIDS.COUNTRY_NAME);
		String st = parts.get(OIDS.STATE_OR_PROVINCE_NAME);
		String l = parts.get(OIDS.LOCALITY_NAME);
		String o = parts.get(OIDS.ORGANIZATION_NAME);
		String ou = parts.get(OIDS.ORGANIZATIONAL_UNIT_NAME);
		String cncommonName = parts.get(OIDS.COMMON_NAME);

		return "CN=" + cncommonName + ", OU=" + ou + ", O=" + o + ", L=" + l + ", ST=" + st + ", C=" + c;

	}

	public static AsnKeyInfo mapToKeyInfo(AsnSequence foo) {
		String algorithmOID = foo.getSequence(0).getObjectIdentifier(0).getValue();
		ArrayList<Byte> byteList = new ArrayList<Byte>();
		foo.encode(byteList);
		byte[] bytes = new byte[byteList.size()];
		for (int i = 0; i < byteList.size(); ++i) {
			bytes[i] = byteList.get(i);
		}
		String algorithmName = OIDS.getLabel(algorithmOID);
		return new AsnKeyInfo(algorithmOID, algorithmName , bytes);
	}
}
