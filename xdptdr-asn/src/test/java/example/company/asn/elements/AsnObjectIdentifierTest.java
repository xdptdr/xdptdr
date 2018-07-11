package example.company.asn.elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import xdptdr.asn.elements.AsnObjectIdentifier;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;
import xdptdr.common.Common;

public class AsnObjectIdentifierTest {

	private static class SpecCase {
		public String value;
		public byte[] encoded;

		public SpecCase(String value, byte[] encoded) {
			this.value = value;
			this.encoded = encoded;
		}

	}

	static List<SpecCase> specCases = new ArrayList<>();
	static {
		specCases.add(new SpecCase("2.100.3", Common.bytes(0x06, 0x03, 0x81, 0x34, 0x03)));
		specCases.add(new SpecCase("1.2.840.113549.1.1.1",
				Common.bytes(0x06, 0x09, 0x2A, 0x86, 0x48, 0x86, 0xF7, 0x0D, 0x01, 0x01, 0x01)));
	}

	@Test
	public void testDecode() {
		specCases.forEach(tc -> {
			AsnObjectIdentifier oid = (AsnObjectIdentifier) AsnUtils.parse(new Bytes(tc.encoded));
			Assert.assertEquals(tc.value, oid.getValue());
		});
	}

	@Test
	public void testEncode() {
		specCases.forEach(tc -> {
			AsnObjectIdentifier oid = new AsnObjectIdentifier(tc.value);
			byte[] actual = AsnUtils.encode(oid);
			System.out.println(tc.value);
			System.out.println(Common.bytesToString(tc.encoded));
			System.out.println(Common.bytesToString(actual));
			Assert.assertArrayEquals(tc.encoded, actual);
		});

	}

}
