package xdptdr.acme.v2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.databind.ObjectMapper;

import xdptdr.acme.v2.account.AcmeError;

public class AcmeNetwork {

	private static <T> AcmeResponse<T> parseResponse(HttpResponse response, Class<T> clazz, ObjectMapper om,
			AcmeSession session) throws IOException {

		int code = response.getStatusLine().getStatusCode();
		InputStream is = response.getEntity().getContent();

		AcmeResponse<T> r = new AcmeResponse<T>();

		setNonce(r, response, session);

		if (code >= 200 && code < 300) {
			if (code != 204) {
				try {

					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					IOUtils.copy(is, baos);
					byte[] bytes = baos.toByteArray();
					r.setResponseText(new String(bytes, Charset.forName("UTF-8")));
					if (!Void.class.equals(clazz)) {
						T content = om.readValue(bytes, clazz);
						r.setContent(content);
					}
				} catch (Exception ex) {
					r.setFailed(true);
					r.setFailureDetails(ex.getClass().getName() + " : " + ex.getMessage());
				}
			}
		} else {
			r.setFailed(true);
			try {
				AcmeError error = om.readValue(is, AcmeError.class);
				r.setFailureDetails(error.getDetail());
			} catch (Exception ex) {
				r.setFailureDetails(ex.getClass().getName() + " : " + ex.getMessage());
			}
		}

		return r;
	}

	public static <T> AcmeResponse<T> post(String url, byte[] body, Class<T> clazz, AcmeSession session)
			throws ClientProtocolException, IOException {

		ObjectMapper om = session.getOm();

		Request request = Request.Post(url)

				.setHeader("Content-Type", "application/jose+json")

				.bodyByteArray(body);

		ResponseHandler<AcmeResponse<T>> responseHandler = new ResponseHandler<AcmeResponse<T>>() {

			@Override
			public AcmeResponse<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return AcmeNetwork.parseResponse(response, clazz, om, session);
			}

		};

		return request.execute().handleResponse(responseHandler);
	}

	private static AcmeResponse<Boolean> parseResponse(HttpResponse response, ObjectMapper om, AcmeSession session)
			throws IOException {

		int code = response.getStatusLine().getStatusCode();
		InputStream is = response.getEntity().getContent();

		AcmeResponse<Boolean> r = new AcmeResponse<Boolean>();
		r.setContent(false);

		setNonce(r, response, session);

		if (code >= 200 && code < 300) {
			r.setContent(true);
		} else {
			r.setFailed(true);
			try {
				AcmeError error = om.readValue(is, AcmeError.class);
				r.setFailureDetails(error.getDetail());
			} catch (Exception ex) {
				r.setFailureDetails(ex.getClass().getName() + " : " + ex.getMessage());
			}
		}

		return r;
	}

	public static AcmeResponse<Boolean> post(String url, byte[] body, ObjectMapper om, AcmeSession session)
			throws Exception {

		Request request = Request.Post(url)

				.setHeader("Content-Type", "application/jose+json")

				.bodyByteArray(body);

		ResponseHandler<AcmeResponse<Boolean>> responseHandler = new ResponseHandler<AcmeResponse<Boolean>>() {

			@Override
			public AcmeResponse<Boolean> handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				return AcmeNetwork.parseResponse(response, om, session);
			}

		};

		return request.execute().handleResponse(responseHandler);
	}

	public static <T> AcmeResponse<T> get(String url, Class<T> clazz, AcmeSession session)
			throws ClientProtocolException, IOException {

		ObjectMapper om = session.getOm();

		Request request = Request.Get(url).setHeader("Accept", "application/json");

		ResponseHandler<AcmeResponse<T>> responseHandler = new ResponseHandler<AcmeResponse<T>>() {
			@Override
			public AcmeResponse<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {

				InputStream responseContent = response.getEntity().getContent();

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				IOUtils.copy(responseContent, baos);
				byte[] bytes = baos.toByteArray();

				AcmeResponse<T> r = new AcmeResponse<>();
				r.setContent(om.readValue(bytes, clazz));
				r.setResponseText(new String(bytes, Charset.forName("UTF-8")));
				return r;
			}

		};

		return request.execute().handleResponse(responseHandler);

	}

	public static AcmeResponse<String> nonce(AcmeSession session) throws ClientProtocolException, IOException {

		String url = session.getInfos().getNewNonce();

		Request request = Request.Head(url);

		ResponseHandler<AcmeResponse<String>> responseHandler = new ResponseHandler<AcmeResponse<String>>() {
			@Override
			public AcmeResponse<String> handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				AcmeResponse<String> r = new AcmeResponse<>();
				setNonce(r, response, session);
				r.setContent(session.getNonce());
				return r;
			}

		};

		return request.execute().handleResponse(responseHandler);
	}

	private static <T> void setNonce(AcmeResponse<T> r, HttpResponse response, AcmeSession session) {
		Header fh = response.getFirstHeader("Replay-Nonce");
		if (fh != null) {
			String nonce = fh.getValue();
			session.setNonce(nonce, false);
		}
	}

}
