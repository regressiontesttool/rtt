package rtt.ui.handlers.tests;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.utils.AbstractTestRunnable;
import rtt.ui.utils.GenerateTestRunnable;

public class GenerateTestsHandler extends AbstractSelectionHandler implements
		IHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		Shell parentShell = getParentShell(event);
		
		ProjectContent projectContent = this.getProjectContent(event);
		TestsuiteContent suite = getSelectedObject(
				TestsuiteContent.class, event);
		
		AbstractTestRunnable runnable = new GenerateTestRunnable(projectContent, suite.getText());
		
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
		
		RttPluginUI.refreshListener();

		return null;
	}
}
