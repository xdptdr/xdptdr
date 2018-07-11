package xdptdr.acme.v2.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xdptdr.acme.jw.JWSBuilder;
import xdptdr.acme.v2.AcmeDirectoryInfos2;
import xdptdr.acme.v2.AcmeNetwork;
import xdptdr.acme.v2.AcmeResponse;
import xdptdr.acme.v2.AcmeSession;
import xdptdr.acme.v2.account.AcmeAccount;

public class AcmeNewAccount {

	public static Map<String, Object> createJWS(AcmeSession session, String contact) throws Exception {

		String url = session.getInfos().getNewAccountURL();

		List<String> contacts = new ArrayList<>();
		String prefix = "";
		if (!contact.startsWith("mailto:")) {
			prefix = "mailto:";
		}
		contacts.add(prefix + contact);

		Map<String, Object> payload = new HashMap<>();
		payload.put("termsOfServiceAgreed", true);
		payload.put("contact", contacts);

		return JWSBuilder.build(session, payload, url);
	}

	public static AcmeResponse<AcmeAccount> sendRequest(AcmeSession session, String contact) throws Exception {

		Map<String, Object> jws = createJWS(session, contact);

		String url = session.getInfos().getNewAccountURL();
		byte[] body = session.getOm().writeValueAsBytes(jws);

		AcmeResponse<AcmeAccount> post = AcmeNetwork.post(url, body, AcmeAccount.class, session);
		if (post.getContent() != null) {
			post.getContent().setUrl(getUrl(session.getInfos(), post.getContent()));
		}
		return post;

	}

	private static String getUrl(AcmeDirectoryInfos2 infos, AcmeAccount account) {

		String url = infos.getNewAccountURL();
		int lastSlash = url.lastIndexOf('/');

		return url.substring(0, lastSlash) + "/acct/" + account.getId();
	}
}
