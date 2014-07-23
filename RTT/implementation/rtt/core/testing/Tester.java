package rtt.core.testing;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.output.Output;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.VersionData;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.results.TestExecutionFailure;
import rtt.core.testing.compare.results.TestFailure;
import rtt.core.testing.compare.results.TestResult;
import rtt.core.testing.compare.results.TestResult.ResultType;
import rtt.core.utils.RTTLogging;

public class Tester {

	ArchiveLoader loader;
	boolean matching;

	public Tester(ArchiveLoader loader, boolean matching) {
		super();
		this.loader = loader;
	}

	public TestResult test(String suiteName, Testcase tcase,
			Configuration config) {

		if (tcase == null) {
			throw new NullPointerException("Testcase was null.");
		}

		if (config == null) {
			throw new NullPointerException("Configuration was null.");
		}
		
		String caseName = tcase.getName();
		
		// open a reference and test manager
		OutputDataManager refManager = new OutputDataManager(loader, suiteName, caseName, config, OutputDataType.REFERENCE);
		OutputDataManager testManager = new OutputDataManager(loader, suiteName, caseName, config, OutputDataType.TEST);
		
		TestResult result = new TestResult(ResultType.SKIPPED, suiteName, caseName);
		
		// find current version of data for given configuration
		VersionData versionData = null;
		for (VersionData tempData : tcase.getVersionData()) {
			if (tempData.getConfig().equals(config.getName())) {
				versionData = tempData;
			}
		}
		
		// if no version data was found, return SKIPPED
		if (versionData == null) {
			return result;
		}
		
		// if version data was found, set reference and test version to result
		result.setRefVersion(versionData.getReferenceID());
		result.setTestVersion(versionData.getTestID());
		
		// if the input version is newer than the last used version of the reference manager
		// then return skipped.
		if (refManager.isOutDated(tcase.getInputID()) || testManager.isOutDated(tcase.getInputID())) {
			return result;
		}
		
		boolean testSuccess = true;
		
		try {
			Output testData = testManager.getOutputData(versionData.getTestID());
			Output refData = refManager.getOutputData(versionData.getReferenceID()); 
			
			List<TestFailure> failures = compareOutput(testData, refData);
			
			if (failures != null && !failures.isEmpty()) {
				for (TestFailure testFailure : failures) {
					result.addFailure(testFailure);
				}
				testSuccess = false;
			}
		} catch (RTTException e) {
			RTTLogging.debug(e.getMessage(), e);
			result.addFailure(new TestExecutionFailure(e));
			testSuccess = false;
		}

		if (testSuccess) {
			result.setType(ResultType.SUCCESS);
		} else {
			result.setType(ResultType.FAILURE);
		}

		return result;
	}

	private List<TestFailure> compareOutput(Output testData, Output refData) throws RTTException {
		
		checkData(testData, refData);
		RTTLogging.info("Testing Syntactic Results");
		return OutputCompare.compareOutput(testData, refData, false,
				matching);
	}

	private void checkData(Object testData, Object refData) throws RTTException {
		if (testData != null && refData == null) {
			throw new IllegalStateException(
					"Reference data was null, but test data not!");
		}

		if (refData == null) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Reference data was null.");
		}

		if (testData == null) {
			throw new RTTException(Type.DATA_NOT_FOUND, "Test data was null.");
		}
	}

}
