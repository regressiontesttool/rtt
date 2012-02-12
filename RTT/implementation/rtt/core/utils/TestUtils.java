package rtt.core.utils;

import java.io.File;
import java.util.List;

import rtt.core.exceptions.RTTException;

/**
 * This is an utility class for tests.
 * 
 * @author C.Oelsner <C.Oelsner@web.de>
 */
public class TestUtils {
	
	/**
	 * This method creates all test reference data for the given test suite.
	 * During generation the default configuration of the archive will be used.
	 * After finishing the generation, this method will return a list of all 
	 * errors which occurred during the generation.
	 * 
	 * @param archiveFile file handler of the archive
	 * @param suiteName the name of the test suite
	 * @return list of errors during data generation
	 * @throws RTTException 
	 */
	public static List<Throwable> generateReferenceData(File archiveFile, String suiteName, String configName) throws RTTException {
		ArchiveContainer container = new ArchiveContainer(archiveFile);	
		container.setActiveConfiguration(configName);
		List<Throwable> exceptions = container.generateReferenceData(suiteName);
		container.save();
		
		return exceptions;
	}

	/**
	 * This method runs all tests for the given test suite. 
	 * The matching parameter allows to compare matching structures of 
	 * partial trees rather than strict compare.
	 * 
	 * @param archiveFile file handler of the archive
	 * @param suiteName the name of the suite
	 * @param matching compare trees by matching
	 * @throws RTTException 
	 */
	public static void runTests(File archiveFile, String suiteName, boolean matching) throws RTTException {
		ArchiveContainer container = new ArchiveContainer(archiveFile);
		container.runTests(suiteName, matching);
		container.save();
	}

}
