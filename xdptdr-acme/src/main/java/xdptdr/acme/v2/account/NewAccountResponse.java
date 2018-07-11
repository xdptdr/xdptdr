package xdptdr.acme.v2.account;

public class NewAccountResponse {

	private String nonce;
	private String location;

	public NewAccountResponse(String nonce, String location) {
		this.nonce = nonce;
		this.location = location;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
