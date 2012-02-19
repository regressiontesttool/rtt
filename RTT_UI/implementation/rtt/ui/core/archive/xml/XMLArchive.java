package rtt.ui.core.archive.xml;

import java.io.File;
import java.util.List;

import rtt.core.archive.Archive;
import rtt.core.manager.Manager;
import rtt.ui.core.archive.IArchive;
import rtt.ui.core.archive.IConfiguration;
import rtt.ui.core.archive.ILog;
import rtt.ui.core.archive.ITestSuite;

public class XMLArchive implements IArchive {	

	private String name;
	private Archive archive;
	
	private List<IConfiguration> configurations;
	private List<ITestSuite> testSuites;
	private ILog log;
	
//	private ArchiveLog archiveLog;	
//	private Testarchive testArchive;
	
//	private File archiveFile;
	
	public XMLArchive(File archiveFile, String config) throws Exception {
		Manager m = new Manager(archiveFile, true);
		m.loadArchive(config);
		
		this.archive = m.getArchive();
		
		configurations = XMLConfiguration.getList(archive);		
		testSuites = XMLTestSuite.getList(archive);
		log = new XMLArchiveLog(archive.getLogManager().getData());
		
		name = archiveFile.getName();
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public ILog getArchiveLog() {
		return log;
	}	
	
	@Override
	public List<IConfiguration> getConfigurations() {
		return configurations;
	}
	
	@Override
	public IConfiguration getDefaultConfiguration() {
		for (IConfiguration config : getConfigurations()) {
			if (config.isDefault()) {
				return config;
			}			
		}
		
		return null;
	}

	@Override
	public IConfiguration getConfiguration(String name) {
		for (IConfiguration config : getConfigurations()) {
			if (config.getName().equals(name)) {
				return config;
			}
		}
		
		return null;
	}

	@Override
	public List<ITestSuite> getTestSuites() {
		return testSuites;
	}	

	@Override
	public ITestSuite getTestSuite(String name) {
		for (ITestSuite suite : getTestSuites()) {
			if (suite.getName().equals(name)) {
				return suite;
			}
		}
		
		return null;
	}

}
