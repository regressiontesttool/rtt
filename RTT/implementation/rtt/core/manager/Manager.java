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
import rtt.core.archive.configuration.Path;
import rtt.core.archive.logging.Detail;
import rtt.core.archive.logging.EntryType;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.archive.testsuite.VersionData;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.ZipArchiveLoader;
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
import rtt.core.testing.generation.LexerExecutor;
import rtt.core.testing.generation.ParserExecutor;
import rtt.core.utils.Debug;
import rtt.core.utils.GenerationInformation;
import rtt.core.utils.GenerationInformation.GenerationResult;

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

	public static boolean verbose = true;

	public Manager(File archivePath, boolean verbose) {

		this.archivePath = archivePath;

		// because sometimes, it is not the same
		Thread.currentThread().setContextClassLoader(
				this.getClass().getClassLoader());

		Manager.verbose = verbose;
	}

	public void createArchive() throws Exception {
		currentArchive = new Archive(getArchiveLoader(archivePath));
		currentLog = currentArchive.getLogManager();

		currentLog.addEntry(EntryType.ARCHIVE, "Archive created.", "");

		currentArchive.save();
	}

	public void loadArchive(String config) throws RTTException {
		loadArchive();
		boolean hasChanged = currentArchive.setActiveConfiguration(config);

		if (hasChanged) {
			Debug.log("Active configuration: " + config);
		}
	}

	public void loadArchive() throws RTTException {
		currentArchive = new Archive(getArchiveLoader(archivePath));
		currentLog = currentArchive.getLogManager();

		try {
			currentArchive.load();
		} catch (Exception e) {
			throw new RTTException(Type.NO_ARCHIVE, "Could not load archive.", e);
		}

	}

	private ArchiveLoader getArchiveLoader(File path) throws RTTException {
		if (!(path.isDirectory() || path.getPath().endsWith("zip")))
			throw new RTTException(Type.NO_ARCHIVE, path.getAbsolutePath()
					+ " is no supported Archive");

		File aPath = path.getAbsoluteFile();
		if (aPath == null) {
			throw new RTTException(Type.NO_ARCHIVE, "Absolute file of '"
					+ path.getAbsolutePath() + "' returned null.");
		}

		try {
			ArchiveLoader loader = new ZipArchiveLoader(aPath.getParent(),
					aPath.getName());
			this.baseDir = loader.getBasePath();

			return loader;
		} catch (Exception e) {
			throw new RTTException(Type.NO_ARCHIVE,
					"Could not start archive loader", e);
		}

	}

	private boolean isInitialized() {
		return (currentArchive != null);
	}
	
	public Archive getArchive() {
		return currentArchive;
	}
	
	public void exportLog(File location) throws Exception {
		if (!isInitialized())
			throw new Exception("No archive loaded.");

		currentLog.export(location);
	}

	public void printArchiveInformations() throws Exception {

		if (!isInitialized())
			throw new Exception("no archive loaded.");

		System.out.println("Informations of archive:");

		currentArchive.print();
	}

	public void saveArchive(File archivePath) throws RTTException {
		if (!isInitialized())
			throw new RTTException(Type.NO_ARCHIVE, "No archive loaded.");

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
	 * @param lexerName the name of lexer class
	 * @param parserName the name of parser class
	 * @param cpEntries a list of entries, which should be added to the class path.
	 * @param defaultConfig indicates, if new configuration should be the default configuration of this archive
	 * @param overwrite indicates, if an existing configuration should be overwritten
	 * @return a {@link ConfigStatus} object, indicating the taken actions
	 * @see ConfigStatus#ADDED
	 * @see ConfigStatus#UPDATED
	 * @see ConfigStatus#SKIPPED
	 */
	public ConfigStatus setConfiguration(String configName, String lexerName,
			String parserName, List<String> cpEntries, boolean defaultConfig,
			boolean overwrite) {
		
		List<Detail> infos = new LinkedList<Detail>();
		
		Configuration config = new Configuration();		
		config.setName(configName);
		Classpath cPath = new Classpath();
		for (String entry : cpEntries) {
			Path path = new Path();
			path.setValue(entry);
			
			cPath.getPath().add(path);
		}
		config.setClasspath(cPath);
		
		config.setLexerClass(lexerName);
		config.setParserClass(parserName);
		
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
		
		if (state.lexerSet) {
			Detail detail = new Detail();
			detail.setMsg("Lexer class: ");
			detail.setSuffix(lexerName);
			detail.setPriority(0);
			
			infos.add(detail);
		}
		
		if (state.parserSet) {
			Detail detail = new Detail();
			detail.setMsg("Parser class: ");
			detail.setSuffix(parserName);
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
		if (!isInitialized())
			throw new RTTException(Type.NO_ARCHIVE, "No archive loaded.");
		
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

		if (!isInitialized())
			throw new RTTException(Type.NO_ARCHIVE, "No archive loaded.");

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
		
		Debug.log(detail.getMsg() + detail.getSuffix());

		return detail;
	}
	
	public void removeTest(String suiteName, String caseName)
			throws RTTException {
		if (!isInitialized())
			throw new RTTException(Type.NO_ARCHIVE, "No archive loaded.");
		
		if (!currentArchive.hasTestcase(suiteName, caseName)) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Cannot find test case: "
					+ suiteName + "/" + caseName);
		}
		
		currentArchive.removeTestcase(suiteName, caseName);
		currentLog.addEntry(EntryType.ARCHIVE, "Test case removed: ",
				suiteName + "/" + caseName);
	}
	
	public GenerationInformation generateTests(String suiteName)
			throws RTTException {
		GenerationInformation results = new GenerationInformation();

		if (suiteName == null) {
			for (Testsuite suite : currentArchive.getTestsuites(false)) {
				results.addResults(generateTests(suite.getName()));
			}
		} else {
			results.addResults(generateTests(suiteName,
					currentArchive.getActiveConfiguration()));
		}

		return results;
	}

	public GenerationInformation generateTests(String suiteName,
			Configuration config) throws RTTException {

		if (!isInitialized())
			throw new RTTException(Type.NO_ARCHIVE, "No archive loaded.");

		if (!currentArchive.hasTestsuite(suiteName)) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Test suite '" + suiteName
					+ "' does not exist.");
		}

		GenerationInformation genInfos = new GenerationInformation();

		// create executors
		LexerExecutor lexer = DataGenerator.getLexerExecutor(config, baseDir);
		ParserExecutor parser = DataGenerator
				.getParserExecutor(config, baseDir);

		for (Testcase tcase : currentArchive.getTestcases(suiteName)) {

			// load reference data for the test case
			OutputDataManager refManager = new OutputDataManager(
					currentArchive.getLoader(), suiteName, tcase.getName(),
					config, OutputDataType.REFERENCE);

			Debug.log("Generate tests for [" + suiteName + "/"
					+ tcase.getName() + "]");

			// create new reference data
			GenerationResult result = refManager.createData(lexer, parser,
					tcase.getInput());

			if (result.noError) {
				// No error during the generation of the reference data
				refManager.save();

				// if reference data has replaced, update version data
				if (result.hasReplaced) {

					String configName = config.getName();

					// find version data for current config in test case data
					List<VersionData> versionList = tcase.getVersionData();
					VersionData versionData = null;
					for (VersionData tempData : versionList) {
						if (tempData.getConfig().equals(configName)) {
							versionData = tempData;
							break;
						}
					}

					// if no version data was found, then create, otherwise
					// remove
					if (versionData == null) {
						versionData = new VersionData();
						versionData.setConfig(configName);
					} else {
						versionList.remove(versionData);
					}

					// update the version data to current reference version &
					// save
					versionData.setReference(versionData.getReference() + 1);
					versionList.add(versionData);
				}
			} else {
				currentLog.addEntry(EntryType.INFO, result.getMessage(),
						result.suiteName + "/" + result.caseName);
			}

			genInfos.addGenerationResult(result);
		}

		List<Detail> details = genInfos.makeDetails();
		
		String message = "Reference data for test suite [" + suiteName + "]";
		if (!details.isEmpty()) {
			message += " generated with configuration: ";
		} else {
			message += " was empty. Configuration used: ";
		}
		
		currentLog.addEntry(EntryType.GENERATION, message, config.getName(), details);

		return genInfos;
	}

	public boolean runTests(String suiteName, boolean matching)
			throws RTTException {
		boolean result = true;

		if (suiteName == null) {
			List<Testsuite> suites = currentArchive.getTestsuites(false);
			for (Testsuite suite : suites) {
				if (runTestsInternal(suite.getName(), matching) == false) {
					result = false;
				}
			}
		} else {
			result = runTestsInternal(suiteName, matching);
		}

		return result;
	}

	private boolean runTestsInternal(String suiteName, boolean matching)
			throws RTTException {

		if (Manager.verbose)
			System.out.println("Running Tests...");

		if (!isInitialized())
			throw new RTTException(Type.NO_ARCHIVE, "No archive loaded.");
		
		if (currentArchive.hasTestsuite(suiteName) == false) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Test suite '"
					+ suiteName + "' does not exist or was removed from archive.");
		}
		
		boolean result = true;
		
		
		Configuration configuration = currentArchive.getActiveConfiguration();
		LexerExecutor lexer = DataGenerator.getLexerExecutor(configuration,
				baseDir);
		ParserExecutor parser = DataGenerator.getParserExecutor(configuration,
				baseDir);
		
		Tester tester = new Tester(currentArchive.getLoader(), matching);
		List<TestResult> results = new ArrayList<TestResult>();

		for (Testcase tcase : currentArchive.getTestcases(suiteName)) {
			try {
				
				// Create new test data manager 
				OutputDataManager testManager = new OutputDataManager(
						currentArchive.getLoader(), suiteName,
						tcase.getName(), configuration, OutputDataType.TEST);
				
				// Create new test data ...
				GenerationResult info = testManager.createData(lexer, parser, tcase.getInput());
				if (info.noError) {
					testManager.save();
					
					if (info.hasReplaced) {
						for (VersionData versionData : tcase.getVersionData()) {
							if (versionData.getConfig().equals(configuration.getName())) {
								versionData.setTest(versionData.getTest() + 1);
							}
						}						
					}					
				}				

				Debug.log("Running tests for [" + suiteName + "/" + tcase.getName() + "]");

				// do testing
				TestResult caseResults = tester.test(suiteName, tcase, configuration);

				if (caseResults != null) {
					// System.err.println("Errors occured ("+results.size()+")");

					if (caseResults.getType() == ResultType.FAILURE) {
						List<ITestFailure> failures = caseResults.getFailures();
						for (ITestFailure failure : failures) {
							System.err.println("\n[Error](in Testcase '"
									+ tcase.getName() + "', Testsuite '"
									+ suiteName + "')");
							System.err.println(failure.getMessage());

							result = false;
						}
					}

					results.add(caseResults);
				}

				if (result == true) {
					if (Manager.verbose)
						System.out.println("No errors occured");
				}
			} catch (Exception e) {
				// currentLog.addInformational(
				// "Error during testing: " + e,
				// suite.getName() + "/" + tcase.getName());
			}
		}

		currentLog
				.addTestrunResult(results, configuration.getName(), suiteName);

		return result;
	}
}
