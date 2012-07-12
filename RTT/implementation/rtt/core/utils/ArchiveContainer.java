package rtt.core.utils;

import java.io.File;
import java.util.List;

import rtt.core.archive.logging.Detail;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.manager.Manager;
import rtt.core.manager.Manager.TestCaseMode;

/**
 * This class is a container for storing archive file and manager
 * within one object. It's also used as facade and abstracting manager access.
 * 
 * @author C.Oelsner <C.Oelsner@web.de>
 */
public class ArchiveContainer {
	private File archiveFile;
	private Manager manager;
	
	/**
	 * Creates a new archive container and loads the content from the given archive file handler.
	 * 
	 * @param archiveFile file handler of the archive file
	 * @throws RTTException 
	 */
	public ArchiveContainer(File archiveFile) throws RTTException {
		try {
			this.archiveFile = archiveFile;
			this.manager = new Manager(archiveFile, true);
			manager.loadArchive();
		} catch (Exception e) {
			throw new RTTException(Type.ARCHIVE, "Can not load archive.", e);
		}		
	}
	
	/**
	 * Saves all changes within the archive.
	 * @throws RTTException 
	 */
	public void save() throws RTTException {
		try {
			manager.saveArchive(archiveFile);
		} catch (Exception e) {
			throw new RTTException(Type.ARCHIVE, "Could not save archive.", e);
		}	
	}

	/**
	 * Adds a new test suite to the archive. Changes need to be saved 
	 * manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @param suiteName the name of the new test suite
	 */
	public void addTestSuite(String suiteName) {
		manager.createTestSuite(suiteName);
	}

	/**
	 * Removes a test suite from the archive. Changes need to be saved 
	 * manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @param suiteName the name of the test suite
	 * @throws RTTException 
	 */
	public void removeTestSuite(String suiteName) throws RTTException {
		try {
			manager.removeTestsuite(suiteName);
		} catch (Exception e) {
			throw new RTTException(Type.TESTSUITE, "Could not remove test suite", e);
		}
	}

	/**
	 * Adds new test cases to the given test suite within the archive. 
	 * The name of the new test cases will be computed by the name of the test files.
	 * The mode sets the behavior during adding of new test cases. 
	 * 
	 * Changes need to be saved manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @see TestCaseMode
	 * @param suiteName the name of the test suite
	 * @param caseFiles list of file handlers for test cases
	 * @param mode the adding mode (see {@link TestCaseMode})
	 * @throws RTTException 
	 */
	public void addTestCases(String suiteName, List<File> caseFiles, TestCaseMode mode) throws RTTException {
		try {
			manager.addAllFiles(caseFiles, suiteName, mode);
		} catch (Exception e) {
			throw new RTTException(Type.TESTCASE, "Could not add test case.", e);
		}
	}
	
	/**
	 * Adds new test cases to the given test suite within the archive. 
	 * The name of the new test cases will be computed by the name of the test files.
	 * Test cases with the same name will be overwritten ({@link TestCaseMode#OVERWRITE})
	 * 
	 * Changes need to be saved manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @see #addTestCases(String, List, TestCaseMode)
	 * @param suiteName the name of the test suite
	 * @param caseFiles list of file handlers for test cases
	 * @throws RTTException 
	 */
	public void addTestCases(String suiteName, List<File> caseFiles) throws RTTException {
		addTestCases(suiteName, caseFiles, TestCaseMode.OVERWRITE);
	}
	
	/**
	 * Adds a new test case to the given test suite within the archive. 
	 * The name of the new test case will be computed by the name of the test file.
	 * A test case with the same name will be overwritten ({@link TestCaseMode#OVERWRITE})
	 * 
	 * Changes need to be saved manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @param suiteName the name of the test suite
	 * @param caseFile the file handler of the test case
	 * @throws RTTException 
	 */
	public void addTestCase(String suiteName, File caseFile) throws RTTException  {
		addTestCase(suiteName, caseFile, TestCaseMode.OVERWRITE);		
	}
	
	/**
	 * Adds a new test case to the given test suite within the archive. 
	 * The name of the new test case will be computed by the name of the test file.
	 * The mode sets the behavior during adding of new test cases. 
	 * 
	 * Changes need to be saved manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @param suiteName the name of the test suite
	 * @param caseFile the file handler of the test case
	 * @param mode mode the adding mode (see {@link TestCaseMode})
	 * @throws RTTException 
	 */
	public void addTestCase(String suiteName, File caseFile, TestCaseMode mode) throws RTTException  {
		try {
			// TODO
//			List<Detail> r = manager.addFile(caseFile, suiteName, mode);
//			manager.getArchive().getLogManager().addInformational("File added", "", r);
		} catch (Exception e) {
			throw new RTTException(Type.TESTCASE, "Could not add test case.", e);
		}		
		
	}

	/**
	 * Removes a test case from the given test suite within the archive.
	 * 
	 * Changes need to be saved manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @param testSuite the name of the test suite
	 * @param testCase the name of the test case
	 * @throws RTTException 
	 */
	public void removeTestCase(String testSuite, String testCase) throws RTTException {
		try {
			manager.removeTest(testSuite, testCase);
		} catch (Exception e) {
			throw new RTTException(Type.TESTCASE, "Could not remove test case.", e);
		}
	}

	/**
	 * Adds a new configuration to the archive, with the given parameters.
	 * 
	 * Changes need to be saved manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @param lexerClass the fully qualified name of the lexer class
	 * @param parserClass the fully qualified name of the lexer class
	 * @param configName the name of the new configuration
	 * @param defaultConfig make the new configuration the default configuration of the archive
	 * @param overwrite overwrite configuration
	 * @param cpEntries class path entries of configuration
	 * @throws RTTException 
	 */
	public void addConfiguration(
			String lexerClass, 
			String parserClass, 
			String configName, 
			boolean defaultConfig, 
			boolean overwrite, 
			List<String> cpEntries
		) throws RTTException {
		
		try {
			manager.createConfiguration(lexerClass, parserClass, configName, defaultConfig, overwrite, cpEntries);
		} catch (Exception e) {
			throw new RTTException(Type.CONFIGURATION, "Could not add configuration.", e);
		}
	}
	
	/**
	 * Sets the given configuration as the default configuration.
	 * @param configName the name of the configuration
	 */
	public void setConfigurationDefault(String configName) {
		manager.setDefaultConfiguration(configName);
	}
	
	/**
	 * This method creates all test reference data for the given test suite.
	 * During generation the default configuration of the archive will be used.
	 * After finishing the generation, this method will return a list of all 
	 * errors which occurred during the generation.
	 * 
	 * Changes need to be saved manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @param suiteName the name of the test suite
	 * @return list of errors during data generation
	 * @throws RTTException 
	 */
	public List<Throwable> generateReferenceData(String suiteName) throws RTTException {
		try {
			return manager.generateTests(suiteName);
		} catch (Exception e) {
			throw new RTTException(Type.TEST, "Could not generate reference data.", e);
		}
	}
	
	/**
	 * This method runs all tests for the given test suite. 
	 * The matching parameter allows to compare matching structures of 
	 * partial trees rather than strict compare.
	 * 
	 * Changes need to be saved manually by calling {@link ArchiveContainer#save()}!
	 * 
	 * @param suiteName the name of the test suite
	 * @param matching compare trees by matching
	 * @throws RTTException 
	 */
	public void runTests(String suiteName, boolean matching) throws RTTException {
		try {
			manager.runTests(suiteName, matching);
		} catch (Exception e) {
			throw new RTTException(Type.TEST, "Could not run tests", e);
		}
	}

	public void setActiveConfiguration(String configName) {
		manager.getArchive().setActiveConfiguration(configName);
	}

	
}
