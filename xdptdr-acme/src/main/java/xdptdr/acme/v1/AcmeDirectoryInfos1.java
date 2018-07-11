package xdptdr.acme.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AcmeDirectoryInfos1 {

	private String keyChange;
	private String newAuthz;
	private String newCert;
	private String newReg;
	private String revokeCert;

	@JsonProperty("key-change")
	public String getKeyChange() {
		return keyChange;
	}

	public void setKeyChange(String keyChange) {
		this.keyChange = keyChange;
	}

	@JsonProperty("new-authz")
	public String getNewAuthz() {
		return newAuthz;
	}

	public void setNewAuthz(String newAuthz) {
		this.newAuthz = newAuthz;
	}

	@JsonProperty("new-cert")
	public String getNewCert() {
		return newCert;
	}

	public void setNewCert(String newCert) {
		this.newCert = newCert;
	}

	@JsonProperty("new-reg")
	public String getNewReg() {
		return newReg;
	}

	public void setNewReg(String newReg) {
		this.newReg = newReg;
	}

	@JsonProperty("revoke-cert")
	public String getRevokeCert() {
		return revokeCert;
	}

	public void setRevokeCert(String revokeCert) {
		this.revokeCert = revokeCert;
	}

	
}
