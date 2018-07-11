package xdptdr.acme.v2.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewAccountRequest {

	private String protekted;
	private String payload;
	private String signature;

	@JsonProperty("protected")
	public String getProtected() {
		return protekted;
	}

	public void setProtected(String protekted) {
		this.protekted = protekted;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
