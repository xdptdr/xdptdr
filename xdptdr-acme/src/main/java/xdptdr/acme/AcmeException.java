package xdptdr.acme;

public class AcmeException extends Exception {

	private static final long serialVersionUID = 1L;

	public AcmeException(Exception ex) {
		super(ex);
	}

	public AcmeException(String message) {
		super(message);
	}

}
