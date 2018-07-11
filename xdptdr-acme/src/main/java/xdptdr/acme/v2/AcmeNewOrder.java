package xdptdr.acme.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import xdptdr.acme.jw.JWSBuilder;
import xdptdr.acme.v2.AcmeDirectoryInfos2;
import xdptdr.acme.v2.AcmeOrder;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;

public class AcmeNewOrder {

	public static Map<String, Object> createJWS(AcmeSession session, String site) throws Exception {

		AcmeDirectoryInfos2 infos = session.getInfos();

		Map<String, String> identifier = new HashMap<>();
		identifier.put("type", "dns");
		identifier.put("value", site);

		List<Map<String, String>> identifiers = new ArrayList<>();
		identifiers.add(identifier);

		Map<String, Object> payload = new HashMap<>();
		payload.put("identifiers", identifiers);

		return JWSBuilder.build(session, payload, infos.getNewOrder());

	}

	public static AcmeResponse<AcmeOrder> sendRequest(AcmeSession session, String site) throws Exception {

		Map<String, Object> jws = AcmeNewOrder.createJWS(session, site);

		AcmeDirectoryInfos2 infos = session.getInfos();
		ObjectMapper om = session.getOm();

		String url = infos.getNewOrder();
		byte[] body = om.writeValueAsBytes(jws);

		return AcmeNetwork.post(url, body, AcmeOrder.class, session);

	}

}
