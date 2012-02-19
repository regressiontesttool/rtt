package rtt.ui.core.archive;

import java.util.List;

public interface IArchiveLog {
	
	List<IInformationLogEntry> getInformationLogEntries();
	List<ITestrunLogEntry> getTestrunLogEntries();
	List<IFailedLogEntry> getFailedLogEntries();
}
