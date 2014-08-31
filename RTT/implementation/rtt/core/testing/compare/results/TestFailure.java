package rtt.core.testing.compare.results;


public class TestFailure implements ITestFailure {
	
	private String message = null;

	public TestFailure(String message) {
		if (message == null || message.equals("")) {
			throw new RuntimeException("Failure message was null or empty.");
		}
		
		this.message = message;
	}
	
	@Override
	public String getShortMessage() {
		return "Test case failed.";
	}

	@Override
	public String getMessage() {
		return "Test case failed: " + message;
	}	
}
