package rtt.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import rtt.ui.content.IContent;
import rtt.ui.core.ITreeItem;
import rtt.ui.core.Item;

public abstract class AbstractSelectionHandler extends AbstractHandler {
	
	@Deprecated
	protected IProject getSelectedProject(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (selection instanceof IStructuredSelection) {
			Object selectedObject = ((IStructuredSelection)selection).getFirstElement();
			
			if (selectedObject instanceof Item) {
				return ((Item) selectedObject).getProject();
			}
			
			if (selectedObject instanceof IJavaProject) {
				return ((IJavaProject) selectedObject).getProject();				
			}
		}
		
		throw new ExecutionException("No Project selected");
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T getSelectedObject(Class<T> clazz, ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
		if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
			Object sObject = ((IStructuredSelection) selection).getFirstElement();
			
			if (clazz.isInstance(sObject)) {
				return (T) sObject;
			}
			
			if (sObject instanceof ITreeItem) {
				return (T) ((ITreeItem) sObject).getObject(clazz);
			}
			
			if (sObject instanceof IContent) {
				return ((IContent) sObject).getContent(clazz);
			}
		}
		
		return null;
	}
	
	protected Shell getParentShell(ExecutionEvent event) throws ExecutionException {
		return HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
	}

	@Override
	public abstract Object execute(ExecutionEvent event) throws ExecutionException;

}
