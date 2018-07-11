package xdptdr.acme.v2;

import java.util.HashMap;
import java.util.Map;

import xdptdr.acme.AcmeException;
import xdptdr.acme.jw.JWSBuilder;

public class AcmeDeactivateAccount {

	private static Map<String, Object> createJWS(AcmeSession session, String url) throws Exception {

		Map<String, String> payload = new HashMap<>();
		payload.put("status", "deactivated");

		return JWSBuilder.build(session, payload, url);

	}

	public static AcmeResponse<Boolean> sendRequest(AcmeSession session) throws Exception {

		String url = session.getAccount().getUrl();

		Map<String, Object> jws;
		try {
			jws = createJWS(session, url);
		} catch (Exception ex) {
			throw new AcmeException(ex);
		}

		byte[] body = session.getOm().writeValueAsBytes(jws);

		return AcmeNetwork.post(url, body, session.getOm(), session);
	}

}
