package rtt.ui.handlers.testcase;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.ui.RttPluginUI;
import rtt.ui.content.testsuite.TestcaseContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;

public class TCaseRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TestsuiteContent suite = getSelectedObject(TestsuiteContent.class,
				event);
		TestcaseContent tcase = getSelectedObject(TestcaseContent.class, event);
		
		try {
			suite.removeTestcase(tcase.getText());
			RttPluginUI.refreshManager();
		} catch (Exception e) {
			throw new ExecutionException("Could not remove test case.", e);
		}

		return null;
	}

}
