package rtt.ui.handlers.tests;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.widgets.Shell;

import rtt.core.utils.GenerationInformation;
import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.dialogs.GenerationResultsDialog;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.utils.AbstractTestRunnable;
import rtt.ui.utils.RunTestRunnable;

public class RunTestsHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell parentShell = getParentShell(event);
		ProjectContent projectContent = this.getProjectContent(event);
		TestsuiteContent suiteContent = getSelectedObject(
				TestsuiteContent.class, event);
		
		AbstractTestRunnable runnable = new RunTestRunnable(projectContent.getProject(), suiteContent.getText());
		
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
		
		projectContent.reload(new ReloadInfo(Content.TESTCASE));
		
		GenerationInformation info = runnable.getInformation();
		if (info.hasErrors()) {
			GenerationResultsDialog dialog = new GenerationResultsDialog(parentShell, info);
			dialog.open();
		}		
		
		return null;
	}
}
