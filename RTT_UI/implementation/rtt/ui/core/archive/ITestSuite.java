package rtt.ui.core.archive;

import java.util.List;


public interface ITestSuite {
	
	String getName();
	
	List<ITestCase> getTestCases();
	ITestCase getTestCase(String caseName);
}
