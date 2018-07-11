package xdptdr.common;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class Common {

	public static byte bit(int integer) {
		return (byte) integer;
	}

	public static byte bit(long l) {
		return (byte) l;
	}

	public static byte[] bytes(int... integers) {
		byte[] bytes = new byte[integers.length];
		for (int i = 0; i < integers.length; ++i) {
			bytes[i] = (byte) integers[i];
		}
		return bytes;
	}

	public static String tos(Object o) {
		if (o == null) {
			return "n$";
		} else if (o instanceof String) {
			return "s$" + o.toString();
		} else {
			return "c$" + o.getClass().getName();
		}
	}

	public static String bytesToString(byte[] value) {
		return new HexBinaryAdapter().marshal(value);
	}

	public static String booleansToString(boolean[] value) {
		StringWriter sw = new StringWriter();
		for (int i = 0; i < value.length; ++i) {
			sw.append(value[i] ? "1" : "0");
		}
		return sw.toString();
	}

	public static String dateToString(Date value) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(value.getTime());
		return DatatypeConverter.printDateTime(cal);
	}

	public static byte[] toArray(List<Byte> bytes) {
		byte[] array = new byte[bytes.size()];
		for (int i = 0; i < bytes.size(); ++i) {
			array[i] = bytes.get(i);
		}
		return array;
	}

	public static byte[] bytes(String s) {
		return new HexBinaryAdapter().unmarshal(s);
	}

	public static void disableHCLogging() {
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
	}

	public static byte[] concatenate(byte[]... arrays) {
		int len = 0;
		for (byte[] array : arrays) {
			len += array.length;
		}
		byte[] output = new byte[len];

		int offset = 0;
		for (byte[] array : arrays) {
			for (int i = 0; i < array.length; ++i) {
				output[i + offset] = array[i];
			}
			offset += array.length;
		}
		return output;

	}

	public static byte[] first(byte[] bytes, int num) {

		byte[] result = new byte[num];

		for (int i = 0; i < num; ++i) {
			result[i] = bytes[i];
		}

		return result;
	}

	public static byte[] last(byte[] bytes, int num) {

		byte[] result = new byte[num];

		for (int i = num - 1; i >= 0; --i) {
			result[i] = bytes[bytes.length - i - 1];
		}

		return result;
	}

	public static BigInteger bigInteger(byte[] bytes) {
		BigInteger bigInteger = BigInteger.ZERO;
		for (int i = 0; i < bytes.length; ++i) {
			bigInteger = bigInteger.shiftLeft(8).add(BigInteger.valueOf(bytes[i]&0xFF));
		}
		return bigInteger;
	}
	
	public static byte[] bigIntegerToBytes(BigInteger big) {

		List<Byte> rbytes = new ArrayList<>();

		BigInteger ff = BigInteger.valueOf(0xFF);
		while (big.compareTo(BigInteger.ZERO) == 1) {
			rbytes.add(big.and(ff).byteValue());
			big = big.shiftRight(8);
		}

		byte[] bytes = new byte[rbytes.size()];
		for (int i = 0; i < rbytes.size(); ++i) {
			bytes[i] = rbytes.get(rbytes.size() - 1 - i);
		}

		return bytes;

	}

	public static byte[] part(byte[] input, int start, int end) {
		byte[] output = new byte[end - start];
		for (int i = start; i < end; ++i) {
			output[i - start] = input[i];
		}
		return output;
	}
}
