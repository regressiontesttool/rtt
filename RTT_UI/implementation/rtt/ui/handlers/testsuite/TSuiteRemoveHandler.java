package rtt.ui.handlers.testsuite;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.ui.RttPluginUI;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;

public class TSuiteRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = this.getProjectContent(event);
		TestsuiteContent suite = getSelectedObject(TestsuiteContent.class, event);
		
		try {
			projectContent.removeTestsuite(suite.getText());
			RttPluginUI.refreshManager();
		} catch (Exception e) {
			throw new ExecutionException("Could not remove test suite.", e);
		}
		
		return null;
	}
	
}
