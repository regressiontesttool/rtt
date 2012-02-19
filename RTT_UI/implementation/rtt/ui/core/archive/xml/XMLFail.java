package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.logging.Testrun;
import rtt.ui.core.archive.IFailedTestEntry;

public class XMLFail implements IFailedTestEntry {
	
	private String testName;
	private String suiteName;
	private String path;
	private String message;
	
//	Failed failed;

	public XMLFail() {//Failed entry) {
//		this.testName = entry.getTest();
//		this.suiteName = entry.getTestsuit();
//		this.path = entry.getPath();
//		this.message = entry.getMsg();
	}

	@Override
	public String getTestName() {
		return testName;
	}

	@Override
	public String getSuiteName() {
		return suiteName;
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public static List<IFailedTestEntry> getList(Testrun testrun) {
		List<IFailedTestEntry> list = new LinkedList<IFailedTestEntry>();
		
//		for (Failed entry : testrun.getFailed()) {
//			list.add(new XMLFail(entry));			
//		}
		
		return list;
	}

}
