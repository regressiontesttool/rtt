package rtt.ui.utils;

import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.exceptions.RTTException;
import rtt.core.utils.GenerationInformation;
import rtt.ui.model.RttProject;

public class GenerateTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	public GenerateTestRunnable(RttProject project, String suiteName) {
		this();
		setProject(project);
		setSuiteName(suiteName);
	}
	
	public GenerateTestRunnable() {
		super("Generating new tests ...", "Generating reference data for test suite ");
	}
	
	@Override
	GenerationInformation doWork(RttProject project) throws RTTException {
		return project.generateTests(suiteName);
	}
	
}
