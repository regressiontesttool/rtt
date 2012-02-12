package rtt.core.utils;

import java.io.File;

import rtt.core.exceptions.RTTException;

/**
 * This is an utility class for test cases.
 * 
 * @author C.Oelsner <C.Oelsner@web.de>
 */
public class TestCaseUtils {
	
	/**
	 * Adds a new test case to the given rtt archive within the given test suite.
	 * The name of the new test case will be name of the case file (without extension)
	 * 
	 * @param archiveFile file handler of archive
	 * @param suiteName the name of the test suite, where the case should be added
	 * @param caseFile the file handler for the test case.
	 * @throws RTTException 
	 */
	public static void addTestCase(File archiveFile, String suiteName, File caseFile) throws RTTException {
		ArchiveContainer container = new ArchiveContainer(archiveFile);
		container.addTestCase(suiteName, caseFile);
		container.save();
	}
	
	/**
	 * Removes a given test case from a rtt archive.
	 * 
	 * @param archiveFile file handler of the archive
	 * @param suiteName the name of the parental test suite
	 * @param caseName the name of the case, which should be removed.
	 * @throws RTTException 
	 */
	public static void removeTestCase(File archiveFile, String suiteName, String caseName) throws RTTException {
		ArchiveContainer container = new ArchiveContainer(archiveFile);
		container.removeTestCase(suiteName, caseName);
		container.save();
	}

}
