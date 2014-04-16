package rtt.ui.handlers.tests;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;

import rtt.core.RTTApplication;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.launching.ApplicationRunnable;
import rtt.ui.model.RttProject;

/**
 * The handler for the "Generate Reference Results ..." command.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class GenerateTestsHandler extends AbstractSelectionHandler implements
		IHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		Shell parentShell = getParentShell(event);
		
		ProjectContent projectContent = this.getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		TestsuiteContent suiteContent = getSelectedObject(
				TestsuiteContent.class, event);
		
		IRunnableWithProgress runnable = new ApplicationRunnable(project, 
				suiteContent.getText(), RTTApplication.GENERATE);
		
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

		return null;
	}

	
}
