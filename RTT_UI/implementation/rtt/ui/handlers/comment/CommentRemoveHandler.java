package rtt.ui.handlers.comment;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.core.archive.logging.Comment;
import rtt.core.archive.logging.Result;
import rtt.core.exceptions.RTTException;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.logging.CommentContent;
import rtt.ui.content.logging.TestResultContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class CommentRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		CommentContent commentContent = getSelectedObject(CommentContent.class, event);
		TestResultContent testResultContent = getSelectedObject(TestResultContent.class, event);
		
		if (commentContent == null || testResultContent == null) {
			return null;
		}		
	
		Result result = testResultContent.getTestresult();
		Comment comment = commentContent.getComment();
		
		if (result == null || comment == null) {
			return null;
		}
		
		result.getComment().remove(comment);
		
		try {
			project.save();
		} catch (RTTException e) {
			throw new ExecutionException("Could not remove comment.", e);
		} finally {
			projectContent.reload(new ReloadInfo(Content.TESTRUN));
		}
		
		return null;
	}

}
