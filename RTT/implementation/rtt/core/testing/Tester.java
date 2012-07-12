package rtt.core.testing;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.archive.testsuite.VersionData;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.history.OutputDataManager;
import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.core.testing.compare.LexerOutputCompare;
import rtt.core.testing.compare.ParserOutputCompare;
import rtt.core.testing.compare.results.LexerTestFailure;
import rtt.core.testing.compare.results.ParserTestFailure;
import rtt.core.testing.compare.results.TestExecutionFailure;
import rtt.core.testing.compare.results.TestResult;
import rtt.core.testing.compare.results.TestResult.ResultType;
import rtt.core.utils.DebugLog;
import rtt.core.utils.DebugLog.LogType;

public class Tester {

	ArchiveLoader loader;
	boolean matching;

	public Tester(ArchiveLoader loader, boolean matching) {
		super();
		this.loader = loader;
	}

	public TestResult test(Testsuite suite, Testcase tcase,
			Configuration config) {
		
		if (suite == null) {
			throw new NullPointerException("Testsuite was null.");
		}

		if (tcase == null) {
			throw new NullPointerException("Testcase was null.");
		}

		if (config == null) {
			throw new NullPointerException("Configuration was null.");
		}
		
		String suiteName = suite.getName();
		String caseName = tcase.getName();
		
		OutputDataManager refManager = new OutputDataManager(loader, suiteName, caseName, config, OutputDataType.REFERENCE);
		OutputDataManager testManager = new OutputDataManager(loader, suiteName, caseName, config, OutputDataType.TEST);
		
		TestResult result = new TestResult(ResultType.SKIPPED, suiteName, tcase.getName());
		
		VersionData versionData = null;
		for (VersionData tempData : tcase.getVersionData()) {
			if (tempData.getConfig().equals(config.getName())) {
				versionData = tempData;
			}
		}
		
		if (versionData == null) {
			return result;
		}
		
		result.setRefVersion(versionData.getReference());
		result.setTestVersion(versionData.getTest());
		
		if (refManager.isOutDated(tcase.getInput()) || testManager.isOutDated(tcase.getInput())) {
			return result;
		}
		
		boolean testSuccess = true;
		boolean somethingTested = false;
		
		try {
			LexerTestFailure lexerFailure = testLexer(
					testManager.getLexerOutput(versionData.getTest()),
					refManager.getLexerOutput(versionData.getReference()));
			
			if (lexerFailure != null) {
				result.addFailure(lexerFailure);
				testSuccess = false;
			}

			somethingTested = true;
		} catch (RTTException e) {
			DebugLog.printTrace(e);
			result.addFailure(new TestExecutionFailure(e));
			testSuccess = false;
		}
		
		try {
			List<ParserTestFailure> parserFailure = testParser(
					testManager.getParserOutput(versionData.getTest()),
					refManager.getParserOutput(versionData.getReference()));
			if (parserFailure != null && parserFailure.size() > 0) {
				for (ParserTestFailure parserTestFailure : parserFailure) {
					result.addFailure(parserTestFailure);
				}
				testSuccess = false;
			}

			somethingTested = true;
		} catch (RTTException e) {
			DebugLog.printTrace(e);
			result.addFailure(new TestExecutionFailure(e));
			testSuccess = false;
		}
		
		if (somethingTested) {
			if (testSuccess) {
				result.setType(ResultType.SUCCESS);
			} else {
				result.setType(ResultType.FAILURE);
			}
		}

		return result;
	}

	private LexerTestFailure testLexer(LexerOutput testData, LexerOutput refData)
			throws RTTException {
		checkData(testData, refData);
		DebugLog.log(LogType.VERBOSE, "Testing Lexic Results");
		return LexerOutputCompare.compareLexerOutput(testData, refData, false);
	}

	private List<ParserTestFailure> testParser(ParserOutput testData,
			ParserOutput refData) throws RTTException {
		checkData(testData, refData);
		DebugLog.log(LogType.VERBOSE, "Testing Syntactic Results");
		return ParserOutputCompare.compareParserOuput(testData, refData, false,
				matching);
	}

	private void checkData(Object testData, Object refData) throws RTTException {
		if (testData != null && refData == null) {
			throw new IllegalStateException(
					"Reference data was null, but test data not!");
		}

		if (refData == null) {
			throw new RTTException(Type.TEST, "Reference data was null.");
		}

		if (testData == null) {
			throw new RTTException(Type.TEST, "Test data was null.");
		}
	}

}
