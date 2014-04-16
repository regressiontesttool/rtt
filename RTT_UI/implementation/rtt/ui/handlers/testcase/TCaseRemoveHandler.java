package rtt.ui.handlers.testcase;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.core.archive.testsuite.Testcase;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestcaseContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

/**
 * This handler is used to remove a {@link Testcase}.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class TCaseRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		TestcaseContent tcaseContent = getSelectedObject(TestcaseContent.class, event);
		
		ProjectContent projectContent = getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		try {
			project.removeTestcase(tcaseContent.getSuiteName(), tcaseContent.getCaseName());
			
			projectContent.reload(new ReloadInfo(Content.TESTCASE));			
		} catch (Exception e) {
			throw new ExecutionException("Could not remove test case.", e);
		}

		return null;
	}

}
