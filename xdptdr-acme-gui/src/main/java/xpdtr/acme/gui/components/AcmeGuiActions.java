package xpdtr.acme.gui.components;

public enum AcmeGuiActions {

	CREATE_KEY_PAIR("Create new key pair", 1),

	LOAD_KEY_PAIR("Load key pair", 1),

	SAVE_KEY_PAIR("Save key pair", 1),

	NONCE("New Nonce", 1),

	CREATE_ACCOUNT("Create Account", 1),

	ACCOUNT_DETAILS("Get Account Details", 1),

	DEACTIVATE_ACCOUNT("Deactivate Account", 1),

	ORDER("Create Order", 1),

	AUTHORIZATION_DETAILS("Get authorization", 1),

	CHALLENGE_DETAIL("Select challenge", 2),

	RESPOND_CHALLENGE("Respond to challenge", 2),

	CREATE_CERT_KEY("Create cert key", 2),

	FINALIZE("Finalize", 2),

	RETRIEVE_CERT("Retrive cert", 2),

	SAVE_CERT_PRIVATE_KEY("Save cert private key", 2),

	CHANGE_KEY("Change Key", 2),

	REVOKE_CERT("Revoke Cert", 2),

	FOO("Foo", 2);

	private String label;
	private int row;

	private AcmeGuiActions(String label, int row) {
		this.label = label;
		this.row = row;
	}

	public String getLabel() {
		return label;
	}

	public int getRow() {
		return row;
	}

}
