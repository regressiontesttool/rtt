package rtt.ui.core;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;

import rtt.ui.core.archive.IArchive;
import rtt.ui.core.archive.IConfiguration;
import rtt.ui.core.archive.ILog;
import rtt.ui.core.archive.ITestSuite;

public interface IRttProject {
	
	String getName();
	void reload();
	
	File getArchiveFile();
	IProject getWorkspaceProject();
	
	IArchive getArchive();
	ILog getLog();
	
	void setActiveConfiguration(IConfiguration activeConfig);
	
	List<IConfiguration> getConfigurations();
	List<ITestSuite> getTestSuites();

}
