package xdptdr.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

	public static <T> T convert(ObjectMapper om, InputStream content, Class<T> clazz, boolean dumpToStdout)
			throws JsonParseException, JsonMappingException, IOException {
		if (dumpToStdout) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(content, baos);
			byte[] bytes = baos.toByteArray();
			System.out.println(new String(bytes, Charset.forName("UTF-8")));
			return om.readValue(bytes, clazz);
		} else {
			return om.readValue(content, clazz);
		}
	}
	
}
