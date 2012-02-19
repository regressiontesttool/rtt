package rtt.ui.core.archive;

public interface IFailedLogEntry {
	
	String getTestName();
	String getPath();
	String getMessage();
	String getTestsuiteName();
}
