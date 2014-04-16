package rtt.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import rtt.ui.content.IContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.utils.RttLog;

/**
 * An abstract handler class for most of the RTT UI handlers. <br>
 * Mostly the {@link #getSelectedObject(Class, ExecutionEvent)} 
 * is used to get the current selected Object of the current selection. 
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 * @see AbstractHandler
 */
public abstract class AbstractSelectionHandler extends AbstractHandler {
	
	/**
	 * Returns an object of the current selection. <p>
	 * If selected object is type of {@link IContent} the {@link IContent#getContent(Class)} method will be used.<br>
	 * If the object is type of {@link IAdaptable} the {@link IAdaptable#getAdapter(Class)} method will be used.
	 * @param clazz the expected type of the returned object
	 * @param event the current {@link ExecutionEvent}
	 * @return an object of the excepted type
	 * @throws ExecutionException  if an exception occurred during execution.
	 */
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
			
			if (sObject instanceof IAdaptable) {
				return (T) ((IAdaptable) sObject).getAdapter(clazz);
			}
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public final Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			return doExecute(event);
		} catch(ExecutionException e) {
			handleException(event, e, "Error during command execution.");
		}
		
		return null;
	}
	
	/**
	 * The normal {@link #execute(ExecutionEvent)} method needed to be capsulated due to
	 * wrong exception handling of the eclipse platform. (See Bug 396035)
	 * @param event
	 * @return the result of the execution. Reserved for future use, must be null. (see {@link #execute(ExecutionEvent)})
	 * @throws ExecutionException  if an exception occurred during execution.
	 */
	public abstract Object doExecute(ExecutionEvent event) throws ExecutionException;

	/**
	 * Returns  the {@link ProjectContent} of the current selection.
	 * @param event
	 * @return the project content of the current selection.
	 * @throws ExecutionException  if an exception occurred during execution.
	 */
	protected ProjectContent getProjectContent(ExecutionEvent event) throws ExecutionException {
		return getSelectedObject(ProjectContent.class, event);
	}
	
	/**
	 * Returns the parent {@link Shell}.
	 * @param event
	 * @return the parent shell.
	 * @throws ExecutionException  if an exception occurred during execution.
	 */
	protected Shell getParentShell(ExecutionEvent event) throws ExecutionException {
		return HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
	}
	
	/**
	 * Due to Bug 396035 this method will create an error dialog displaying the {@link Exception#getMessage()} of the
	 * given {@link Exception}.
	 * @param event the current {@link ExecutionEvent}
	 * @param exception an {@link Exception}
	 * @param title the title of the error dialog
	 */
	protected void handleException(ExecutionEvent event, Exception exception, String title) {
		Shell parentShell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		if (parentShell == null) {
			throw new RuntimeException("Could not find current shell.");
		}
		
		Throwable throwable = exception;
		if (exception instanceof ExecutionException) {
			throwable = exception.getCause();
		}
		
		ErrorDialog.openError(parentShell, "RTT UI", title, RttLog.log(throwable));
	}

}
