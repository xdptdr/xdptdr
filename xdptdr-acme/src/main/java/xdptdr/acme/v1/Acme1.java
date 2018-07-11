package xdptdr.acme.v1;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;

import com.fasterxml.jackson.databind.ObjectMapper;

import xdptdr.common.JsonUtils;

public class Acme1 {

	public static final String ACME_STAGING_V1 = "https://acme-staging.api.letsencrypt.org";
	private static final String DIRECTORY = "/directory";

	public static AcmeDirectoryInfos1 directory(ObjectMapper om) throws ClientProtocolException, IOException {
		AcmeDirectoryInfos1 response = Request.Get(ACME_STAGING_V1 + "/" + DIRECTORY)
				.setHeader("Accept", "application/json").execute()
				.handleResponse(new ResponseHandler<AcmeDirectoryInfos1>() {

					@Override
					public AcmeDirectoryInfos1 handleResponse(HttpResponse response)
							throws ClientProtocolException, IOException {
						return JsonUtils.convert(om, response.getEntity().getContent(), AcmeDirectoryInfos1.class,
								false);
					}

				});
		return response;
	}
}
