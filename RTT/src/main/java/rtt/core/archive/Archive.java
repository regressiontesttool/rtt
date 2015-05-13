package rtt.core.archive;

import java.io.File;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.archive.testsuite.VersionData;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.Manager.TestCaseMode;
import rtt.core.manager.data.ConfigurationManager;
import rtt.core.manager.data.ConfigurationManager.ConfigStatus;
import rtt.core.manager.data.LogManager;
import rtt.core.manager.data.TestsuiteManager;
import rtt.core.manager.data.TestsuiteManager.TestcaseStatus;

/**
 * The {@code Archive} contains all relevant data of a physically archive from
 * the disk and provides an uniform access to all operations, which can be made on an
 * archive.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 * @see ConfigurationManager
 * @see TestsuiteManager
 * @see LogManager
 * 
 */
public class Archive {

	protected ArchiveLoader loader;
	private ConfigurationManager configManager;
	private TestsuiteManager suiteManager;
	private LogManager logManager;
	private Configuration activeConfig;

	/**
	 * Creates a new {@link Archive} with the given {@link ArchiveLoader}.
	 * 
	 * @param loader
	 *            the {@link ArchiveLoader}, which should be used to open the
	 *            archive
	 * @see ArchiveLoader
	 */
	public Archive(ArchiveLoader loader) {
		this.loader = loader;

		configManager = new ConfigurationManager(loader);
		logManager = new LogManager(loader);
		suiteManager = new TestsuiteManager(loader);
	}

	/**
	 * Returns the {@link ArchiveLoader} of this {@link Archive}
	 * 
	 * @return an {@link ArchiveLoader}
	 * @see ArchiveLoader
	 */
	public ArchiveLoader getLoader() {
		return loader;
	}

	/**
	 * Loads all data (configurations, test suites, log) from disk to the
	 * {@link Archive}.
	 * 
	 * @throws Exception
	 *             thrown, if any error occurred
	 * @see #loadConfigurations()
	 * @see #loadTestsuites()
	 * @see #loadLog()
	 */
	public void load() throws Exception {
		this.loadConfigurations();
		this.loadTestsuites();
		this.loadLog();
	}

	/**
	 * Loads all data from disk to the {@link ConfigurationManager} of this
	 * {@link Archive}. This function sets also the default configuration as
	 * current active configuration.
	 * 
	 * @throws Exception
	 *             thrown, if any error occurred
	 * @see Configuration
	 * @see ConfigurationManager#load()
	 * @see Archive#load()
	 */
	private void loadConfigurations() throws Exception {
		configManager.load();

		activeConfig = configManager.getDefaultConfig();
	}

	/**
	 * Loads all data from disk to the {@link LogManager} of this
	 * {@link Archive}.
	 * 
	 * @throws Exception
	 *             thrown, if any error occurred
	 * @see LogManager#load()
	 * @see Archive#load()
	 */
	private void loadLog() throws Exception {
		logManager.load();
	}

	/**
	 * Loads all data from disk to the {@link TestsuiteManager} of this
	 * {@link Archive}.
	 * 
	 * @throws Exception
	 *             thrown, if any error occurred
	 * @see TestsuiteManager#load()
	 * @see Archive#load()
	 */
	private void loadTestsuites() throws Exception {
		suiteManager.load();
	}

	/**
	 * Saves all data from this {@link Archive} to disk.
	 * 
	 * @throws Exception
	 *             thrown, if any error occurred
	 * @see ConfigurationManager#save()
	 * @see TestsuiteManager#save()
	 * @see LogManager#save()
	 */
	public void save() throws Exception {
		configManager.save();
		suiteManager.save();
		logManager.save();
	}

	/**
	 * Adds or overwrites a {@link Configuration} of this {@link Archive}.
	 * 
	 * @param config
	 *            the new or changed configuration
	 * @param overwrite
	 *            indicates, if an existing configuration should be overwritten
	 * @return {@link ConfigStatus}, indicating the actions which have been
	 *         taken
	 * @see ConfigurationManager#setConfiguration(Configuration, boolean)
	 */
	public ConfigStatus setConfiguration(Configuration config, boolean overwrite) {
		ConfigStatus configSet = configManager.setConfiguration(config,
				overwrite);

		if (activeConfig == null && configSet != ConfigStatus.SKIPPED) {
			activeConfig = configManager.getDefaultConfig();
		}

		return configSet;
	}
	
	/**
	 * Returns a {@link Configuration} or {@code null} for the given name.
	 * 
	 * @param configName
	 *            the name of the configuration
	 * @return {@link Configuration} or {@code null}
	 */
	public Configuration getConfiguration(String configName) {
		return configManager.getConfiguration(configName);
	}

	/**
	 * Returns the current active {@link Configuration} of this {@link Archive}.
	 * 
	 * @return the current active configuration
	 */
	public Configuration getActiveConfiguration() {
		return activeConfig;
	}

	/**
	 * Returns the default {@link Configuration} of this {@link Archive}
	 * 
	 * @return the default configuration
	 */
	public Configuration getDefaultConfiguration() {
		return configManager.getDefaultConfig();
	}

	/**
	 * Sets the active {@link Configuration} of this {@link Archive}.
	 * 
	 * @param configName
	 *            the name of the configuration, which should be the new active
	 *            configuration
	 * @return true, if the active configuration has been found and set
	 */
	public boolean setActiveConfiguration(String configName) {
		Configuration config = configManager.getConfiguration(configName);

		if (config != null) {
			activeConfig = config;
			return true;
		}

		return false;
	}

	/**
	 * Sets the default {@link Configuration} to this {@link Archive}.
	 * 
	 * @param configName
	 *            the name of the new default configuration
	 * @return true, if configuration has been set
	 */
	public boolean setDefaultConfiguration(String configName) {
		return configManager.setDefaultConfig(configName);
	}

	

	/**
	 * Returns all {@link Configuration}s of this {@link Archive}.
	 * 
	 * @return a {@link List} of configurations
	 */
	public List<Configuration> getConfigurations() {
		return configManager.getConfigurations();
	}

	/**
	 * Creates a new test suite within this {@link Archive}.
	 * 
	 * @param suiteName
	 *            the name of the new test suite
	 * @return true, if the test suite could be created
	 * @see TestsuiteManager#addTestsuite(String)
	 */
	public boolean addTestsuite(String suiteName) {
		return suiteManager.addTestsuite(suiteName);
	}

	/**
	 * Removes a test suite from this {@link Archive}.
	 * 
	 * @param testsuite
	 *            the name of the test suite, which should be removed
	 * @return true, if successfully removed
	 * @see TestsuiteManager#removeTestsuite(String)
	 */
	public boolean removeTestsuite(String testsuite) {
		return suiteManager.removeTestsuite(testsuite);
	}

	/**
	 * Returns a {@link Testsuite} or {@code null} for the given name.
	 * 
	 * @param suiteName
	 *            the name of the test suite
	 * @param deleted
	 *            indicates, if a removed test suite can be returned
	 * @return a {@link Testsuite} or {@code null}
	 * @see TestsuiteManager#getTestsuite(String, boolean)
	 */
	public Testsuite getTestsuite(String suiteName, boolean deleted) {
		return suiteManager.getTestsuite(suiteName, deleted);
	}
	
	/**
	 * Returns a {@link Testsuite} or {@code null} for the given name.
	 * <p>
	 * Notice, that removed test suites will not be searched. If needed, use {@link #getTestsuite(String, boolean)}
	 * 
	 * @param suiteName
	 *            the name of the test suite
	 * @return a {@link Testsuite} or {@code null}
	 * @see #getTestsuite(String, boolean)
	 */
	public Testsuite getTestsuite(String suiteName) {
		return getTestsuite(suiteName, false);
	}

	/**
	 * Returns all {@link Testsuite}s of this {@link Archive}.
	 * 
	 * @param getDeleted
	 *            indicates, if removed test suites should be contained
	 * @return a {@link List} of test suites
	 * @see TestsuiteManager#getTestsuites(boolean)
	 */
	public List<Testsuite> getTestsuites(boolean getDeleted) {
		return suiteManager.getTestsuites(getDeleted);
	}
	
	/**
	 * Returns all {@link Testsuite}s of this {@link Archive}.
	 * <p>
	 * Notice, that removed test suites will not be searched. If needed, use {@link #getTestsuites(boolean)}
	 * @return a {@link List} of test suites
	 * @see #getTestsuites(boolean)
	 */
	public List<Testsuite> getTestsuites() {
		return suiteManager.getTestsuites(false);
	}
	
	/**
	 * Returns true, if the given test suite is existing in this {@link Archive}.
	 * @param suiteName the name of the test suite
	 * @param deleted indicates, if removed test suites should be searched
	 * @return true, if existing
	 */
	public boolean hasTestsuite(String suiteName, boolean deleted) {
		return suiteManager.getTestsuite(suiteName, deleted) != null;
	}
	
	/**
	 * Returns true, if the given test suite is existing in this {@link Archive}.
	 * <p>
	 * Notice, that removed test suites will not be searched. If needed, use {@link #hasTestsuite(String, boolean)}  
	 * @param suiteName the name of the test suite
	 * @return true, if existing
	 * @see #hasTestsuite(String, boolean)
	 */
	public boolean hasTestsuite(String suiteName) {
		return hasTestsuite(suiteName, false);
	}

	/**
	 * Adds a test case to this {@link Archive}, with the given
	 * {@link TestCaseMode}.
	 * 
	 * @param suiteName
	 *            the name of the test suite, where the test case should be
	 *            added.
	 * @param newTestcase
	 *            a file containing the content (input data) of the test case
	 * @param mode
	 *            the {@link TestCaseMode}
	 * @return {@link TestcaseStatus}, indicating the actions which have been
	 *         taken
	 * @throws RTTException
	 *             thrown, if any error occurred
	 * @see TestsuiteManager#addTestcase(String, File, TestCaseMode);
	 * @see TestCaseMode
	 */
	public TestcaseStatus addTestcase(String suiteName, File newTestcase,
			TestCaseMode mode) throws RTTException {
		try {
			return suiteManager.addTestcase(suiteName, newTestcase, mode);
		} catch (Exception e) {
			throw new RTTException(Type.OPERATION_FAILED, "Could not add test case.", e);
		}
	}

	/**
	 * Removes a test case from this {@link Archive}.
	 * 
	 * @param suiteName
	 *            the name of the containing test suite
	 * @param caseName
	 *            the name of the test case
	 * @return true, if successfully removed
	 */
	public boolean removeTestcase(String suiteName, String caseName) {
		return suiteManager.removeTestcase(suiteName, caseName);
	}

	/**
	 * Returns a {@link Testcase} or {@code null} for the given name.
	 * 
	 * @param suiteName
	 *            the name of the test suite
	 * @param caseName
	 *            the name of the test case
	 * @param deleted
	 *            indicates, if a removed test case can be returned
	 * @return {@link Testcase} or {@code null}
	 * @see Testcase
	 */
	public Testcase getTestcase(String suiteName, String caseName,
			boolean deleted) {
		return suiteManager.getTestcase(suiteName, caseName, deleted);
	}
	
	/**
	 * Returns a {@link Testcase} or {@code null} for the given name.
	 * <p>
	 * Notice, that removed test cases will not be searched. If needed, use {@link #getTestcase(String, String, boolean)}
	 *
	 * @param suiteName
	 *            the name of the test suite
	 * @param caseName
	 *            the name of the test case
	 * @return {@link Testcase} or {@code null}
	 * @see Testcase
	 * @see #getTestcase(String, String, boolean)
	 */
	public Testcase getTestcase(String suiteName, String caseName) {
		return getTestcase(suiteName, caseName, false);
	}	
	
	/**
	 * Returns true, if the given test case is existing in this {@link Archive}.
	 *   
	 * @param suiteName the name of the test suite
	 * @param caseName the name of the test case
	 * @param deleted indicates, if removed test cases should be searched
	 * @return true, if existing
	 * @see #hasTestcase(String, String, boolean)
	 */	
	public boolean hasTestcase(String suiteName, String caseName, boolean deleted) {
		return suiteManager.getTestcase(suiteName, caseName, deleted) != null;
	}
	
	/**
	 * Returns true, if the given test case is existing in this {@link Archive}.
	 * <p>
	 * Notice, that removed test suites will not be searched. If needed, use {@link #hasTestcase(String, String, boolean)}
	 *   
	 * @param suiteName the name of the test suite
	 * @param caseName the name of the test case
	 * @return true, if existing
	 * @see #hasTestcase(String, String, boolean)
	 */
	public boolean hasTestcase(String suiteName, String caseName) {
		return hasTestcase(suiteName, caseName, false);
	}

	/**
	 * Returns all test cases from a given test suite of this {@link Archive}.
	 * 
	 * @param suiteName
	 *            the name of the test suite
	 * @param deleted
	 *            indicates, if removed test cases should be contained
	 * @return {@link List} of test cases
	 * @see Testcase
	 */
	public List<Testcase> getTestcases(String suiteName, boolean deleted) {
		return suiteManager.getTestcases(suiteName, deleted);
	}
	
	/**
	 * Returns all test cases from a given test suite of this {@link Archive}.
	 * <p>
	 * Notice, that removed test cases will not be searched. If needed, use {@link #getTestcases(String, boolean)}
	 * 
	 * @param suiteName
	 *            the name of the test suite
	 * @return {@link List} of test cases
	 * @see Testcase
	 * @see Archive#getTestcases(String, boolean)
	 */
	public List<Testcase> getTestcases(String suiteName) {
		return suiteManager.getTestcases(suiteName, false);
	}

	/**
	 * Returns the {@link LogManager} of this {@link Archive}.
	 * 
	 * @return the {@link LogManager}
	 */
	public LogManager getLogManager() {
		return logManager;
	}

	/**
	 * Prints all informations of configurations and test suites to the console.
	 * 
	 * @see ConfigurationManager#print()
	 * @see TestsuiteManager#print()
	 */
	public void print() {
		configManager.print();
		suiteManager.print();
	}

	public void close() {
		configManager = null;
		suiteManager = null;
		logManager = null;

		loader.close();
	}

	public VersionData getVersionData(Testcase tcase, String configName, boolean create) {
		if (tcase == null || configName == null || configName.isEmpty()) {
			throw new IllegalArgumentException("The given test case was null or the config name was null or empty");
		}
		
		List<VersionData> versionList = tcase.getVersionData();
		
		for (VersionData versionData : versionList) {
			if (versionData.getConfig().equals(configName)) {
				return versionData;
			}
		}
		
		if (create) {
			VersionData versionData = new VersionData();
			versionData.setConfig(configName);
			
			versionList.add(versionData);
			return versionData;
		}		
		
		return null;
	}

}
