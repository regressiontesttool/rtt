package rtt.ui.handlers.testsuite;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;

import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class TSuiteAddHandler extends AbstractSelectionHandler {
	
	//CHRISTIAN
	private final String initialValue = "testSuite";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ProjectContent projectContent = this.getProjectContent(event);
		
		InputDialog inputDialog = new InputDialog(
				getParentShell(event), 
				"Add test suite", "Please enter a name for the new test suite", 
				initialValue, 
				null);
		
		inputDialog.setBlockOnOpen(true);
		
		if (inputDialog.open() == InputDialog.OK) {
			String suiteName = inputDialog.getValue();
			if (suiteName != null && !suiteName.equals("")) {
				try {
					projectContent.addTestsuite(suiteName);
				} catch (Exception e) {
					throw new ExecutionException("Could not add test suite", e);
				}
			}
		}
		
		return null;
	}

}
