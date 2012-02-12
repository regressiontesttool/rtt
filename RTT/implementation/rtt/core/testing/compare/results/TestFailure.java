package rtt.core.testing.compare.results;


public abstract class TestFailure extends TestResult {

	public TestFailure() { // Testcase testCase, Testsuite testSuite) {
		super(ResultType.FAILURE, 0); //, testCase, testSuite);
	}
	
	public void setDataVersion(Integer version) {
		this.dataVersion = version;
	}

	public abstract String getShortMessage();

	public abstract String getMessage();

	public abstract String getPath();

}