package example.company.asn.elements;

import org.junit.Assert;
import org.junit.Test;

import xdptdr.asn.elements.AsnNull;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Common;

public class AsnNullEncodingTest {

	@Test
	public void test() {
		AsnNull asnNull = new AsnNull();
		byte[] actual = AsnUtils.encode(asnNull);
		byte[] expected = Common.bytes(0x5, 0x0);
		Assert.assertArrayEquals(expected, actual);
	}

}
