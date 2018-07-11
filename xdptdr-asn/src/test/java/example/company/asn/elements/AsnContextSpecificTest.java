package example.company.asn.elements;

import org.junit.Assert;
import org.junit.Test;

import xdptdr.asn.elements.AsnContextSpecific;
import xdptdr.asn.elements.AsnNull;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;
import xdptdr.common.Common;

public class AsnContextSpecificTest {

	private byte[] encoded = Common.bytes(0xA3, 0x02, 0x05, 0x00);

	@Test
	public void testEncode() {
		AsnContextSpecific contextSpecific = new AsnContextSpecific(3);
		contextSpecific.setValue(new AsnNull().encode());

		byte[] actual = AsnUtils.encode(contextSpecific);
		Assert.assertArrayEquals(encoded, actual);
	}

	@Test
	public void testDecode() {
		AsnContextSpecific acs = (AsnContextSpecific) AsnUtils.parse(new Bytes(encoded));
		Assert.assertEquals(3, acs.getTag());
		Assert.assertTrue(acs.get() instanceof AsnNull);
	}

}
