package rtt.ui.content.logging;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;

import rtt.ui.content.IClickableContent;
import rtt.ui.content.IColumnableContent;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public abstract class AbstractLogContent extends AbstractContent implements
		IColumnableContent, IClickableContent, Comparable<AbstractLogContent> {

	public AbstractLogContent(IContent parent) {
		super(parent);
	}

	@Override
	public ContentIcon getIcon() {
		return ContentIcon.PLACEHOLDER;
	}
	
	@Override
	public String getText() {
		return "LogEntry";
	}
	
	@Override
	public String getText(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return getTitle();

		case 1:
			return getMessage();

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
	
	public abstract String getMessage();
	public abstract String getTitle();
	

	@Override
	public void doDoubleClick(IWorkbenchPage currentPage) {
		MessageDialog.openInformation(currentPage.getWorkbenchWindow()
				.getShell(), getTitle(), getMessage());
	}
}
