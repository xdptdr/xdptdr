package xdptdr.asn.elements;

import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import xdptdr.asn.AsnClass;
import xdptdr.asn.AsnEncoding;
import xdptdr.asn.AsnTag;
import xdptdr.asn.utils.AsnUtils;
import xdptdr.common.Bytes;

public class AsnUtcTime extends AsnElement {

	private String value;

	public AsnUtcTime() {
	}

	public AsnUtcTime(String value) {
		this.value = value;
	}

	public AsnUtcTime(Bytes identifierBytes, Bytes lengthBytes, Bytes contentBytes) {

		String rawvalue = new String(contentBytes.toByteArray(), Charset.forName("UTF-8"));

		StringWriter sb = new StringWriter();

		int c = 0;
		sb.append(rawvalue.substring(c, c + 2));
		c += 2;
		sb.append("/");

		sb.append(rawvalue.substring(c, c + 2));
		c += 2;
		sb.append("/");

		sb.append(rawvalue.substring(c, c + 2));
		c += 2;
		sb.append(" ");

		sb.append(rawvalue.substring(c, c + 2));
		c += 2;
		sb.append(":");

		sb.append(rawvalue.substring(c, c + 2));
		c += 2;
		sb.append(":");

		sb.append(rawvalue.substring(c, c + 2));
		c += 2;
		sb.append(" ");

		sb.append(rawvalue.substring(c, c + 1));

		value = sb.toString();

	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void encode(List<Byte> bytes) {
		AsnUtils.addIdentifierBytes(bytes, AsnClass.UNIVERSAL, AsnEncoding.PRIMITIVE, AsnTag.UTC_TIME);
		byte[] encoded = value.replaceAll("[ :/]", "").getBytes();
		AsnUtils.addLengthBytes(bytes, encoded.length);
		AsnUtils.addBytes(bytes, encoded);
	}

	public Date toDate() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		String[] parts = value.split("[ :/]");
		cal.setTimeInMillis(0);
		Integer year = Integer.valueOf(parts[0]);
		cal.set(Calendar.YEAR, 2000 + year);
		int month = Integer.valueOf(parts[1]) - 1;
		cal.set(Calendar.MONTH, month);
		Integer day = Integer.valueOf(parts[2]);
		cal.set(Calendar.DAY_OF_MONTH, day);
		Integer hour = Integer.valueOf(parts[3]);
		cal.set(Calendar.HOUR_OF_DAY, hour);
		Integer minute = Integer.valueOf(parts[4]);
		cal.set(Calendar.MINUTE, minute);
		Integer second = Integer.valueOf(parts[5]);
		cal.set(Calendar.SECOND, second);
		Date time = cal.getTime();
		return time;
	}

	public static String toString(Date date) {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		cal.setTimeInMillis(date.getTime());
		int y = cal.get(Calendar.YEAR);
		y = y % 100;
		int m = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DAY_OF_MONTH);
		int hh = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);

		StringWriter sw = new StringWriter();
		if (y < 10) {
			sw.append("0");
		}
		sw.append(Integer.toString(y));
		sw.append("/");
		if (m < 10) {
			sw.append("0");
		}
		sw.append(Integer.toString(m));
		sw.append("/");
		if (d < 10) {
			sw.append("0");
		}
		sw.append(Integer.toString(d));
		sw.append(" ");
		if (hh < 10) {
			sw.append("0");
		}
		sw.append(Integer.toString(hh));
		sw.append(":");
		if (mm < 10) {
			sw.append("0");
		}
		sw.append(Integer.toString(mm));
		sw.append(":");
		if (ss < 10) {
			sw.append("0");
		}
		sw.append(Integer.toString(ss));
		sw.append(" Z");
		return sw.toString();
	}

}
