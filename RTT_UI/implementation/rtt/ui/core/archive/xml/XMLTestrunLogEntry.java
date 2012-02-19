package rtt.ui.core.archive.xml;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import rtt.core.archive.logging.Testrun;
import rtt.ui.core.archive.IFailedTestEntry;
import rtt.ui.core.archive.ITestEntry;
import rtt.ui.core.archive.ITestrunLogEntry;

public class XMLTestrunLogEntry implements ITestrunLogEntry {
	
	private XMLGregorianCalendar date;
	private String configName;
	
	private List<ITestEntry> skippedTests;
	private List<ITestEntry> passedTests;
	private List<IFailedTestEntry> failedTests;
	
	public XMLTestrunLogEntry(Testrun testrun) {
		this.date = testrun.getDate();
		this.configName = testrun.getConfiguration();
		
		this.skippedTests = XMLSkipped.getList(testrun);
		this.passedTests = XMLPassed.getList(testrun);
		this.failedTests = XMLFail.getList(testrun);
	}

	@Override
	public XMLGregorianCalendar getDate() {
		return date;
	}

	@Override
	public String getConfigurationName() {
		return configName;
	}

	@Override
	public List<ITestEntry> getPassedTests() {
		return passedTests;
	}
	
	@Override
	public List<ITestEntry> getSkippedTests() {
		return skippedTests;
	}

	@Override
	public List<IFailedTestEntry> getFailedTests() {
		return failedTests;
		
	}	
}
