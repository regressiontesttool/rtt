package rtt.ui.handlers.tests;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import rtt.core.RTTApplication;
import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.launching.ApplicationRunnable;

/**
 * This handler is used to run tests on the selected {@link Testsuite}
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class RunTestsHandler extends AbstractSelectionHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		Shell parentShell = getParentShell(event);
		
		ProjectContent projectContent = this.getProjectContent(event);
		TestsuiteContent suiteContent = getSelectedObject(
				TestsuiteContent.class, event);
		
		IRunnableWithProgress runnable = new ApplicationRunnable(projectContent.getProject(),
				suiteContent.getText(), RTTApplication.RUN);
		
		ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(
				parentShell);
		
		try {
			progressDialog.run(true, true, runnable);
			
		} catch (Throwable e) {
			if (e instanceof InvocationTargetException) {
				InvocationTargetException inno = (InvocationTargetException) e;
				e = inno.getCause();
			}			
			
			throw new ExecutionException(e.getMessage(), e);
		} finally {
			RttPluginUI.getProjectDirectory().reload(new ReloadInfo(Content.PROJECT));
		}
		
				
//		try {
//			progressDialog.run(true, false, runnable);
//			
//			GenerationInformation info = runnable.getInformation();
//			if (info != null && info.hasErrors()) {
//				GenerationResultsDialog dialog = new GenerationResultsDialog(parentShell, info);
//				dialog.open();
//			}
//		} catch (Exception e) {
//			MessageDialog.openError(parentShell,
//					runnable.getMessageTitle(), e.getMessage());
//			RttLog.log(new Status(Status.ERROR, RttPluginUI.PLUGIN_ID, e
//					.getMessage(), e));
//		} finally {
//			projectContent.reload(new ReloadInfo(Content.TESTCASE));
//		}				
		
		return null;
	}
}
