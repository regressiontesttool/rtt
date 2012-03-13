package rtt.ui.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.exceptions.RTTException;
import rtt.ui.RttLog;
import rtt.ui.content.main.ProjectContent;

public class RunTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	public RunTestRunnable(ProjectContent projectContent, String suiteName) {
		this();
		setProjectContent(projectContent);
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
			projectContent.runTest(suiteName);			
		} catch (RTTException exception) {
			RttLog.log(exception);
			throw new InterruptedException("Errors occured during test execution. Check Error Log for details.");
		}
		
		projectContent.reload();

		monitor.done();		
	}
}
