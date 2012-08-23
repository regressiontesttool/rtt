package rtt.ui.handlers.tests;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.InputDialog;

import rtt.core.archive.logging.Comment;
import rtt.core.archive.logging.Result;
import rtt.core.exceptions.RTTException;
import rtt.ui.RttLog;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.logging.TestResultContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class CommentHandler extends AbstractSelectionHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TestResultContent testresultContent = getSelectedObject(TestResultContent.class, event);
		ProjectContent projectContent = getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		InputDialog inputDialog = new InputDialog(
				getParentShell(event), 
				"Comment", "Enter a comment for the selected test result ...", 
				testresultContent.getComment(), 
				null);
		
		inputDialog.setBlockOnOpen(true);
		
		if (inputDialog.open() == InputDialog.OK) {
			
			try {
				Result result = testresultContent.getTestresult();
				
				Comment comment = result.getComment();
				if (comment == null) {
					comment = new Comment();
				}
				comment.setValue(inputDialog.getValue());
				result.setComment(comment);
				
				project.save();
				
				projectContent.reload(new ReloadInfo(Content.TESTRUN));
			} catch (RTTException e) {
				RttLog.log(e);
			}
		}			
		
		return null;
	}

}
