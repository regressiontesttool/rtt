package rtt.core.utils;

import java.io.File;

import rtt.core.exceptions.RTTException;

/**
 * This is an utility class for test suites.
 * 
 * @author C.Oelsner <C.Oelsner@web.de>
 */
public class TestSuiteUtils {
	
	/**
	 * Adds a new test suite to a rtt archive.
	 * 
	 * @param archiveFile file handler of the archive
	 * @param suiteName the name of the new test suite
	 * @throws RTTException 
	 */
	public static void addTestSuite(File archiveFile, String suiteName) throws RTTException {
		ArchiveContainer container = new ArchiveContainer(archiveFile);
		container.addTestSuite(suiteName);
		container.save();
	}
	
	/**
	 * Removes a given test suite from a rtt archive.
	 * Also all containing test cases will be removed from the archive.
	 * 
	 * @param archiveFile file handler of the archive
	 * @param suiteName the name of the test suite
	 * @throws RTTException 
	 */
	public static void removeTestSuite(File archiveFile, String suiteName) throws RTTException {
		ArchiveContainer container = new ArchiveContainer(archiveFile);
		container.removeTestSuite(suiteName);
		container.save();
	}

}
