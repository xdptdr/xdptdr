package xdptdr.acme.jw;

import java.util.HashMap;
import java.util.Map;

public class JWK {

	Map<String, String> jwk = new HashMap<>();

	public JWK() {
	}

	public JWK(Map<String, String> props) {
		this.jwk = props;
	}

	public Map<String, String> getJwk() {
		return jwk;
	}

	public void setJwk(Map<String, String> jwk) {
		this.jwk = jwk;
	}

}
