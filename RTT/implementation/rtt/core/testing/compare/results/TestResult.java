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
	Integer dataVersion;
	
	List<ITestFailure> failures;

	public TestResult(ResultType type, Integer dataVersion) {
		this.type = type;
		this.dataVersion = dataVersion;
		
		failures = new ArrayList<ITestFailure>();
	}
	
	public String getSuiteName() {
		return suiteName;
	}

	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public String getCaseName() {
		return caseName;
	}

	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}
	
	public Integer getDataVersion() {
		return dataVersion;
	}
	
	public ResultType getType() {
		return type;
	}
	
	public void addFailure(ITestFailure failure) {
		failures.add(failure);
	}

	public List<ITestFailure> getFailures() {
		return failures;
	}
}
