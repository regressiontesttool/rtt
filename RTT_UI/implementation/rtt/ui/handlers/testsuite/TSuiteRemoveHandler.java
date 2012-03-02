package rtt.ui.handlers.testsuite;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.ui.content.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class TSuiteRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getSelectedObject(ProjectContent.class, event);
		RttProject project = projectContent.getProject();
		
		TestsuiteContent suite = getSelectedObject(TestsuiteContent.class, event);
		
		try {
			project.removeTestsuite(suite.getText());
			project.save();
			
			projectContent.reload();
		} catch (Exception e) {
			throw new ExecutionException("Could not remove test suite.", e);
		}
		
		return null;
	}
	
}
