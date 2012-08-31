package rtt.ui.content.logging;

import rtt.core.archive.logging.Detail;
import rtt.ui.content.IContent;
import rtt.ui.content.main.ContentIcon;

public class LogDetailContent extends AbstractLogContent {
	
	private Detail detail;

	public LogDetailContent(IContent parent, Detail detail) {
		super(parent);
		this.detail = detail;
	}
	
	public Integer getPriority() {
		return detail.getPriority();
	}
	
	@Override
	public String getMessage() {
		return detail.getMsg() + " " + detail.getSuffix();
	}
	
	@Override
	public String getTitle() {
		return "DETAIL";
	}

	@Override
	public ContentIcon getIcon() {
		return ContentIcon.DETAIL;
	}

	@Override
	public int compareTo(AbstractLogContent o) {
		if (o instanceof LogDetailContent) {
			LogDetailContent detail = (LogDetailContent) o;
			return this.getPriority().compareTo(detail.getPriority());
		}
		
		return 0;
	}

}
