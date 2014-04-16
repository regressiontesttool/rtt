package rtt.ui.handlers.comment;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;

import rtt.core.archive.logging.Comment;
import rtt.core.archive.logging.Result;
import rtt.core.archive.logging.Testrun;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.logging.CommentContent;
import rtt.ui.content.logging.TestResultContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

/**
 * This handler is used to edit an existing {@link Comment}.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class CommentEditHandler extends AbstractSelectionHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		CommentContent commentContent = getSelectedObject(CommentContent.class, event);
		TestResultContent testResultContent = getSelectedObject(TestResultContent.class, event);
		
		if (commentContent == null || testResultContent == null) {
			return null;
		}
		
		Testrun testrun = testResultContent.getTestrun();
		Result result = testResultContent.getTestresult();
		Comment comment = commentContent.getComment();
		
		if (result == null || comment == null) {
			return null;
		}
		
		InputDialog inputDialog = new InputDialog(getParentShell(event),
				"Modify Comment",
				"Edit the comment for the selected test result.", comment.getValue(), null);

		inputDialog.setBlockOnOpen(true);
		
		if (inputDialog.open() == Dialog.OK) {
			String newComment = inputDialog.getValue();
			
			if (newComment == null || newComment.equals("") || newComment.equals(comment.getValue())) {
				return null;
			}
			
			try {
				project.editComment(comment.getValue(), newComment, testrun, result);
			} catch (Exception e) {
				throw new ExecutionException("Could not save comment changes.", e);
			} finally {
				projectContent.reload(new ReloadInfo(Content.TESTRUN));
			}
		}
		
		return null;
	}

}
