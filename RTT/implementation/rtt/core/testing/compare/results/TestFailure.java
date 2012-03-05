package rtt.core.testing.compare.results;


public abstract class TestFailure extends TestResult {

	public TestFailure(int refVersion, int testVersion) { // Testcase testCase, Testsuite testSuite) {
		super(ResultType.FAILURE, refVersion, testVersion); //, testCase, testSuite);
	}

	public abstract String getShortMessage();

	public abstract String getMessage();

	public abstract String getPath();

}