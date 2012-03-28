package rtt.core.testing.compare.results;

import java.util.ArrayList;
import java.util.List;

public class TestResult {

	public enum ResultType {
		SUCCESS, SKIPPED, FAILURE;
	}

	ResultType type;
	String suiteName;
	String caseName;
	
	Integer refVersion;
	Integer testVersion;
	
	List<ITestFailure> failures;
	
	public TestResult(ResultType type, String suiteName, String caseName) {
		this.type = type;
		this.suiteName = suiteName;
		this.caseName = caseName;
		
		this.refVersion = 0;
		this.testVersion = 0;
		
		failures = new ArrayList<ITestFailure>();
	}
	
	public String getSuiteName() {
		return suiteName;
	}

	public String getCaseName() {
		return caseName;
	}

	public Integer getRefVersion() {
		return refVersion;
	}
	
	public void setRefVersion(Integer refVersion) {
		this.refVersion = refVersion;
	}
	
	public Integer getTestVersion() {
		return testVersion;
	}
	
	public void setTestVersion(Integer testVersion) {
		this.testVersion = testVersion;
	}
	
	public ResultType getType() {
		return type;
	}
	
	public void setType(ResultType type) {
		this.type = type;
	}
	
	public void addFailure(ITestFailure failure) {
		failures.add(failure);
	}

	public List<ITestFailure> getFailures() {
		return failures;
	}
}
