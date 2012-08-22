package rtt.ui.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.exceptions.RTTException;
import rtt.core.utils.GenerationInformation;
import rtt.ui.RttLog;
import rtt.ui.model.RttProject;

public class RunTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	public RunTestRunnable(RttProject project, String suiteName) {
		this();
		setProject(project);
		setSuiteName(suiteName);
	}

	public RunTestRunnable() {
		super("Running tests ...");
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		
		monitor.beginTask("Running tests for test suite '" + suiteName + "' ...", IProgressMonitor.UNKNOWN);

		try {
			project.runTests(suiteName, true);
			project.save();
		} catch (RTTException exception) {
			RttLog.log(exception);
			throw new InterruptedException("Errors occured during test execution. Check Error Log for details.");
		}

		monitor.done();		
	}
	
	@Override
	public GenerationInformation getResults() {
		// TODO Auto-generated method stub
		return null;
	}
}
