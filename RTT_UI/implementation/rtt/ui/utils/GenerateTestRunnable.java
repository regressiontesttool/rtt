package rtt.ui.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.exceptions.RTTException;
import rtt.ui.RttLog;
import rtt.ui.content.main.ProjectContent;

public class GenerateTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	public GenerateTestRunnable(ProjectContent projectContent, String suiteName) {
		this();
		setProjectContent(projectContent);
		setSuiteName(suiteName);
	}
	
	public GenerateTestRunnable() {
		super("Generating new tests ...");
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask("Generating reference data for test suite '" + suiteName + "' ...", IProgressMonitor.UNKNOWN);

		boolean exceptionOccured = false;
		try {
			List<Throwable> exceptions = projectContent.generateTest(suiteName);
			if (exceptions != null && !exceptions.isEmpty()) {
				for (Throwable throwable : exceptions) {
					RttLog.log(throwable);
				}
				exceptionOccured = true;				
			}
		} catch (RTTException e) {
			RttLog.log(e);
			exceptionOccured = true;
		}
		
		if (exceptionOccured) {
			throw new InterruptedException("Errors occured during generation. Check Error Log for details.");
		}
		
		monitor.done();
	}
}
