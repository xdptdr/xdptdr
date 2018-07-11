package example.company.asn.elements;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import xdptdr.asn.elements.AsnInteger;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;
import xdptdr.common.Common;

public class AsnIntegerTest {

	private static class SpecCase {
		public int value;
		public byte[] encoded;

		public SpecCase(int value, byte[] encoded) {
			this.value = value;
			this.encoded = encoded;
		}

	}

	static List<SpecCase> specCases = new ArrayList<>();
	static {
		specCases.add(new SpecCase(0, Common.bytes("020100")));
		specCases.add(new SpecCase(0x7F, Common.bytes("02017F")));
		specCases.add(new SpecCase(0x80, Common.bytes("02020080")));
		specCases.add(new SpecCase(0x100, Common.bytes("02020100")));
		specCases.add(new SpecCase(-128, Common.bytes("020180")));
		specCases.add(new SpecCase(-129, Common.bytes("0202FF7F")));
		specCases.add(new SpecCase(50000, Common.bytes("020300C350")));
	}

	@Test
	public void encodingTest() {
		specCases.forEach(tc -> {
			AsnInteger integer = new AsnInteger(tc.value);
			byte[] actual = AsnUtils.encode(integer);
			System.out.println(Common.bytesToString(tc.encoded));
			System.out.println(Common.bytesToString(actual));
			Assert.assertArrayEquals(tc.encoded, actual);
		});
	}

	@Test
	public void decodingTest() {
		specCases.forEach(tc -> {
			AsnInteger actual = (AsnInteger) AsnUtils.parse(new Bytes(tc.encoded));
			Assert.assertEquals(tc.value, actual.getValue().longValue());
		});
	}
}
