package rtt.ui.handlers.tests;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.InputDialog;

import rtt.ui.RttPluginUI;
import rtt.ui.content.logging.TestResultContent;
import rtt.ui.handlers.AbstractSelectionHandler;

public class CommentHandler extends AbstractSelectionHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TestResultContent content = getSelectedObject(TestResultContent.class, event);
		
		InputDialog inputDialog = new InputDialog(
				getParentShell(event), 
				"Comment", "Enter a comment for the selected test result ...", 
				content.getComment(), 
				null);
		
		inputDialog.setBlockOnOpen(true);
		
		if (inputDialog.open() == InputDialog.OK) {
			String comment = inputDialog.getValue();
			content.setComment(comment);
			RttPluginUI.refreshManager();
		}
			
		
		return null;
	}

}
