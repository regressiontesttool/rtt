package rtt.ui.handlers.testcase;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestcaseContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class TCaseRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TestcaseContent tcaseContent = getSelectedObject(TestcaseContent.class, event);
		
		ProjectContent projectContent = getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		try {
			project.removeTestcase(tcaseContent.getSuiteName(), tcaseContent.getCaseName());
			project.save();
			
			projectContent.reload(new ReloadInfo(Content.TESTCASE));			
		} catch (Exception e) {
			throw new ExecutionException("Could not remove test case.", e);
		}

		return null;
	}

}
