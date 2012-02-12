package rtt.core.manager.data;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.archive.testsuite.Testsuites;
import rtt.core.archive.testsuite.VersionData;
import rtt.core.archive.input.Input;
import rtt.core.loader.ArchiveLoader;
import rtt.core.loader.fetching.SimpleFileFetching;
import rtt.core.manager.Manager.TestCaseMode;
import rtt.core.utils.DebugLog;

public class TestsuiteManager extends DataManager<Testsuites> {

	public enum TestcaseStatus {
		NEW, UPDATE, NONE, ERROR;
	}

	public TestsuiteManager(ArchiveLoader loader) {
		super(loader, new SimpleFileFetching("tsuite.xml", ""));
	}

	@Override
	public Testsuites doLoad() throws Exception {
		return unmarshall(Testsuites.class);
	}

	@Override
	public void doSave(Testsuites data) {
		marshall(Testsuites.class, data);
	}

	public boolean addTestsuite(final String suiteName) {
		List<Testsuite> suiteList = data.getTestsuite();

		for (Testsuite testsuite : suiteList) {
			if (testsuite.getName().equals(suiteName)) {
				if (testsuite.isDeleted()) {
					DebugLog.log("Testsuite '" + suiteName
							+ "' was tagged as deleted.");
					testsuite.setDeleted(false);

					return true;
				}

				DebugLog.log("Testsuite '" + suiteName
						+ "' is already existing.");
				return false;
			}
		}

		Testsuite newTestsuite = new Testsuite();
		newTestsuite.setName(suiteName);

		return data.getTestsuite().add(newTestsuite);
	}

	public boolean removeTestsuite(String suiteName) {
		Testsuite suite = getTestsuite(suiteName, false);

		if (suite != null) {
			DebugLog.log("Testsuite '" + suiteName
					+ "' will be tagged as deleted.");
			suite.setDeleted(true);
			return true;
		}

		return false;
	}

	public Testsuite getTestsuite(String name, boolean getDeleted) {
		for (Testsuite suite : data.getTestsuite()) {
			if (suite.getName().equals(name) && suite.isDeleted() == false) {
				return suite;
			}
		}

		return null;
	}

	public List<Testsuite> getTestsuites(boolean getDeleted) {
		if (getDeleted) {
			return data.getTestsuite();
		}
		
		List<Testsuite> suites = new ArrayList<Testsuite>();
		for (Testsuite testsuite : data.getTestsuite()) {
			if (testsuite.isDeleted() == false) {
				suites.add(testsuite);
			}
		}
		
		return suites;
	}

	public TestcaseStatus addTestcase(String suiteName, File testFile,
			TestCaseMode mode) throws Exception {

		if (!testFile.exists()) {
			throw new RuntimeException("Try to add test, but '"
					+ testFile.getAbsolutePath() + "' does not exist.");
		}

		String caseName = getCaseName(testFile);
		Testsuite suite = getTestsuite(suiteName, true);

		if (suite == null) {
			return TestcaseStatus.ERROR;
		}

		Testcase testcase = getTestcase(suiteName, caseName, true);
		TestcaseStatus resultStatus = TestcaseStatus.ERROR;
		boolean force = false;
		VersionData versionData = new VersionData();
		
		if (testcase == null) {
			// add a new test case

			testcase = new Testcase();
			testcase.setName(caseName);

			suite.getTestcase().add(testcase);
			resultStatus = TestcaseStatus.NEW;
		} else {
			// test case already existed.
			resultStatus = TestcaseStatus.UPDATE;
			versionData = testcase.getVersionData();
			
			if (testcase.isDeleted()) {
				testcase.setDeleted(false);
				
				// move to last position in list
				suite.getTestcase().remove(testcase);
				suite.getTestcase().add(testcase);
				
				force = true;
				resultStatus = TestcaseStatus.NEW;
			} else if (mode == TestCaseMode.SKIP) {
				// test case existed and wasn't deleted.
				DebugLog.log("Testcase '" + caseName + "' in testsuite '"
						+ suiteName + "' already existed.");
				return TestcaseStatus.NONE;
			}		
		}

		InputManager inputManager = new InputManager(loader, suiteName,
				caseName);
		Input input = new Input();

		input.setValue(InputManager.getContent(new FileInputStream(testFile)));
		if (inputManager.setData(input, force) == true) {
			inputManager.save();
		} else {
			resultStatus = TestcaseStatus.NONE;
		}
		
		if (versionData != null) {
			versionData.setInput(inputManager.getCurrentNr());		
			testcase.setVersionData(versionData);
		}

		return resultStatus;
	}

	public static String getCaseName(File testFile) {
		return testFile.getName().substring(0,
				testFile.getName().lastIndexOf('.'));
	}

	public boolean removeTestcase(String suiteName, String caseName) {
		Testcase testcase = getTestcase(suiteName, caseName, true);

		if (testcase != null) {
			testcase.setDeleted(true);
			return true;
		}

		return false;
	}

	public Testcase getTestcase(String suiteName, String caseName,
			boolean getDeleted) {
		Testsuite suite = getTestsuite(suiteName, getDeleted);
		if (suite != null) {
			for (Testcase testcase : suite.getTestcase()) {
				if (testcase.getName().equals(caseName)) {
					if (testcase.isDeleted()) {
						if (getDeleted) {
							return testcase;
						} else {
							return null;
						}
					}

					return testcase;
				}
			}
		}

		return null;
	}
	
	public List<Testcase> getTestcases(String suiteName, boolean deleted) {
		Testsuite suite = getTestsuite(suiteName, deleted);
		
		if (suite != null) {
			if (deleted) {
				return suite.getTestcase();
			} else {
				List<Testcase> result = new ArrayList<Testcase>();
				
				for (Testcase testcase : suite.getTestcase()) {
					if (testcase.isDeleted() == false) {
						result.add(testcase);
					}
				}
				
				return result;
			}
		}
		
		return null;
	}

	public void print() {
		List<Testsuite> suites = data.getTestsuite();

		if (suites != null) {
			DebugLog.log("Number of suites: " + suites.size());
			for (Testsuite suite : suites) {
				DebugLog.log("Test suite '" + suite.getName() + "':");

				for (Testcase tcase : suite.getTestcase()) {
					DebugLog.log("\tTest case '" + tcase.getName() + "'");
				}
			}
		}
	}

	@Override
	protected Testsuites getEmptyData() {
		return new Testsuites();
	}
}
