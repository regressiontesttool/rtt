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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rtt.core.archive.Archive;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.LexerClass;
import rtt.core.archive.configuration.ParserClass;
import rtt.core.archive.configuration.Path;
import rtt.core.archive.logging.Detail;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.archive.testsuite.VersionData;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.ZipArchiveLoader;
import rtt.core.manager.data.ConfigurationManager.ConfigStatus;
import rtt.core.manager.data.LogManager;
import rtt.core.manager.data.ReferenceManager;
import rtt.core.manager.data.AbstractTestDataManager.GenerationInfo;
import rtt.core.manager.data.TestManager;
import rtt.core.manager.data.TestsuiteManager;
import rtt.core.manager.data.TestsuiteManager.TestcaseStatus;
import rtt.core.testing.Tester;
import rtt.core.testing.compare.results.ITestFailure;
import rtt.core.testing.compare.results.TestResult;
import rtt.core.testing.compare.results.TestResult.ResultType;
import rtt.core.testing.generation.DataGenerator;
import rtt.core.testing.generation.LexerExecuter;
import rtt.core.testing.generation.ParserExecuter;
import rtt.core.utils.DebugLog;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class Manager {

	public enum TestCaseMode {
		SKIP, IGNORE, RENAME, OVERWRITE
	}

	File archivePath;
	Archive currentArchive;
	LogManager currentLog;
	private String baseDir;

	public static boolean verbose = true;

	public Manager(File archivePath, boolean verbose) {
		// because sometimes, it is not the same

		this.archivePath = archivePath;
		Thread.currentThread().setContextClassLoader(
				this.getClass().getClassLoader());

		Manager.verbose = verbose;
	}

	public void createArchive() throws Exception {
		currentArchive = new Archive(getArchiveLoader(archivePath));
		currentLog = currentArchive.getLogManager();
		currentArchive.save();
	}

	public void loadArchive(String config) throws RTTException {
		loadArchive();
		boolean hasChanged = currentArchive.setActiveConfiguration(config);

		if (hasChanged) {
			DebugLog.log("Active configuration: " + config);
		}
	}

	public void loadArchive() throws RTTException {
		currentArchive = new Archive(getArchiveLoader(archivePath));
		currentLog = currentArchive.getLogManager();

		try {
			currentArchive.load();
		} catch (Exception e) {
			throw new RTTException(Type.ARCHIVE, "Could not load archive.", e);
		}

	}

	private ArchiveLoader getArchiveLoader(File path) throws RTTException {
		if (!(path.isDirectory() || path.getPath().endsWith("zip")))
			throw new RTTException(Type.ARCHIVE, path.getAbsolutePath()
					+ " is no supported Archive");

		File aPath = path.getAbsoluteFile();
		if (aPath == null) {
			throw new RTTException(Type.ARCHIVE, "Absolute file of '"
					+ path.getAbsolutePath() + "' returned null.");
		}

		try {
			ArchiveLoader loader = new ZipArchiveLoader(aPath.getParent(), aPath.getName());
			this.baseDir = loader.getBaseDir();
			
			return loader;
		} catch (Exception e) {
			throw new RTTException(Type.ARCHIVE,
					"Could not start archive loader", e);
		}

	}

	private boolean isInitialized() {
		return (currentArchive != null);
	}

	public void printArchiveInformations() throws Exception {

		if (!isInitialized())
			throw new Exception("no archive loaded.");

		System.out.println("ArchiveInformations of archive:");

		currentArchive.print();
	}

	public void saveArchive(File archivePath) throws RTTException {
		if (!isInitialized())
			throw new RTTException(Type.ARCHIVE, "No archive loaded.");

		try {
			currentArchive.save();
		} catch (Exception e) {
			throw new RTTException(Type.ARCHIVE, "Could not save archive.", e);
		}
	}

	/**
	 * Adds a new Entry to the classpath of the active configuration.
	 * 
	 * @param entry
	 * @return true, if added.
	 * @throws Exception
	 */
	public boolean addClassPathEntry(String entry) throws Exception {
		return addClassPathEntry(currentArchive.getActiveConfiguration()
				.getName(), entry);

	}

	public boolean addClassPathEntry(String configName, String entry) {
		Configuration config = currentArchive.getConfiguration(configName);
		if (config != null) {
			Classpath classpath = config.getClasspath();

			if (classpath == null) {
				classpath = new Classpath();
			}

			for (Path path : classpath.getPath()) {
				if (path.getValue().equals(entry)) {
					return false;
				}
			}

			Path newPath = new Path();
			newPath.setValue(entry);

			return classpath.getPath().add(newPath);
		}

		return false;
	}

	public boolean removeClassPathEntry(String configName, String entry) {
		Configuration config = currentArchive.getConfiguration(configName);
		if (config != null) {
			int index = -1;
			Classpath classpath = config.getClasspath();

			if (classpath != null && classpath.getPath() != null) {
				List<Path> pathList = classpath.getPath();

				for (int i = 0; i < pathList.size(); i++) {
					if (pathList.get(i).getValue().equals(entry)) {
						index = i;
					}
				}

				if (index > -1) {
					pathList.remove(index);
					return true;
				}
			}
		}

		return false;
	}

	public List<Throwable> generateTests(String suiteName) throws RTTException {
		List<Throwable> errors = new ArrayList<Throwable>();

		if (suiteName == null) {
			for (Testsuite suite : currentArchive.getTestsuites(false)) {
				errors.addAll(generateTests(suite.getName()));
			}
		} else {
			errors.addAll(generateTests(suiteName,
					currentArchive.getActiveConfiguration()));
		}

		return errors;
	}

	public List<Throwable> generateTests(String suiteName, Configuration config)
			throws RTTException {

		if (!isInitialized())
			throw new RTTException(Type.ARCHIVE, "No archive loaded.");

		Testsuite suite = currentArchive.getTestsuite(suiteName, false);
		if (suiteName != null && suite == null) {
			throw new RTTException(Type.TESTSUITE, "Cant find testsuite: "
					+ suiteName);
		}

		List<Throwable> generationExceptions = new ArrayList<Throwable>();
		List<Detail> genInfos = new ArrayList<Detail>();
		
		LexerExecuter lexer = DataGenerator.getLexerExecuter(config, baseDir);
		ParserExecuter parser = DataGenerator.getParserExecuter(config, baseDir);

		for (Testcase tcase : suite.getTestcase()) {
			if (tcase.isDeleted()) {
				// CHRISTIAN quick n dirty
				continue;
			}

			ReferenceManager refManager = new ReferenceManager(
					currentArchive.getLoader(), suite.getName(),
					tcase.getName(), config);
			try {
				DebugLog.log("Generate tests for [" + suiteName + "/"
						+ tcase.getName() + "]");

				GenerationInfo genInfo = refManager.createData(lexer, parser);
				// CHRISTIAN todo !
				refManager.save();
				
				if (genInfo.lexerReplaced || genInfo.parserReplaced || genInfo.lexerVersioned || genInfo.parserVersioned) {
					VersionData versionData = tcase.getVersionData();
					versionData.setReference(versionData.getReference() + 1);
					tcase.setVersionData(versionData);
				}
				

				if (genInfo.lexerVersioned) {
					// TestInformation info = new TestInformation();
					// info.setMsg("Lexer output for test [" + tcase.getName()
					// + "] in testsuite [" + suite.getName()
					// + "] were versioned.");
					// info.setPriority(2);
					// info.setTest(tcase.getName());
					// genInfos.add(info);
				}

				if (genInfo.parserVersioned) {
					// TestInformation info = new TestInformation();
					// info.setMsg("Parser output for test [" + tcase.getName()
					// + "] in testsuite [" + suite.getName()
					// + "] were versioned.");
					// info.setPriority(2);
					// info.setTest(tcase.getName());
					// genInfos.add(info);
				}

				if (genInfo.somethingDone
						&& (genInfo.lexerReplaced || genInfo.parserReplaced)) {

					Detail info = new Detail();

					String message = "";
					if (genInfo.lexerReplaced)
						message += "Lexer ";
					if (genInfo.lexerReplaced && genInfo.parserReplaced)
						message += "and ";
					if (genInfo.parserReplaced)
						message += "Parser ";

					info.setMsg(message += "Results for testcase ["
							+ tcase.getName() + "] in testsuite ["
							+ suite.getName() + "] were generated.");
					info.setPriority(1);
					info.setSuffix(tcase.getName());
					genInfos.add(info);
				} else {
					Detail info = new Detail();
					info.setMsg("Results for test [" + tcase.getName()
							+ "] in testsuite [" + suite.getName()
							+ "] were not changed.");
					info.setPriority(0);
					info.setSuffix(tcase.getName());
					genInfos.add(info);
				}

			} catch (Throwable e) {
				if (e instanceof InvocationTargetException) {
					e = e.getCause();
				}

				DebugLog.printTrace(e);

				currentLog.addInformational(
						"Error during generation of Test Results: " + e,
						suite.getName() + "/" + tcase.getName());

				generationExceptions.add(e);
			}
		}

		currentLog.addGenInformational(
				"Testresults generated for configuration: ", config.getName(),
				genInfos);

		return generationExceptions;
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

		boolean result = true;
		if (Manager.verbose)
			System.out.println("Running Tests...");

		if (!isInitialized())
			throw new RTTException(Type.ARCHIVE, "No archive loaded.");

		Testsuite suite = currentArchive.getTestsuite(suiteName, false);

		if (suite == null) {
			throw new RTTException(Type.TESTSUITE, "Cant find testSuite: "
					+ suiteName);
		}

		Configuration configuration = currentArchive.getActiveConfiguration();
		List<TestResult> results = new ArrayList<TestResult>();
		
		LexerExecuter lexer = DataGenerator.getLexerExecuter(configuration, baseDir);
		ParserExecuter parser = DataGenerator.getParserExecuter(configuration, baseDir);

		for (Testcase tcase : suite.getTestcase()) {
			if (tcase.isDeleted()) {
				// CHRISTIAN quick n dirty
				continue;
			}						

			TestManager testManager = new TestManager(
					currentArchive.getLoader(), suite.getName(),
					tcase.getName(), configuration);

			testManager.createData(lexer, parser);

			// CHRISTIAN todo !
			testManager.save();
			
			VersionData versionData = tcase.getVersionData();
			versionData.setTest(versionData.getTest() + 1);
			tcase.setVersionData(versionData);

			DebugLog.log("Running tests for [" + suiteName + "/"
					+ tcase.getName() + "]");

			Tester t = new Tester(currentArchive.getLoader(), matching);
			TestResult caseResults = t.test(suite, tcase, configuration);

			if (caseResults != null) {
				// System.err.println("Errors occured ("+results.size()+")");
				
				if (caseResults.getType() == ResultType.FAILURE) {
					List<ITestFailure> failures = caseResults.getFailures();
					for (ITestFailure failure : failures) {
						System.err.println("\n[Error](in Testcase '"
								+ tcase.getName() + "', Testsuite '"
								+ suite.getName() + "')");
						System.err.println(failure.getMessage());

						result = false;
					}
				}
				
				results.add(caseResults);
			}		
		}

		if (result == true) {
			if (Manager.verbose)
				System.out.println("No errors occured");
		}

		currentLog.addTestrunResult(results, configuration.getName(), suiteName);

		return result;
	}

	/**
	 * Adds a set of files to the given testsuite
	 * 
	 * @param files
	 * @param testSuite
	 * @param mode
	 * @throws Exception
	 */
	public void addAllFiles(List<File> files, String testSuite,
			TestCaseMode mode) throws RTTException {

		List<Detail> ti = new LinkedList<Detail>();
		for (File f : files) {
			List<Detail> tmp = addFile(f, testSuite, mode);
			if (tmp != null)
				ti.addAll(tmp);
		}

		currentLog.addInformational("New Files added for testsuite: ",
				testSuite, ti);
	}

	public Testsuite createTestSuite(String suiteName) {
		currentArchive.addTestsuite(suiteName);
		return currentArchive.getTestsuite(suiteName, false);
	}

	/**
	 * mode = 0 => throw exception, if file already exists mode = 1 => ignore
	 * double files mode = 2 => rename new files mode = 3 => overwrite old file
	 * (saving old file under versioning)
	 * 
	 * @param file
	 * @param suiteName
	 * @param mode
	 * @return testinformation
	 * @throws Exception
	 */
	public List<Detail> addFile(File file, String suiteName,
			TestCaseMode mode) throws RTTException {

		if (!isInitialized())
			throw new RTTException(Type.ARCHIVE, "No archive loaded.");

		List<Detail> result = new LinkedList<Detail>();

		Testsuite t = currentArchive.getTestsuite(suiteName, false);
		if (t == null) {
			t = createTestSuite(suiteName);
		}

		String caseName = TestsuiteManager.getCaseName(file);
		TestcaseStatus status = currentArchive.addTestcase(suiteName, file,
				mode);

		if (status == TestcaseStatus.UPDATE) {
			DebugLog.log("Testcase [" + suiteName + "/" + caseName
					+ "] updated.");

			String msg = "Testcase [" + caseName + "] in [" + suiteName
					+ "] overwritten.";
			Detail info = new Detail();
			info.setMsg(msg);
			info.setSuffix(caseName);
			info.setPriority(1);
			result.add(info);
		} else if (status == TestcaseStatus.NEW) {
			DebugLog.log("Testcase [" + suiteName + "/" + caseName + "] added.");

			Detail info = new Detail();
			info.setMsg("Testcase added: " + file.getPath() + " as Testcase "
					+ caseName + " [Testsuite: " + suiteName + "]");
			info.setSuffix(caseName);
			info.setPriority(0);
			result.add(info);
		}

		return result;
	}

	public void setDefaultConfiguration(String configName) {
		if (currentArchive.setDefaultConfiguration(configName) && verbose) {
			currentLog.addInformational("Default Configuration changed: ",
					configName);
		}
	}

	/**
	 * returns true, if added
	 * 
	 * @param lexerName
	 * @param parserName
	 * @param configName
	 * @param defaultConfig
	 * @param overwrite
	 * @param cpEntries
	 * @return true, if configuration could be created.
	 * @throws Exception
	 */
	public boolean createConfiguration(String lexerName, String parserName,
			String configName, boolean defaultConfig, boolean overwrite,
			List<String> cpEntries) throws RTTException {

		List<Detail> infos = new LinkedList<Detail>();

		Configuration config = currentArchive.getConfiguration(configName);

		boolean confUpdated = false;

		if (config != null && overwrite == false) {
			Detail info = new Detail();
			info.setPriority(2);
			info.setMsg("Configuration [" + configName + "] already exists.");
			info.setSuffix(configName);
			infos.add(info);
			return false;
		}

		if (config == null) {
			config = new Configuration();
			config.setName(configName);

			config.setClasspath(new Classpath());
			confUpdated = true;
		}

		if (lexerName != null) {
			LexerClass lexerClass = config.getLexerClass();
			if (lexerClass == null || lexerClass.getValue() == null) {
				lexerClass = new LexerClass();
				lexerClass.setValue(lexerName);
				config.setLexerClass(lexerClass);
				confUpdated = true;
			} else if (!lexerClass.getValue().equals(lexerName)) {
				lexerClass.setValue(lexerName);
				confUpdated = true;
			}
		}

		if (parserName != null) {
			ParserClass parserClass = config.getParserClass();
			if (parserClass == null || parserClass.getValue() == null) {
				parserClass = new ParserClass();
				parserClass.setValue(parserName);
				config.setParserClass(parserClass);
				confUpdated = true;
			} else if (!parserClass.getValue().equals(parserName)) {
				parserClass.setValue(parserName);
				confUpdated = true;
			}
		}

		List<Path> pathList = config.getClasspath().getPath();
		Set<String> newPathEntries = new TreeSet<String>(cpEntries);

		// find only new path entries
		for (Path path : pathList) {
			if (cpEntries.contains(path.getValue())) {
				newPathEntries.remove(path.getValue());
			}
		}

		// add new path entries
		for (String string : newPathEntries) {
			Path newPath = new Path();
			newPath.setValue(string);
			pathList.add(newPath);
		}

		ConfigStatus state = currentArchive.addConfiguration(config, overwrite);

		if (confUpdated) {
			Detail info = new Detail();
			info.setSuffix(configName);

			switch (state) {
			case CREATED:
				info.setPriority(0);
				info.setMsg("Configuration [" + config.getName()
						+ "] added. (Lexer: " + lexerName + ", Parser: "
						+ parserName + ")");
				break;

			case REPLACED:
				info.setPriority(1);
				info.setMsg("Configuration [" + config.getName()
						+ "] updated. (Lexer: " + lexerName + ", Parser: "
						+ parserName + ")");
				break;

			default:
				throw new IllegalStateException(
						"AddConfiguration returned illegal state '" + state
								+ "'");
			}
			infos.add(info);
		}

		// added log infos for new path entries
		for (String string : newPathEntries) {
			Detail pathInfo = new Detail();
			pathInfo.setPriority(1);
			pathInfo.setMsg("Class Path: " + string);
			pathInfo.setSuffix(configName);
			infos.add(pathInfo);
		}

		currentArchive.setActiveConfiguration(configName);
		if (defaultConfig) {
			setDefaultConfiguration(configName);
		}

		if (infos.size() > 0) {
			currentLog.addInformational("Configuration changed: ", configName,
					infos);
		}

		return true;
	}

	// CHRISTIAN todo
	public void exportLog(File location) throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		currentArchive.getLogManager().export(location);
	}

	public void removeTestsuite(String testSuite) throws RTTException {
		if (!isInitialized())
			throw new RTTException(Type.ARCHIVE, "No archive loaded.");

		Testsuite t = currentArchive.getTestsuite(testSuite, false);
		if (t == null)
			throw new RTTException(Type.TESTSUITE, "Cannot find testsuite: "
					+ testSuite);

		List<Testcase> testcases = new ArrayList<Testcase>(t.getTestcase());

		for (Testcase tc : testcases)
			removeTest(t, tc);

		currentArchive.removeTestsuite(testSuite);
		currentLog.addInformational("Testsuite removed: ", testSuite);
	}

	public void removeTest(String testSuit, String testCase)
			throws RTTException {
		if (!isInitialized())
			throw new RTTException(Type.ARCHIVE, "No archive loaded.");

		removeTest(currentArchive.getTestsuite(testSuit, false),
				currentArchive.getTestcase(testSuit, testCase, false));
	}

	public void removeTest(Testsuite testSuit, Testcase testCase)
			throws RTTException {

		if (!isInitialized())
			throw new RTTException(Type.ARCHIVE, "No archive loaded.");

		// versionTest(testSuit, testCase);

		currentArchive.removeTestcase(testSuit.getName(), testCase.getName());
		currentLog.addInformational("Testcase removed: ", testSuit.getName()
				+ "/" + testCase.getName());

	}

	public void clearClassPaths() throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		Configuration configuration = currentArchive.getActiveConfiguration();

		if (configuration.getClasspath() != null) {
			configuration.getClasspath().getPath().clear();
		}
	}

	public Archive getArchive() {
		return currentArchive;
	}
}
