package example.company.asn.elements;

import org.junit.Assert;
import org.junit.Test;

import xdptdr.asn.elements.AsnUtcTime;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;
import xdptdr.common.Common;

public class AsnUtcTimeTest {

	String value = "18/05/16 08:29:59 Z";
	byte[] encoded = Common.bytes(0x17, 0x0D, 0x31, 0x38, 0x30, 0x35, 0x31, 0x36, 0x30, 0x38, 0x32, 0x39, 0x35, 0x39,
			0x5A);

	@Test
	public void testDecode() {
		AsnUtcTime utcTime = (AsnUtcTime) AsnUtils.parse(new Bytes(encoded));
		Assert.assertEquals(value, utcTime.getValue());
	}

	@Test
	public void testEncode() {
		AsnUtcTime utcTime = new AsnUtcTime(value);
		byte[] actual = AsnUtils.encode(utcTime);
		Assert.assertArrayEquals(encoded, actual);
	}
}
