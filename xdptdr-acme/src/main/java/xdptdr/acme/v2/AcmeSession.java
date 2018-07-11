package xdptdr.acme.v2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import xdptdr.acme.jw.KeyPairWithJWK;
import xdptdr.acme.v2.account.AcmeAccount;

public class AcmeSession {

	private ObjectMapper om = new ObjectMapper();

	private String version;
	private String url;
	private AcmeDirectoryInfos2 infos;
	protected String nonce;
	private AcmeAccount account;
	private AcmeOrder order;
	private AcmeAuthorization authorization;
	private AcmeChallenge challenge;
	private KeyPairWithJWK keyPairWithJWK;

	public AcmeSession() {
		om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public AcmeDirectoryInfos2 getInfos() {
		return infos;
	}

	public void setInfos(AcmeDirectoryInfos2 infos) {
		this.infos = infos;
	}

	public ObjectMapper getOm() {
		return om;
	}

	public void setOm(ObjectMapper om) {
		this.om = om;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce, boolean p) {
		this.nonce = nonce;
	}

	public AcmeAccount getAccount() {
		return account;
	}

	public void setAccount(AcmeAccount account) {
		this.account = account;
	}

	public AcmeOrder getOrder() {
		return order;
	}

	public void setOrder(AcmeOrder order) {
		this.order = order;
	}

	public AcmeAuthorization getAuthorization() {
		return authorization;
	}

	public void setAuthorization(AcmeAuthorization authorization) {
		this.authorization = authorization;
	}

	public AcmeChallenge getChallenge() {
		return challenge;
	}

	public void setChallenge(AcmeChallenge challenge) {
		this.challenge = challenge;
	}

	public KeyPairWithJWK getKeyPairWithJWK() {
		return keyPairWithJWK;
	}

	public void setKeyPairWithJWK(KeyPairWithJWK keyPairWithJWK) {
		this.keyPairWithJWK = keyPairWithJWK;
	}
}
