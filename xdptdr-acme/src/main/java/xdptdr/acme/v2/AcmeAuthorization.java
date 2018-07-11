package xdptdr.acme.v2;

import java.util.ArrayList;
import java.util.List;

import xdptdr.acme.v2.AcmeChallenge;
import xdptdr.acme.v2.AcmeIdentifier;

public class AcmeAuthorization {

	private AcmeIdentifier identifier;
	private String status;
	private String expires;
	private List<AcmeChallenge> challenges = new ArrayList<>();

	public AcmeIdentifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(AcmeIdentifier identifier) {
		this.identifier = identifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public List<AcmeChallenge> getChallenges() {
		return challenges;
	}

	public void setChallenges(List<AcmeChallenge> challenges) {
		this.challenges = challenges;
	}

}
