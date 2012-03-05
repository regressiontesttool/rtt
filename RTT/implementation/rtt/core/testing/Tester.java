package rtt.core.testing;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.history.Version;
import rtt.core.archive.output.LexerOutput;
import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.core.loader.ArchiveLoader;
import rtt.core.manager.data.ReferenceManager;
import rtt.core.manager.data.TestManager;
import rtt.core.testing.compare.LexerOutputCompare;
import rtt.core.testing.compare.ParserOutputCompare;
import rtt.core.testing.compare.results.LexerTestFailure;
import rtt.core.testing.compare.results.ParserTestFailure;
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

	public List<TestResult> test(Testsuite suite,
			Configuration config) {
		if (suite == null) {
			throw new NullPointerException("Testsuite was null.");
		}

		if (config == null) {
			throw new NullPointerException("Configuration was null.");
		}

		List<TestResult> resultList = new ArrayList<TestResult>();

		for (Testcase testcase : suite.getTestcase()) {
			if (testcase.isDeleted() == false) {
				resultList.add(test(suite, testcase, config));
			}
			
		}

		return resultList;
	}

	public TestResult test(Testsuite suite, Testcase testcase,
			Configuration config) {
		if (suite == null) {
			throw new NullPointerException("Testsuite was null.");
		}

		if (testcase == null) {
			throw new NullPointerException("Testcase was null.");
		}

		if (config == null) {
			throw new NullPointerException("Configuration was null.");
		}

		return test(suite.getName(), testcase.getName(), config);
	}

	private TestResult test(String suiteName, String caseName,
			Configuration config) {
		ReferenceManager refManager = new ReferenceManager(loader, suiteName,
				caseName, config);
		TestManager testManager = new TestManager(loader, suiteName, caseName,
				config);

		boolean somethingTested = false;
		boolean testSuccess = true;
		
		Integer testVersion = testManager.getCurrentNr();
		Integer refVersion = -1;
		for (Version v : testManager.getHistory().getVersion()) {
			if (v.getNr() == testVersion) {
				refVersion = v.getReference();
			}
		}		

		TestResult result = new TestResult(ResultType.FAILURE,
				refVersion, testVersion);

		if (testManager.isUpToDate()) {
			try {
				LexerTestFailure lexerFailure = testLexer(
						testManager.getLexerOutput(),
						refManager.getLexerOutput());
				if (lexerFailure != null) {
					result.addFailure(lexerFailure);
					testSuccess = false;
				}

				somethingTested = true;
			} catch (RTTException e) {
				DebugLog.printTrace(e);
			}

			try {
				List<ParserTestFailure> parserFailure = testParser(
						testManager.getParserOutput(),
						refManager.getParserOutput());
				if (parserFailure != null && parserFailure.size() > 0) {
					for (ParserTestFailure parserTestFailure : parserFailure) {
						result.addFailure(parserTestFailure);
					}
					testSuccess = false;
				}

				somethingTested = true;
			} catch (RTTException e) {
				DebugLog.printTrace(e);
			}
		}

		if (!somethingTested) {
			// DebugLog.log(LogType.ALL, "No testdata for Testcase '"
			// + test.getName() + "' in testsuite '" + t.getName()
			// + "'.");

			result = new TestResult(ResultType.SKIPPED,
					refVersion, testVersion);
		} else if (testSuccess) {
			result = new TestResult(ResultType.SUCCESS,
					refVersion, testVersion);
		}

		result.setCaseName(caseName);
		result.setSuiteName(suiteName);

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
