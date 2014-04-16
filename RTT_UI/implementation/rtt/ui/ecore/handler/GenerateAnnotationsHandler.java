package rtt.ui.ecore.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;

import rtt.ui.ecore.wizard.ExportAnnotationsWizard;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.utils.RttLog;

/**
 * An {@link IHandler} for generating annotations. Basically it just 
 * searchs and opens an {@link ExportAnnotationsWizard}.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class GenerateAnnotationsHandler extends AbstractSelectionHandler implements
		IHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		
	    ISelection selection = HandlerUtil.getCurrentSelectionChecked(event);
	    if (selection instanceof IStructuredSelection) {
	    	IWorkbench workbench = PlatformUI.getWorkbench();
		    IWizardDescriptor descriptor = workbench.getExportWizardRegistry().
		    		findWizard(ExportAnnotationsWizard.ID);
		    
		    try {
				if (descriptor != null) {
					IWorkbenchWizard wizard = descriptor.createWizard();
					wizard.init(workbench, (IStructuredSelection) selection);
					
					WizardDialog dialog = new WizardDialog(
							getParentShell(event), wizard);
					dialog.open();
				}
			} catch (Exception e) {
				RttLog.log(e);
			}
	    }	    

		return null;
	}
}
