/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.managing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import rtt.archive.Testsuit;
import rtt.archive.Classpath;
import rtt.archive.Configuration;
import rtt.archive.Input;
import rtt.archive.InputRef;
import rtt.archive.LexerClass;
import rtt.archive.ParserClass;
import rtt.archive.Path;
import rtt.archive.Testcase;
import rtt.archive.Testsuit;
import rtt.archive.Testsuites;
import rtt.archive.utils.ArchiveLoader;
import rtt.archive.utils.TrueZipLoader;
import rtt.logging.TestInformation;
import rtt.managing.Tester.TestFailure;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class Manager {
	File archivePath;

	Configuration configuration;
	Archive currentArchive;
	public Logging currentLog;

	public static boolean verbose = true;

	public Manager(File archivePath, boolean verbose) {

		// because sometimes, it is not the same
		Thread.currentThread().setContextClassLoader(
				this.getClass().getClassLoader());

		this.archivePath = archivePath;
		currentArchive = null;
		currentLog = null;
		configuration = null;

		Manager.verbose = verbose;
	}

	public void loadArchive() throws Exception {
		loadArchive(null);
	}

	public void loadArchive(String config) throws Exception {
		ArchiveLoader loader = getArchiveLoader(archivePath);
		loader.setCurrentConfiguration(config);

		currentArchive = new Archive(loader);
		loader.setCurrentArchive(currentArchive);
		currentArchive.load(archivePath);

		currentLog = new Logging(loader);
		currentLog.load(archivePath);

	
		
		configuration = currentArchive.getConfiguration(config);
		if (Manager.verbose)
			if (configuration != null)
				System.out.println("Current Configuration: "
						+ configuration.getName());
			else
				System.out.println("No Configuration loaded.");
				
		currentArchive.currentArchiveLoader.closeFile();
	}

	private ArchiveLoader getArchiveLoader(File path) throws Exception {
		if (!(path.isDirectory() || path.getPath().endsWith("zip")))
			throw new Exception(archivePath.getAbsolutePath()
					+ " is no supported Archive");

		return new TrueZipLoader();

	}

	private boolean isInitialized() {
		return (currentArchive != null);
//		return (currentArchive != null) && (configuration != null)
//				&& (currentLog != null);
	}

	public void printArchiveInformations() throws Exception {

		if (!isInitialized())
			throw new Exception("no archive loaded.");

		System.out.println("ArchiveInformations of archive: "
				+ archivePath.getAbsolutePath());

		currentArchive.print();
	}

	public void saveArchive(File archivePath) throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");
	
		currentArchive.save(archivePath);
		currentLog.save(archivePath);

		currentArchive.currentArchiveLoader.closeFile();
	}

	// adds classPath to current Configuration
	public void addClassPathEntry(String path) throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		Path p = new Path();
		p.setValue(path);

		if (configuration.getClasspath() == null)
			configuration.setClasspath(new Classpath());

		// add only if not already existing
		for (Path p2 : configuration.getClasspath().getPath())
			if (p.getValue().equals(p2.getValue()))
				return;

		configuration.getClasspath().getPath().add(p);
	}

	public void generateTests(String testSuiteName)
			throws Throwable {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		if (testSuiteName != null
				&& currentArchive.getTestSuite(testSuiteName) == null)
			throw new Exception("Cant find testSuite: " + testSuiteName);

		try {
			TestGenerator generator = new TestGenerator();
			List<TestInformation> genInfos = generator.generateTests(
					currentArchive, configuration, testSuiteName,
					archivePath.getAbsolutePath(), this);
			currentLog
					.addGenInformational("Testresults generated for configuration: ",
							configuration.getName(),
							genInfos);

		} catch (Throwable e) {
			if (verbose)
				e.printStackTrace();
			currentLog.addInformational(
					"Error during generation of Test Results: "
							+ e.getMessage(), testSuiteName);
			//throw e;
		}
	}

	public void merge(File archive2) throws Exception {
		System.out.println("Merging archives");
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		ArchiveLoader loader = getArchiveLoader(archive2);

		Archive secondArchive = new Archive(loader);
		secondArchive.load(archive2);

		currentArchive.merge(secondArchive);

		currentLog.addMerge(archive2);
	}

	public boolean runTests(String testSuite, boolean matching)
			throws Exception {
		boolean result = true;
		if (Manager.verbose)
			System.out.println("Running Tests...");

		if (!isInitialized())
			throw new Exception("no archive loaded.");

		if (testSuite != null && currentArchive.getTestSuite(testSuite) == null)
			throw new Exception("Cant find testSuite: " + testSuite);

		Tester t = new Tester();
		List<Tester.TestResult> results = t.test(currentArchive, configuration,
				testSuite, archivePath.getAbsolutePath(), matching, false,
				currentLog);

		if (results.size() > 0) {
			// System.err.println("Errors occured ("+results.size()+")");
			for (int i = 0; i < results.size(); ++i) {
				if (!(results.get(i) instanceof TestFailure))
					continue;
				Tester.TestFailure e = (TestFailure) results.get(i);

				System.err.println("\n[Error " + i + "](in Testcase '"
						+ e.getTestCase().getName() + "', Testsuite '"
						+ e.getTestSuite().getName() + "')");
				System.err.println(e.getMessage());

				result = false;
			}

		}

		if (result == true) {
			if (Manager.verbose)
				System.out.println("No errors occured");
		}

		this.currentLog.addTestrunResult(results, configuration.getName());

		return result;
	}

	public void addAllFiles(List<File> files, String testSuite, int mode)
			throws Exception {
		List<TestInformation> ti = new LinkedList<TestInformation>();
		for (File f : files) {
			List<TestInformation> tmp = addFile(f, testSuite, mode);
			if (tmp != null)
				ti.addAll(tmp);
		}

		currentLog.addInformational("New Files added for testsuite: ",
				testSuite, ti);
	}

	
	public Testsuit createTestSuite(String testSuite)
	{
		Testsuit t = currentArchive.getTestSuite(testSuite);
		if (t != null)
			return t;
		
		t = new Testsuit();
		t.setName(testSuite);
		currentArchive.getArchiveLoader().setFileFor(t, testSuite + ".xml");
		List<Testsuit> ts = currentArchive.getTestSuites();
		ts.add(t);
		currentArchive.setTestSuites(ts);
		return t;
	}
	
	
	/**
	 * mode = 0 => throw exception, if file already exists mode = 1 => ignore
	 * double files mode = 2 => rename new files mode = 3 => overwrite old file
	 * (saving old file under versioning)
	 * 
	 * @param file
	 * @param testSuite
	 * @param mode
	 * @throws Exception
	 */
	public List<TestInformation> addFile(File file, String testSuite, int mode)
			throws Exception {

		if (!isInitialized())
			throw new Exception("no archive loaded.");
		List<TestInformation> result = new LinkedList<TestInformation>();

		Testsuit t = currentArchive.getTestSuite(testSuite);
		if (t == null) {
			t = createTestSuite(testSuite);
		}
		Testcase tc = null;
		Input i = new Input();
		boolean overwritten = false;
		
		// TODO:das mit dem punkt anders irgendwie
		String testCaseName = getTestcaseName(file);
		String sourceFileName = testCaseName;
		String fileContent = readFileContent(file);
		if ((tc = findTestCase(testCaseName, t)) != null) {
			if (mode == 0) // error
			{
				System.out.println("Testcase [" + tc.getName() + "] in "
						+ t.getName()
						+ " already exists. Addition of test skipped.");
				return null;
			} else if (mode == 2) // rename
			{
				// testCaseName = findUniqueName(t, testCaseName);
				sourceFileName = testCaseName;
				tc = null;

				// currentLog.addInformational("Testcase renamed: " +
				// testCaseName + "[Testsuite: " + testSuite + "]");

			} else if (mode == 3) // overwrite w/ new version
			{
				// versioning, indem einfach die referenz auf eine andere datei
				// (die neue) gelegt wird
				i = tc.getInput();
				if (i == null)
					i = tc.getInputRef().getInput();

				// find out, if its a new version or the same!!!
				if (fileContent.equals(i.getValue()))// no new version
				{
					System.out.println("Testcase [" + testCaseName
							+ "] is the same. Not Added.");
					return null;
				}

				// String f = currentArchive.getArchiveLoader().getFileName(i);
				versionTest(t, tc);

				sourceFileName = testCaseName; // findUniqueName(t,
												// testCaseName);
				tc.setLexerOutputRef(null);
				tc.setParserOutputRef(null);
				String msg = "Testcase [" + tc.getName() + "] in [" + testSuite
						+ "] overwritten.";
				// currentLog.addInformational();
				TestInformation info = new TestInformation();
				info.setMsg(msg);
				info.setTest(tc.getName());
				info.setPriority(1);
				overwritten = true;
				result.add(info);
			}

		}

		if (tc == null) {
			tc = new Testcase();
			tc.setName(testCaseName);
			sourceFileName = findUniqueName(t, testCaseName);
			t.getTestcase().add(tc);
		}
		i.setValue(fileContent);
		InputRef ref = new InputRef();
		ref.setInput(i);
		currentArchive.getArchiveLoader().setFileFor(i,
				testSuite + "/" + sourceFileName + ".src");
		tc.setInputRef(ref);
		if (!overwritten)
		{
			TestInformation info = new TestInformation();
			info.setMsg("Testcase added: " + file.getPath() + " as Testcase "
					+ tc.getName() + " [Testsuite: " + testSuite + "]");
			info.setTest(tc.getName());
			info.setPriority(0);
			result.add(info);
		}
		return result;
	}

	private String getTestcaseName(File file) {
		return file.getName().substring(0, file.getName().lastIndexOf('.'));
	}

	private String findUniqueName(Testsuit t, String testCaseName) {

		if (findTestCase(testCaseName, t) == null) {
			// if (testCaseName.matches(".*\\$\\d"))
			// return testCaseName;

			return testCaseName; // version zero
		}

		int counter = 1;
		String[] val = testCaseName.split(File.separator+"$");
		if (val.length > 1) {
			counter = Integer.parseInt(val[val.length - 1]) + 1;
		}

		String testName = val[0] + "$";
		for (int i = 1; i < val.length - 1; i++)
			testName = testName.concat(val[i]) + "$";

		testCaseName = testName + counter;

		// recursive because it can be, that this name was already
		// generated...just try with generated name again
		return findUniqueName(t, testCaseName);
	}

	private Testcase findTestCase(String name, Testsuit testsuit) {
		for (Testcase c : testsuit.getTestcase()) {
			if (c.getName().equals(name))
				return c;
		}
		return null;
	}

	private String readFileContent(File f) throws Exception {
		// System.out.println("loading: " + fileName);
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(f
				.getAbsolutePath())); // warum keine file??!
		try {
			char[] buf = new char[1024];

			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}
		} finally {
			reader.close();
		}
		return fileData.toString();
	}

	public void createArchive() throws Exception {
		ArchiveLoader l = getArchiveLoader(archivePath);

		currentArchive = new Archive(l);
		currentArchive.create();

		currentLog = new Logging(l);
		currentLog.create();

	}

	public void setDefaultConfiguration(String configName)
	{
		String oldConfig = currentArchive.getArchive().getConfigurations().getDefault();
		
		currentArchive.getArchive().getConfigurations().setDefault(configName);
		
		
		if (verbose && oldConfig != null && !oldConfig.equals(configName))
			currentLog.addInformational("Default Configuration changed: ", configName);
		
	}
	
	
	
	/**
	 * returns true, if added
	 * 
	 * @param lexerClass
	 * @param parserClass
	 * @param configName
	 * @param defaultConfig
	 * @param overwrite
	 * @param cpEntries 
	 * @throws Exception
	 */
	public boolean createConfiguration(String lexerClass, String parserClass,
			String configName, boolean defaultConfig, boolean overwrite, List<String> cpEntries)
			throws Exception {

		List<TestInformation> infos = new LinkedList<TestInformation>();

	
		boolean confUpdated = false;
		
		if (configName == null) {
			configName = "default";
			defaultConfig = true;
		}

		if (currentArchive.hasConfiguration(configName)) {
			if (!overwrite) {
				TestInformation info = new TestInformation();
				info.setPriority(2);
				info.setMsg("Configuration [" + configName
						+ "] already exists.");
				info.setTest(configName);
				infos.add(info);
				return false;
			}
			configuration = currentArchive.getConfiguration(configName);
			if (defaultConfig)
				setDefaultConfiguration(configName);
			confUpdated = true;
			
		} else {
			configuration = new Configuration();
			configuration.setName(configName);
			currentArchive.addConfiguration(configuration, defaultConfig);

			TestInformation info = new TestInformation();
			info.setPriority(0);
			info.setMsg("Configuration [" + configuration.getName()
					+ "] added. (Lexer: " + lexerClass + ", Parser: "
					+ parserClass + ")");
			info.setTest(configName);
			infos.add(info);
			
		}

		boolean updated = false;
		currentArchive.getArchiveLoader().setCurrentConfiguration(configName);

		if (lexerClass != null) {
			LexerClass lc = new LexerClass();
			lc.setValue(lexerClass);
			if (configuration.getLexerClass() != null
					&& !configuration.getLexerClass().getValue().equals(
							lc.getValue()))
				updated = true;
			configuration.setLexerClass(lc);
		}

		if (parserClass != null) {
			ParserClass pc = new ParserClass();
			pc.setValue(parserClass);
			if (configuration.getParserClass() != null
					&& !configuration.getParserClass().getValue().equals(
							pc.getValue()))
				updated = true;
			configuration.setParserClass(pc);
		}

		if (updated) {
			TestInformation info = new TestInformation();
			info.setPriority(1);
			info.setMsg("Configuration [" + configuration.getName()
					+ "] updated. (Lexer: " + lexerClass + ", Parser: "
					+ parserClass + ")");
			info.setTest(configName);
			infos.add(info);
			
		}
		
		boolean newCPEntries = true;
		
		if (confUpdated)
		{
			List<Path> ocp = configuration.getClasspath().getPath();
			//are the new classPaths really new?
			
		
			if (ocp.size() == cpEntries.size())
			{
				int equalEntries = 0;
				for (Path path : ocp) {
					if (cpEntries.contains(path.getValue()))
						equalEntries++;
				}
				if (equalEntries == ocp.size())
					newCPEntries = false;
			}
		}
			
			
		if (newCPEntries)
		{
			for(String cp : cpEntries)
			{
				TestInformation info = new TestInformation();
				info.setPriority(1);
				info.setMsg("Class Path: " + cp);
				info.setTest(configName);
				infos.add(info);
			}
		}
		
		clearClassPaths();
		
		for(String cp : cpEntries)
		{
			addClassPathEntry(cp);
		}
		
		
		if (infos.size() > 0)
			currentLog.addInformational("Configuration changed: ", configName,
					infos);

		return true;

	}

	public void exportLog(File location) throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		File dir = location;

		if (!dir.isDirectory())
			dir = location.getCanonicalFile().getParentFile();
		if (location.isDirectory())
			location = new File(dir.getAbsolutePath() + File.separator
					+ "log.xml");

		System.out.println("Dir:  " + dir + "\nLoc: "
				+ location.getAbsolutePath());

		ArchiveLoader loader = currentArchive.getArchiveLoader();

		loader.saveLog(currentLog.log, location);

		InputStream logStream = getClass().getResourceAsStream("/log.xslt");
		File f = new File(dir.getAbsolutePath() + "/log.xslt");

		FileOutputStream fos = new FileOutputStream(f, false);
		Reader r = new InputStreamReader(logStream);
		int curByte = -1;
		while ((curByte = r.read()) != -1) {
			fos.write(curByte);
		}
		r.close();
		fos.close();
		logStream.close();

		System.out.println("Logging File exported to: " + location);

	}

	public void removeTestsuite(String testSuite) throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		Testsuit t = currentArchive.getTestSuite(testSuite);
		if (t == null)
			throw new Exception("Cannot find testsuite: " + testSuite);

		for (Testcase tc : t.getTestcase())
			removeTest(t, tc);

		List<Testsuit> suites = currentArchive.getTestSuites();
		suites.remove(t);
		currentArchive.setTestSuites(suites);
		currentLog.addInformational("Testsuite removed: ", testSuite);

	}

	public void removeTest(String testSuit, String testCase) throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		removeTest(currentArchive.getTestSuite(testSuit), currentArchive
				.getTest(testSuit, testCase));
	}

	public void removeTest(Testsuit testSuit, Testcase testCase)
			throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		versionTest(testSuit, testCase);

		currentArchive.removeTest(testSuit.getName(), testCase.getName());
		currentLog.addInformational("Testcase removed: ", testSuit.getName() + "/" + testCase.getName());

	}

	/**
	 * increments the testversion
	 * 
	 * @param testSuit
	 * @param testCase
	 * @throws Exception
	 */
	protected void versionTest(Testsuit testSuit, Testcase testCase)
			throws Exception {
		// String timeStamp = getNewTimestamp();
		testCase.setVersion(testCase.getVersion() + 1);

		String inputFile = "input"+File.separator + testSuit.getName() + File.separator
				+ testCase.getName() + ".src";
		String newInputFile = "input"+File.separator + testSuit.getName() + File.separator
				+ testCase.getName() + "." + testCase.getVersion() + ".src";

		currentArchive.getArchiveLoader().moveFileToPath(inputFile,
				newInputFile);
		for (Configuration c : currentArchive.getArchive().getConfigurations()
				.getConfiguration()) {
			versionResults(testSuit, testCase, c, true, true);
		}
	}

	// protected String getNewTimestamp() {
	// java.sql.Timestamp ts = new
	// java.sql.Timestamp(System.currentTimeMillis());
	// String timeStamp = ( "_" + ts.toString()).replace(' ','_');
	// return timeStamp;
	// }

	/**
	 * does NOT increment the testversion!
	 */
	protected void versionResults(Testsuit testSuit, Testcase testCase,
			Configuration configuration, boolean lexer, boolean parser)
			throws Exception {

		System.out.println("moving output files to bin");
		String outputFile = "output"+File.separator + configuration.getName() + File.separator
				+ testSuit.getName() + File.separator + testCase.getName();
		String lOut = outputFile + ".lexer.xml";
		String lOutNew = outputFile + "." + testCase.getVersion()
				+ ".lexer.xml";

		String pOut = outputFile + ".parser.xml";
		String pOutNew = outputFile + "." + testCase.getVersion()
				+ ".parser.xml";

		if (lexer)
			currentArchive.getArchiveLoader().moveFileToPath(lOut, lOutNew);
		if (parser)
			currentArchive.getArchiveLoader().moveFileToPath(pOut, pOutNew);

	}

	public void clearClassPaths() throws Exception {
		if (!isInitialized())
			throw new Exception("no archive loaded.");

		if (configuration.getClasspath() != null)
			configuration.getClasspath().getPath().clear();
	}

}
