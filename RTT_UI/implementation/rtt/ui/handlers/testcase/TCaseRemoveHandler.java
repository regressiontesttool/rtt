package rtt.ui.handlers.testcase;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.ui.content.ProjectContent;
import rtt.ui.content.internal.data.TestcaseContent;
import rtt.ui.content.internal.data.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class TCaseRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getSelectedObject(ProjectContent.class, event);
		RttProject project = projectContent.getProject();
		
		TestsuiteContent suite = getSelectedObject(TestsuiteContent.class,
				event);
		TestcaseContent tcase = getSelectedObject(TestcaseContent.class, event);
		
		try {
			project.removeTestcase(suite.getText(), tcase.getText());
			project.save();
			projectContent.reload();
		} catch (Exception e) {
			throw new ExecutionException("Could not remove test case.", e);
		}

		return null;
	}

}
