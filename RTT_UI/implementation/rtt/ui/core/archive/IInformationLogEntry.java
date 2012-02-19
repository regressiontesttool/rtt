package rtt.ui.core.archive;

import java.util.List;


public interface IInformationLogEntry extends IDatedLogEntry {
	
	List<ITestInformation> getTestInformations();
	String getMessage();
	String getSuffix();

}
