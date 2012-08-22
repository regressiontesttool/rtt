package rtt.ui.handlers.testsuite;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class TSuiteRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		TestsuiteContent suiteContent = getSelectedObject(TestsuiteContent.class, event);
		
		try {
			RttProject project = projectContent.getProject();
			if (project.removeTestsuite(suiteContent.getText()) == true) {
				project.save();
				projectContent.reload(new ReloadInfo(Content.TESTSUITE));
			}
		} catch (Exception e) {
			throw new ExecutionException("Could not remove test suite.", e);
		}
		
		return null;
	}	
}
