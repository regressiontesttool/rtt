package rtt.core.archive;

import java.io.File;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.Manager.TestCaseMode;
import rtt.core.manager.data.ConfigurationManager;
import rtt.core.manager.data.LogManager;
import rtt.core.manager.data.TestsuiteManager;
import rtt.core.manager.data.ConfigurationManager.ConfigStatus;
import rtt.core.manager.data.TestsuiteManager.TestcaseStatus;

public class Archive {

	protected ArchiveLoader loader;
	private ConfigurationManager configManager;
	private TestsuiteManager suiteManager;
	private LogManager logManager;
	private Configuration activeConfig;

	public Archive(ArchiveLoader loader) {
		this.loader = loader;

		configManager = new ConfigurationManager(loader);
		logManager = new LogManager(loader);
		suiteManager = new TestsuiteManager(loader);
	}

	public ArchiveLoader getLoader() {
		return loader;
	}

	public void load() throws Exception {
		this.loadConfigurations();
		this.loadTestsuites();
		this.loadLog();
	}

	private void loadConfigurations() throws Exception {
		configManager.load();

		activeConfig = configManager.getDefaultConfig();
	}

	private void loadLog() throws Exception {
		logManager.load();
	}

	private void loadTestsuites() throws Exception {
		suiteManager.load();
	}

	public void save() throws Exception {
		configManager.save();
		suiteManager.save();
		logManager.save();
	}

	public ConfigStatus addConfiguration(Configuration config, boolean overwrite) {
		ConfigStatus configSet = configManager.setConfiguration(config,
				overwrite);

		if (activeConfig == null && configSet != ConfigStatus.SKIPPED) {
			activeConfig = configManager.getDefaultConfig();
		}

		return configSet;
	}

	public Configuration getActiveConfiguration() {
		return activeConfig;
	}

	public Configuration getDefaultConfiguration() {
		return configManager.getDefaultConfig();
	}
	
	public boolean setActiveConfiguration(String configName) {
		Configuration config = configManager.getConfiguration(configName);

		if (config != null) {
			activeConfig = config;
			return true;
		}

		return false;
	}

	public boolean setDefaultConfiguration(String configName) {
		return configManager.setDefaultConfig(configName);
	}

	public Configuration getConfiguration(String configName) {
		return configManager.getConfiguration(configName);
	}

	public List<Configuration> getConfigurations() {
		return configManager.getConfigurations();
	}

	public boolean addTestsuite(String suiteName) {
		return suiteManager.addTestsuite(suiteName);
	}

	public boolean removeTestsuite(String testsuite) {
		return suiteManager.removeTestsuite(testsuite);
	}

	public Testsuite getTestsuite(String suiteName, boolean deleted) {
		return suiteManager.getTestsuite(suiteName, deleted);
	}

	public List<Testsuite> getTestsuites(boolean getDeleted) {
		return suiteManager.getTestsuites(getDeleted);
	}

	public TestcaseStatus addTestcase(String suiteName, File newTestcase,
			TestCaseMode mode) throws RTTException {
		try {
			return suiteManager.addTestcase(suiteName, newTestcase, mode);
		} catch (Exception e) {
			throw new RTTException(Type.ARCHIVE, "Could not add test case.", e);
		}
	}

	public boolean removeTestcase(String suiteName, String caseName) {
		return suiteManager.removeTestcase(suiteName, caseName);
	}

	public Testcase getTestcase(String suiteName, String caseName,
			boolean deleted) {
		return suiteManager.getTestcase(suiteName, caseName, deleted);
	}
	
	public List<Testcase> getTestcases(String suiteName, boolean deleted) {
		return suiteManager.getTestcases(suiteName, deleted);
	}

	public LogManager getLogManager() {
		return logManager;
	}

	public void print() {
		configManager.print();
		suiteManager.print();
	}

}
