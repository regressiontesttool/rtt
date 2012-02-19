package rtt.ui.content;

import org.eclipse.ui.IWorkbenchPage;

public interface IClickableContent extends IContent {
	
	void doDoubleClick(IWorkbenchPage currentPage);
}
