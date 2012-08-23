package rtt.ui.utils;

import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.exceptions.RTTException;
import rtt.core.utils.GenerationInformation;
import rtt.ui.model.RttProject;

public class RunTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	public RunTestRunnable(RttProject project, String suiteName) {
		this();
		setProject(project);
		setSuiteName(suiteName);
	}

	public RunTestRunnable() {
		super("Running tests ...", "Running tests for test suite");
	}

	@Override
	GenerationInformation doWork(RttProject project) throws RTTException {
		return project.runTests(suiteName, true);
	}
}
