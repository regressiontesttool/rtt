package rtt.core.testing.compare.results;

public class TestExecutionFailure implements ITestFailure {

	private Throwable throwable;
	
	public TestExecutionFailure(Throwable throwable) {
		this.throwable = throwable;
	}

	@Override
	public String getShortMessage() {
		return "Error during test execution.";
	}

	@Override
	public String getMessage() {
		return "Error during test execution: " + throwable.getMessage();
	}
}
