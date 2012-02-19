package rtt.ui.utils;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.model.RttProject;

public class GenerateTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask("Generating reference data for test suite '" + suiteName + "' ...", IProgressMonitor.UNKNOWN);

		try {
			RttProject project = projectContent.getProject();
			
			exceptions.addAll(project.generateTests(suiteName));

			for (Throwable throwable : exceptions) {
				RttLog.log(new Status(Status.ERROR,
						RttPluginUI.PLUGIN_ID, throwable
								.getMessage(), throwable));
			}

			project.save();
			projectContent.reload(false);				
			
		} catch (Exception e) {
			RttLog.log(new Status(Status.ERROR,
					RttPluginUI.PLUGIN_ID, e.getMessage(), e));

			exceptions.add(e);
		}

		monitor.done();
		
		if (exceptions.size() > 0) {
			throw new InterruptedException("Errors occured during generation. Check Error Log for details.");
		}
	}

	@Override
	public String getMessageTitle() {
		return "Generating new tests ...";
	}
}
