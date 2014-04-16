package rtt.ui.handlers.testsuite;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

/**
 * This handler is used to remove a {@link Testsuite}.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class TSuiteRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		TestsuiteContent suiteContent = getSelectedObject(TestsuiteContent.class, event);
		
		try {
			RttProject project = projectContent.getProject();
			if (project.removeTestsuite(suiteContent.getText()) == true) {
				projectContent.reload(new ReloadInfo(Content.TESTSUITE));
				RttPluginUI.getProjectManager().setCurrentContent(projectContent, true);
			}
		} catch (Exception e) {
			throw new ExecutionException("Could not remove test suite.", e);
		}
		
		return null;
	}	
}
