package rtt.ui.viewer;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;

import rtt.ui.content.IClickableContent;

public class ContentDoubleClickListener implements IDoubleClickListener {
	
	private IWorkbenchPage page;

	public ContentDoubleClickListener(IWorkbenchPage page) {
		this.page = page;
	}

	@Override
	public void doubleClick(DoubleClickEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			IStructuredSelection selection = (IStructuredSelection) event.getSelection();
			if (selection.getFirstElement() instanceof IClickableContent) {
				((IClickableContent) selection.getFirstElement()).doDoubleClick(page);
			}
		}
	}

}
