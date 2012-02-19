package rtt.ui.core.archive;

import java.util.List;




public interface IArchive {
	
	String getName();
	
	List<IConfiguration> getConfigurations();
	IConfiguration getConfiguration(String name);
	IConfiguration getDefaultConfiguration();
	
	List<ITestSuite> getTestSuites();
	ITestSuite getTestSuite(String name);
	
	ILog getArchiveLog();
}
