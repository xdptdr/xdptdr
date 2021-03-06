package xdptdr.acme.v2.requests;

import java.util.HashMap;
import java.util.Map;

import xdptdr.acme.jw.JWBase64;
import xdptdr.acme.jw.JWSBuilder;
import xdptdr.acme.v2.AcmeSession;
import xdptdr.acme.v2.net.AcmeNetwork;
import xdptdr.acme.v2.net.AcmeResponse;

public class AcmeFinalize {

	public static AcmeResponse<Void> sendRequest(AcmeSession session, byte[] csrBytes) throws Exception {
		HashMap<Object, Object> payload = new HashMap<>();
		payload.put("csr", JWBase64.encode(csrBytes));
		String url = session.getOrder().getFinalize();
		Map<String, Object> jws = JWSBuilder.build(session, payload, url);

		byte[] body = session.getOm().writeValueAsBytes(jws);

		return AcmeNetwork.post(url, body, Void.class, session);
	}

}
