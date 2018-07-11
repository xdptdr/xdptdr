package xdptdr.acme.v2;

public class AcmeResponse<T> {

	private boolean failed;
	private T content;
	private String failureDetails;
	private String responseText;

	public AcmeResponse() {
	}

	public AcmeResponse(T content) {
		this.content = content;
	}

	public AcmeResponse(Exception e) {
		failed = true;
		failureDetails = e.getClass().getName() + " : " + e.getMessage();
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public boolean isFailed() {
		return failed;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}

	public String getFailureDetails() {
		return failureDetails;
	}

	public void setFailureDetails(String failureDetails) {
		this.failureDetails = failureDetails;
	}

	public String getResponseText() {
		return responseText;
	}

	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}

}