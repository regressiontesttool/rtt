package rtt.ui.handlers.comment;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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

public class CommentAddHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		ProjectContent projectContent = getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		TestResultContent testresultContent = getSelectedObject(
				TestResultContent.class, event);
		
		if (testresultContent == null || testresultContent.getTestresult() == null) {
			return null;
		}		

		InputDialog inputDialog = new InputDialog(getParentShell(event),
				"New Comment",
				"Enter a new comment for the selected test result.", "", null);

		inputDialog.setBlockOnOpen(true);

		if (inputDialog.open() == InputDialog.OK) {
			
			String commentText = inputDialog.getValue();
			if (commentText == null || commentText.equals("")) {
				return null;
			}
			
			Result result = testresultContent.getTestresult();
			Comment comment = new Comment();
			comment.setValue(commentText);

			result.getComment().add(comment);

			try {
				project.save();

				projectContent.reload(new ReloadInfo(Content.TESTRUN));
			} catch (RTTException e) {
				RttLog.log(e);
			}
		}

		return null;
	}

}
