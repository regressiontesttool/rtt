package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.logging.Testrun;
import rtt.ui.core.archive.ITestEntry;

public class XMLPassed implements ITestEntry {
	
	private String testName;
	private String suiteName;

	public XMLPassed() { //Passed entry) {
//		this.testName = entry.getTest();
//		this.suiteName = entry.getTestSuite();		
	}

	@Override
	public String getTestName() {
		return testName;
	}

	@Override
	public String getSuiteName() {
		return suiteName;
	}

	public static List<ITestEntry> getList(Testrun testrun) {
		List<ITestEntry> list = new LinkedList<ITestEntry>();
//		
//		for (Passed entry : testrun.getPassed()) {
//			list.add(new XMLPassed(entry));			
//		}
		
		return list;
	}

}
