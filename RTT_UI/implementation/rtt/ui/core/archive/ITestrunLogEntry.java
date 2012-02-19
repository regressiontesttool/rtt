package rtt.ui.core.archive;

import java.util.List;

public interface ITestrunLogEntry extends IDatedLogEntry {
	
	String getConfigurationName();
	List<ITestEntry> getPassedTests();
	List<IFailedTestEntry> getFailedTests();
	List<ITestEntry> getSkippedTests();
}
