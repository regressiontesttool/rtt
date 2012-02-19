package rtt.ui.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.model.RttProject;

public class RunTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		
		monitor.beginTask("Running tests for test suite '" + suiteName + "' ...", IProgressMonitor.UNKNOWN);

		try {
			RttProject project = projectContent.getProject();
			
			try {
				project.runTests(suiteName, true);
				project.save();
				projectContent.reload(false);
			} catch (Exception e) {
				exceptions.add(e);
			}
			
		} catch (Exception e) {
			RttLog.log(new Status(Status.ERROR,
					RttPluginUI.PLUGIN_ID, e.getMessage(), e));

			exceptions.add(e);
		}

		monitor.done();
		
		if (exceptions.size() > 0) {
			throw new InterruptedException("Errors occured during test execution. Check Error Log for details.");
		}		
	}
	
	@Override
	public String getMessageTitle() {
		return "Running tests ...";
	}

}
