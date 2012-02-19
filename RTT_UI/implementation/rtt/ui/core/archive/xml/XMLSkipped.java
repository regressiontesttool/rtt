package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.logging.Testrun;
import rtt.ui.core.archive.ITestEntry;

public class XMLSkipped implements ITestEntry {
	
	private String testName;
	private String suiteName;

	public XMLSkipped(){//Skipped entry) {
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
		
//		for (Skipped entry : testrun.getSkipped()) {
//			list.add(new XMLSkipped(entry));			
//		}
		
		return list;
	}

}
