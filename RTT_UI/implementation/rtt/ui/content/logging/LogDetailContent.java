package rtt.ui.content.logging;

import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.core.archive.logging.Detail;
import rtt.ui.content.IColumnableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public class LogDetailContent extends AbstractContent implements IColumnableContent {
	
	private Detail detail;

	public LogDetailContent(IContent parent, Detail detail) {
		super(parent);
		this.detail = detail;
	}
	
	public Integer getPriority() {
		return detail.getPriority();
	}

	@Override
	public String getText(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "DETAIL";

		case 1:
			return detail.getMsg() + " " + detail.getSuffix();

		default:
			return "";
		}	
	}

	@Override
	public Image getImage(int columnIndex, LocalResourceManager resourceManager) {
		if (columnIndex == 0) {
			return getImage(resourceManager);
		}
		
		return null;
	}

	@Override
	public String getText() {
		return "DetailEntry";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.DETAIL;
	}

}
