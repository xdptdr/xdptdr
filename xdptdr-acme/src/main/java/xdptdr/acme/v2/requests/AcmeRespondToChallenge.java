package xdptdr.acme.v2.requests;

import java.util.HashMap;
import java.util.Map;

import xdptdr.acme.jw.JWSBuilder;
import xdptdr.acme.v2.AcmeSession;
import xdptdr.acme.v2.model.AcmeChallenge;
import xdptdr.acme.v2.net.AcmeNetwork;
import xdptdr.acme.v2.net.AcmeResponse;

public class AcmeRespondToChallenge {

	public static AcmeResponse<Void> sendRequest(AcmeSession session, AcmeChallenge challenge) throws Exception {
		HashMap<Object, Object> payload = new HashMap<>();
		String url = challenge.getUrl();
		Map<String, Object> jws = JWSBuilder.build(session, payload, url);

		byte[] body = session.getOm().writeValueAsBytes(jws);

		return AcmeNetwork.post(url, body, Void.class, session);
	}

}
