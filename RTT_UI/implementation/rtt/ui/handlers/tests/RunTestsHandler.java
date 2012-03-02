package rtt.ui.handlers.tests;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ProjectContent;
import rtt.ui.content.internal.data.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.utils.AbstractTestRunnable;
import rtt.ui.utils.RunTestRunnable;

public class RunTestsHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parentShell = getParentShell(event);
		ProjectContent projectContent = getSelectedObject(
				ProjectContent.class, event);
		TestsuiteContent suite = getSelectedObject(
				TestsuiteContent.class, event);
		
		AbstractTestRunnable runnable = new RunTestRunnable();
		runnable.setProjectContent(projectContent);
		runnable.setSuiteName(suite.getText());
		
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(
				parentShell);
		
		try {
			progressDialog.run(true, false, runnable);
		} catch (Exception e) {
			MessageDialog.openError(parentShell,
					runnable.getMessageTitle(), e.getMessage());
			RttLog.log(new Status(Status.ERROR, RttPluginUI.PLUGIN_ID, e
					.getMessage(), e));
		}
		
		projectContent.updateObserver();

		return null;
	}
}
