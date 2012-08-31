package rtt.ui.handlers.testsuite;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.InputDialog;

import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class TSuiteAddHandler extends AbstractSelectionHandler {
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ProjectContent projectContent = getProjectContent(event);
		
		InputDialog inputDialog = new InputDialog(
				getParentShell(event), 
				"New Test Suite ...", "Enter a name for the new test suite.", 
				"", 
				null);
		
		inputDialog.setBlockOnOpen(true);
		
		if (inputDialog.open() == InputDialog.OK) {
			String suiteName = inputDialog.getValue();
						
			// if a test suite name was given try to create a new test suite
			if (suiteName != null && !suiteName.equals("")) {
				try {
					RttProject project = projectContent.getProject();
					
					if (project.addTestsuite(suiteName) == true) {
						project.save();						
						projectContent.reload(new ReloadInfo(Content.TESTSUITE));
					}
					
				} catch (Exception e) {
					throw new ExecutionException("Could not add test suite", e);
				}
			}
		}
		
		return null;
	}

}
