package rtt.ui.core;

import java.io.File;
import java.util.List;

import rtt.core.exceptions.RTTException;
import rtt.core.utils.ConfigParameter;
import rtt.core.utils.ConfigurationUtils;
import rtt.core.utils.TestCaseUtils;
import rtt.core.utils.TestSuiteUtils;
import rtt.core.utils.TestUtils;
import rtt.ui.RttPreferenceStore;
import rtt.ui.core.archive.IConfiguration;
import rtt.ui.core.archive.ITestCase;
import rtt.ui.core.archive.ITestSuite;


public class ModelController {
	
	public static void addConfiguration(IRttProject project, ConfigParameter params) throws RTTException {
		ConfigurationUtils.addConfiguration(project.getArchiveFile(), params);
		
		if (params.defaultConfig || project.getConfigurations().isEmpty()) {
			RttPreferenceStore.put(project.getWorkspaceProject(), RttPreferenceStore.PREF_CONFIG_DEFAULT, params.configName);
		}		
		
		ProjectRegistry.fireModelChanged(project);
	}
	
	public static void setConfigurationActive(IRttProject project, IConfiguration config) throws RTTException {
		project.setActiveConfiguration(config);		
		ProjectRegistry.fireModelChanged(project);
	}
	
	public static void addTestsuite(IRttProject project, String suiteName) throws RTTException {
		TestSuiteUtils.addTestSuite(project.getArchiveFile(), suiteName);
		ProjectRegistry.fireModelChanged(project);
	}
	
	public static void removeTestSuite(IRttProject project, ITestSuite testSuite) throws RTTException {
		TestSuiteUtils.removeTestSuite(project.getArchiveFile(), testSuite.getName());
		ProjectRegistry.fireModelChanged(project);
	}
	
	public static void addTestCase(IRttProject project, ITestSuite testSuite, File file) throws RTTException {
		TestCaseUtils.addTestCase(project.getArchiveFile(), testSuite.getName(), file);
		ProjectRegistry.fireModelChanged(project);
	}
	
	public static void removeTestCase(IRttProject project, ITestSuite testSuite, ITestCase testCase) throws RTTException {
		TestCaseUtils.removeTestCase(project.getArchiveFile(), testSuite.getName(), testCase.getName());
		ProjectRegistry.fireModelChanged(project);
	}

	public static List<Throwable> generateReferenceData(IRttProject project, ITestSuite testSuite) throws RTTException {
		// CHRISTIAN todo.
		List<Throwable> exceptions = TestUtils.generateReferenceData(project.getArchiveFile(), testSuite.getName(), "");
		ProjectRegistry.fireModelChanged(project);
		
		return exceptions;
	}

	public static void runTests(IRttProject project, ITestSuite suite, boolean matching) throws RTTException {
		TestUtils.runTests(project.getArchiveFile(), suite.getName(), matching);
	}
}
