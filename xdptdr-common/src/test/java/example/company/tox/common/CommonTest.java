package example.company.tox.common;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import xdptdr.common.Common;

public class CommonTest {

	@Test
	public void test128() {
		BigInteger big = BigInteger.valueOf(128);
		Assert.assertArrayEquals(big.toByteArray(), Common.bigInteger(Common.bigIntegerToBytes(big)).toByteArray());
	}

	@Test
	public void testBig() {

		byte[] expected = Common.bytes(
				"0081E63E32108F094B5277A1EA0A73C3D0D2A34A0D40AC84E49E58E298E39F910967AFFF4D590A2F09630AA6268F385690FD302502998D968381024322BCD061211435C33072BD904FF235F951CC15FF5B135D67519E9825C154EA95043EE7ACAC5F5241D030010963FC51F8E7F92D6DEEBCD618401377DEF66AB056D2E4D2B7545398665F6C603AE483BE3300E354E69D3E36D714839CF868476B0F7DF33A9BFF403C224A88DC6EF35E1FAA2FDC3A32CF6E9D5B196ED5B885809EBE8FA800625EF00A319258CC1B6CBB359676ABD8057C9318B1C0AEA7C6C18BAF2E3802CCB70A10FBF46CC1C066A3D4D06E0FFD8BE64EE5936F3A31B9C02582A6C555FBFB93FF");
		BigInteger big = new BigInteger(expected);

		Assert.assertArrayEquals(expected, Common.bigInteger(Common.bigIntegerToBytes(big)).toByteArray());

	}

}
