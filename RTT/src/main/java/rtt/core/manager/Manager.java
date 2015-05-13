/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.core.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.Archive;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.logging.Detail;
import rtt.core.archive.logging.EntryType;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.archive.testsuite.VersionData;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.ConfigurationManager.ConfigStatus;
import rtt.core.manager.data.LogManager;
import rtt.core.manager.data.TestsuiteManager;
import rtt.core.manager.data.TestsuiteManager.TestcaseStatus;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.core.testing.Tester;
import rtt.core.testing.compare.results.ITestFailure;
import rtt.core.testing.compare.results.TestResult;
import rtt.core.testing.compare.results.TestResult.ResultType;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.Executor;
import rtt.core.utils.GenerationInformation;
import rtt.core.utils.GenerationInformation.GenerationResult;
import rtt.core.utils.GenerationInformation.GenerationType;
import rtt.core.utils.RTTLogging;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class Manager {

	/**
	 * This modes specify, which action should take for adding test cases.
	 * @author Christian Oelsner <C.Oelsner@gmail.com>
	 * @see #SKIP
	 * @see #IGNORE
	 * @see #RENAME
	 * @see #OVERWRITE
	 */
	public enum TestCaseMode {
		/**
		 * throw exception, if file already exists
		 */
		SKIP,
		
		/**
		 * ignore double files
		 */
		IGNORE, 
		
		/**
		 * rename new files
		 */
		RENAME,
		
		/**
		 * overwrite old file
		 */
		OVERWRITE
		
	}

	File archivePath;
	Archive currentArchive;
	LogManager currentLog;
	private String baseDir;
	
	private ArchiveLoader loader;

	public static boolean verbose = true;

	public Manager(File archivePath, boolean verbose) throws RTTException {
		this.archivePath = archivePath;
		this.loader = ArchiveLoader.create(archivePath);

		// because sometimes, it is not the same
		Thread.currentThread().setContextClassLoader(
				this.getClass().getClassLoader());

		Manager.verbose = verbose;
	}
	
	public Manager(File archive, boolean verbose, ClassLoader classLoader) throws RTTException {
		this(archive, verbose);
		Thread.currentThread().setContextClassLoader(classLoader);
	}
	
	private void initArchive(File archive) {
		loader.setBasePath(archive);
		baseDir = loader.getBasePath();
		
		currentArchive = new Archive(loader);
		currentLog = currentArchive.getLogManager();
	}
	
	public void loadArchive(File archive) throws RTTException {
		if (!archive.exists()) {
			throw new RTTException(Type.NO_ARCHIVE, archive + " not found.");
		}
		
		initArchive(archive);

		try {
			currentArchive.load();
		} catch (Exception e) {
			throw new RTTException(Type.NO_ARCHIVE, "Could not load archive.", e);
		}
	}
	
	public void loadArchive(File archive, String config) throws RTTException {
		loadArchive(archive);
		boolean hasChanged = currentArchive.setActiveConfiguration(config);

		if (hasChanged) {
			RTTLogging.info("Active configuration: " + config);
		}
	}

	public void createArchive(File archive) throws Exception {
		File parentFolder = archive.getParentFile();
		if (!parentFolder.exists()) {
			parentFolder.mkdirs();
		}
		
		initArchive(archive);

		currentLog.addEntry(EntryType.ARCHIVE, "Archive created.", "");

		currentArchive.save();
	}	

	private boolean isInitialized() {
		return (currentArchive != null);
	}
	
	private void checkInitialize() throws RTTException {
		if (!isInitialized()) {
			throw new RTTException(Type.NO_ARCHIVE, "No archive loaded.");
		}
	}
	
	public Archive getArchive() {
		return currentArchive;
	}
	
	public void exportLog(File location) throws Exception {
		checkInitialize();
		currentLog.export(location);
	}

	public void printArchiveInformations() throws Exception {
		checkInitialize();
		RTTLogging.info("Informations of archive:");
		currentArchive.print();
	}

	public void saveArchive(File archivePath) throws RTTException {
		checkInitialize();

		try {
			currentArchive.save();
		} catch (Exception e) {
			throw new RTTException(Type.NO_ARCHIVE, "Could not save archive.", e);
		}
	}
	
	/**
	 * This function sets a {@link Configuration} in this {@link Archive}. If the configuration does not exist, 
	 * it will be created. If a configuration with the given name exists, it will be updated unless the overwrite flag is set.
	 * <p>
	 * A {@link ConfigStatus} object will be returned to indicate which actions are taken by this function. See {@link ConfigStatus#ADDED}, {@link ConfigStatus#UPDATED} and {@link ConfigStatus#SKIPPED}.
	 * 
	 * @param configName the name of the configuration
	 * @param initialNode the name of initial node
	 * @param cpEntries a list of entries, which should be added to the class path.
	 * @param defaultConfig indicates, if new configuration should be the default configuration of this archive
	 * @param overwrite indicates, if an existing configuration should be overwritten
	 * @return a {@link ConfigStatus} object, indicating the taken actions
	 * @see ConfigStatus#ADDED
	 * @see ConfigStatus#UPDATED
	 * @see ConfigStatus#SKIPPED
	 */
	public ConfigStatus setConfiguration(String configName, String initialNode, 
			List<String> cpEntries, boolean defaultConfig, boolean overwrite) {
		
		List<Detail> infos = new LinkedList<Detail>();
		
		Configuration config = new Configuration();		
		config.setName(configName);
		Classpath cPath = new Classpath();
		cPath.getPath().addAll(cpEntries);
		config.setClasspath(cPath);
		
		config.setInitialNode(initialNode);
		
		ConfigStatus state = currentArchive.setConfiguration(config, overwrite);
		
		String message = null;
		switch (state) {
		case ADDED:
			message = "Configuration added: ";
			break;
			
		case UPDATED:
			message = "Configuration updated: ";
			break;
			
		case SKIPPED:
			message = "Configuration not changed: ";
			break;
		}

		if (state.initialNodeSet) {
			Detail detail = new Detail();
			detail.setMsg("Initial Node:");
			detail.setSuffix(initialNode);		
			detail.setPriority(0);
			
			infos.add(detail);
		}
		
		for (String entry : state.newEntries) {
			Detail pathInfo = new Detail();
			pathInfo.setPriority(1);
			pathInfo.setMsg("Added classpath entry: ");
			pathInfo.setSuffix(entry);
			
			infos.add(pathInfo);
		}
		
		for (String entry : state.deletedEntries) {
			Detail pathInfo = new Detail();
			pathInfo.setPriority(1);
			pathInfo.setMsg("Removed classpath entry: ");
			pathInfo.setSuffix(entry);
			
			infos.add(pathInfo);
		}
		
		currentLog.addEntry(EntryType.ARCHIVE, message, configName, infos);
		
		currentArchive.setActiveConfiguration(configName);		
		if (defaultConfig) {
			setDefaultConfiguration(configName);
		}

		return state;	
	}
	
	public void setDefaultConfiguration(String configName) {
		if (currentArchive.setDefaultConfiguration(configName) && verbose) {
			currentLog.addEntry(EntryType.INFO,
					"Default Configuration set: ", configName);
		}
	}
	
	public boolean createTestSuite(String suiteName) {
		boolean result = currentArchive.addTestsuite(suiteName);
		if (result) {
			currentLog.addEntry(EntryType.ARCHIVE, "Test suite created: ",
					suiteName);
		}

		return result;
	}
	
	public boolean removeTestsuite(String suiteName) throws RTTException {
		checkInitialize();
		
		if (!currentArchive.hasTestsuite(suiteName)) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Cannot find test suite: "
					+ suiteName);
		}
		
		List<Testcase> caseList = currentArchive.getTestcases(suiteName);
		List<Detail> detailList = new ArrayList<Detail>();
		
		// first, remove all test cases
		for (Testcase testcase : caseList) {
			Detail detail = new Detail();
			detail.setSuffix(testcase.getName());
			detail.setPriority(0);
			
			if (currentArchive.removeTestcase(suiteName, testcase.getName())) {
				detail.setMsg("Test case removed: ");
			} else {
				detail.setMsg("Test case could not be removed: ");
			}			
			
			detailList.add(detail);
		}
		
		boolean suiteRemoved = currentArchive.removeTestsuite(suiteName);
		
		String message = "Test suite could not be removed: ";
		if (suiteRemoved) {
			message = "Test suite removed: ";
		}
		currentLog.addEntry(EntryType.ARCHIVE, message, suiteName, detailList);
		
		return suiteRemoved;
	}
	
	/**
	 * Adds a set of files to the given test suite
	 * 
	 * @param files
	 * @param testSuite
	 * @param mode
	 * @throws Exception
	 */
	public List<RTTException> addAllFiles(List<File> files, String testSuite,
			TestCaseMode mode) {

		List<Detail> detailList = new LinkedList<Detail>();
		List<RTTException> exceptions = new ArrayList<RTTException>();
		for (File f : files) {
			try {
				detailList.add(addFile(f, testSuite, mode));
			} catch (RTTException e) {
				exceptions.add(e);
			}
		}

		currentLog.addEntry(EntryType.ARCHIVE,
				"Test suite modified: ", testSuite, detailList);

		return exceptions;
	}
	
	/**
	 * 
	 * @param file
	 * @param suiteName
	 * @param mode
	 * @return testinformation
	 * @throws Exception
	 */
	protected Detail addFile(File file, String suiteName, TestCaseMode mode)
			throws RTTException {

		checkInitialize();
		
		Testsuite t = currentArchive.getTestsuite(suiteName, false);
		if (t == null) {
			if (createTestSuite(suiteName) == false) {
				throw new RTTException(Type.OPERATION_FAILED,
						"Could not create test suite.");
			}
			t = currentArchive.getTestsuite(suiteName, false);
		}

		String caseName = TestsuiteManager.getCaseName(file);
		TestcaseStatus status = currentArchive.addTestcase(suiteName, file,
				mode);
		
		Detail detail = new Detail();
		detail.setSuffix(file.getAbsolutePath());
		detail.setPriority(0);

		String message = "Test case '" + caseName + "' ";
		switch (status) {
		case NEW:
			message += "added: ";
			break;
			
		case UPDATE:
			message += "updated: ";
			detail.setPriority(1);
			break;
			
		case ERROR:
			message += "had an error: ";
			detail.setPriority(1);
			break;
			
		case NONE:
			message += "not changed: ";
			break;

		default:
			message = "Unknown operation on " + message;
			break;
		}
		
		detail.setMsg(message);	
		
		RTTLogging.info(detail.getMsg() + detail.getSuffix());

		return detail;
	}
	
	public void removeTest(String suiteName, String caseName)
			throws RTTException {
		checkInitialize();
		
		if (!currentArchive.hasTestcase(suiteName, caseName)) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Cannot find test case: "
					+ suiteName + "/" + caseName);
		}
		
		currentArchive.removeTestcase(suiteName, caseName);
		currentLog.addEntry(EntryType.ARCHIVE, "Test case removed: ",
				suiteName + "/" + caseName);
	}
	
	public void setParametersToTest(String suiteName, String caseName, List<String> parameters) throws RTTException {
		checkInitialize();
		Testcase testcase = currentArchive.getTestcase(suiteName, caseName);
		if (testcase != null) {
			testcase.getParameter().clear();
			testcase.getParameter().addAll(parameters);
			
			currentLog.addEntry(EntryType.ARCHIVE, "Parameters set for test case: ", suiteName + "/" + caseName);
		}
	}
	
	public GenerationInformation generateTests(String suiteName)
			throws Exception {
		GenerationInformation results = new GenerationInformation(GenerationType.REFERENCE_DATA);

		if (suiteName == null) {
			for (Testsuite suite : currentArchive.getTestsuites(false)) {
				results.concat(generateTests(suite.getName()));
			}
		} else {
			results.concat(generateTests(suiteName,
					currentArchive.getActiveConfiguration()));
		}

		return results;
	}

	public GenerationInformation generateTests(String suiteName,
			Configuration config) throws Exception {
		
		checkInitialize();

		if (!currentArchive.hasTestsuite(suiteName)) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Test suite '" + suiteName
					+ "' does not exist.");
		}
		
		GenerationInformation genInfos = new GenerationInformation(GenerationType.REFERENCE_DATA);
		
		RTTLogging.info("Test suite: " + suiteName + " - Configuration: " + config.getName());
		
		Executor executor = DataGenerator.locateInitialNode(config, baseDir);
		
		RTTLogging.info("**** Generate reference data ****");
		
		for (Testcase tcase : currentArchive.getTestcases(suiteName)) {	
			
			// load reference data for the test case
			OutputDataManager refManager = new OutputDataManager(
					currentArchive.getLoader(), suiteName, tcase.getName(),
					config, OutputDataType.REFERENCE);

			// create new reference data
			GenerationResult result = refManager.createData(executor,
					tcase.getInputID(), tcase.getParameter());
			
			StringBuilder infoMessage = new StringBuilder();
			infoMessage.append("[" + suiteName + "/" + tcase.getName() + "]");

			if (result.noError) {
				// No error during the generation of the reference data
				refManager.save();
				
				infoMessage.append(" has been generated");

				// if reference data has replaced, update version data
				if (result.hasReplaced) {
					String configName = config.getName();
					VersionData versionData = currentArchive.getVersionData(tcase, configName, true);
					versionData.setReferenceID(versionData.getReferenceID() + 1);
					
					infoMessage.append(" and replaced");
				}
			} else {
				infoMessage.append(" has NOT been generated");
				
				StringBuilder errorMessage = new StringBuilder();
				errorMessage.append("[Exception] in Test case [");
				errorMessage.append(suiteName + "/" + tcase.getName());
				errorMessage.append("]: ");
				
				RTTLogging.error(errorMessage.toString(), result.exception);
			}

			genInfos.addResult(result);
			RTTLogging.info(infoMessage.toString());
		}

		List<Detail> details = genInfos.makeDetails(true);
		
		String message = "Reference data for test suite [" + suiteName + "]";
		if (!details.isEmpty()) {
			message += " generated with configuration: ";
		} else {
			message += " was empty. Configuration used: ";
		}
		
		currentLog.addEntry(EntryType.GENERATION, message, config.getName(), details);
		
		return genInfos;
	}

	public GenerationInformation runTests(String suiteName, boolean matching)
			throws Exception {

		GenerationInformation results = new GenerationInformation(GenerationType.TEST_DATA);
		
		if (suiteName == null) {
			List<Testsuite> suites = currentArchive.getTestsuites(false);
			for (Testsuite suite : suites) {
				results.concat(runTestsInternal(suite.getName(), matching));
			}
		} else {
			results.concat(runTestsInternal(suiteName, matching));
		}

		return results;
	}

	private GenerationInformation runTestsInternal(String suiteName, boolean matching)
			throws Exception {

		checkInitialize();
		
		if (currentArchive.hasTestsuite(suiteName) == false) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Test suite '"
					+ suiteName + "' does not exists or has been removed from archive.");
		}
		
		
		GenerationInformation genInfos = new GenerationInformation(GenerationType.TEST_DATA);
		Configuration config = currentArchive.getActiveConfiguration();
		
		RTTLogging.info("Test suite: " + suiteName + " - Configuration: " + config.getName());
		
		Executor executor = DataGenerator.locateInitialNode(config, baseDir);
		
		Tester tester = new Tester(currentArchive.getLoader(), matching);
		List<TestResult> testResults = new ArrayList<TestResult>();
		
		RTTLogging.info("**** Running tests ****");

		for (Testcase tcase : currentArchive.getTestcases(suiteName)) {
			
			// Create new test data manager 
			OutputDataManager testManager = new OutputDataManager(
					currentArchive.getLoader(), suiteName,
					tcase.getName(), config, OutputDataType.TEST);
			
			// Create new test data ...
			GenerationResult genResult = testManager.createData(executor, tcase.getInputID(), tcase.getParameter());
			genInfos.addResult(genResult);
			
			StringBuilder infoMessage = new StringBuilder();
			infoMessage.append("[" + suiteName + "/" + tcase.getName() + "]");
			infoMessage.append(" has been tested");
			
			// if test data could not have been generated, show skip
			TestResult caseResults = new TestResult(ResultType.SKIPPED, suiteName, tcase.getName());
			if (genResult.noError) {
				testManager.save();
				
				if (genResult.hasReplaced) {
					VersionData versionData = currentArchive.getVersionData(tcase, config.getName(), true);
					versionData.setTestID(versionData.getTestID() + 1);						
				}
				
				// do testing
				caseResults = tester.test(suiteName, tcase, config);
				
				if (caseResults != null) { 
					// results for test case are present -> no exceptions occurred
					if (caseResults.getType() == ResultType.FAILURE) {
						List<ITestFailure> failures = caseResults.getFailures();
						StringBuilder warnMessage = new StringBuilder();
						
						for (ITestFailure failure : failures) {
							warnMessage.append("[Failure] in Test case [");
							warnMessage.append(suiteName + "/" + tcase.getName());
							warnMessage.append("]: ");
							warnMessage.append(failure.getMessage());						
						}
						
						RTTLogging.warn(warnMessage.toString());
						infoMessage.append(" with failures");
					} else {
						infoMessage.append(" with no errors");
					}

					testResults.add(caseResults);
				}
			} else {
				// no results for test case -> exception occurred
				StringBuilder errorMessage = new StringBuilder();
				errorMessage.append("[Exception] in Test case [");
				errorMessage.append(suiteName + "/" + tcase.getName());
				errorMessage.append("]: ");

				RTTLogging.error(errorMessage.toString(), genResult.exception);
				infoMessage.append(" with exception(s)");
			}
			
			RTTLogging.info(infoMessage.toString());
		}
		
		List<Detail> details = genInfos.makeDetails(false);
		if (genInfos.hasErrors() && !details.isEmpty()) {
			String message = "Test data for test suite [" + suiteName + "] generated errors with configuration: ";
			currentLog.addEntry(EntryType.INFO, message, config.getName(), details);
		}
		
		currentLog.addTestrunResult(testResults, config.getName(), suiteName);	

		return genInfos;
	}

	public void close() {
		archivePath = null;
		currentLog = null;
		currentArchive.close();
		currentArchive = null;
	}
}
