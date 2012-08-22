package rtt.ui.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.exceptions.RTTException;
import rtt.core.utils.GenerationInformation;
import rtt.ui.RttLog;
import rtt.ui.model.RttProject;

public class GenerateTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	private GenerationInformation results;

	public GenerateTestRunnable(RttProject project, String suiteName) {
		this();
		setProject(project);
		setSuiteName(suiteName);
	}
	
	public GenerateTestRunnable() {
		super("Generating new tests ...");
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask("Generating reference data for test suite '" + suiteName + "' ...", IProgressMonitor.UNKNOWN);

		try {
			results = project.generateTests(suiteName);		
			project.save();
		} catch (RTTException e) {
			RttLog.log(e);
			throw new InterruptedException("Generation exception: " + e.getMessage());
		}
		
		monitor.done();
	}
	
	@Override
	public GenerationInformation getResults() {
		return results;
	}
}
