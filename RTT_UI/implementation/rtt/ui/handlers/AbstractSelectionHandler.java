package rtt.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import rtt.ui.content.IContent;
import rtt.ui.content.main.ProjectContent;

public abstract class AbstractSelectionHandler extends AbstractHandler {
	
	@SuppressWarnings("unchecked")
	protected <T> T getSelectedObject(Class<T> clazz, ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			Object sObject = ((IStructuredSelection) selection).getFirstElement();
			
			if (clazz.isInstance(sObject)) {
				return (T) sObject;
			}
			
			if (sObject instanceof IContent) {
				return ((IContent) sObject).getContent(clazz);
			}
		}
		
		return null;
	}
	
	protected ProjectContent getProjectContent(ExecutionEvent event) throws ExecutionException {
		return getSelectedObject(ProjectContent.class, event);
	}
	
	protected Shell getParentShell(ExecutionEvent event) throws ExecutionException {
		return HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
	}

}
