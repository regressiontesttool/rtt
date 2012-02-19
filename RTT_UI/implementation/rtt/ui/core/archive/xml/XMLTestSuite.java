package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.Archive;
import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.core.archive.ITestCase;
import rtt.ui.core.archive.ITestSuite;

public class XMLTestSuite implements ITestSuite {
	
	String name;
	List<ITestCase> testCases;

	public XMLTestSuite(Testsuite testsuite) {
		this.name = testsuite.getName();
		this.testCases = XMLTestCase.getList(testsuite);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<ITestCase> getTestCases() {
		return testCases;
	}

	@Override
	public ITestCase getTestCase(String caseName) {
		for (ITestCase testcase : getTestCases()) {
			if (testcase.getName().equals(caseName)) {
				return testcase;
			}			
		}
		
		return null;
	}

	public static List<ITestSuite> getList(Archive archive) {
		List<ITestSuite> list = new LinkedList<ITestSuite>();	
		
		for (Testsuite testsuiteRef : archive.getTestsuites(false)) {
			list.add(new XMLTestSuite(testsuiteRef));
		}
		
		return list;
	}

}
