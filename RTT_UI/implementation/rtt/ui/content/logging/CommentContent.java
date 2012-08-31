package rtt.ui.content.logging;

import rtt.core.archive.logging.Comment;
import rtt.ui.content.IContent;
import rtt.ui.content.main.ContentIcon;

public class CommentContent extends AbstractLogContent {

	private Comment comment;

	public CommentContent(IContent parent, Comment comment) {
		super(parent);
		this.comment = comment;
	}

	@Override
	public ContentIcon getIcon() {
		return ContentIcon.COMMENT;
	}

	@Override
	public int compareTo(AbstractLogContent o) {
		
		if (o instanceof FailureContent) {
			return 1;
		}
		
		return 0;
	}

	@Override
	public String getMessage() {
		return comment.getValue();
	}

	@Override
	public String getTitle() {
		return "COMMENT";
	}

	public Comment getComment() {
		return comment;
	}

}
