package rtt.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import rtt.ui.RttPluginUI;
import rtt.ui.editors.input.OutputDataEditorInput;

public class ReferenceEditor extends FormEditor {

	public static final String ID = "rtt.ui.editors.reference";
	public static final String OUTPUT_PAGE_ID = "rtt.ui.editors.parser";

	private OutputDataEditorInput outputData;
	
	@Override
	protected void addPages() {		
		
		ReferenceEditorPage outputPage = new ReferenceEditorPage(
				this, outputData.getType(), OUTPUT_PAGE_ID, "Output");
		
		outputPage.setResource(outputData.getResource(OutputDataEditorInput.PARSER_URI));		
		try {			
			addPage(outputPage);
			
			setActivePage(OUTPUT_PAGE_ID);
		} catch (Exception e) {
			ErrorDialog.openError(getSite().getShell(), "Error",
					"Could not open editor", new Status(Status.ERROR,
							RttPluginUI.PLUGIN_ID, e.getMessage(), e));
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		
		super.init(site, input);
		super.setPartName(input.getName());

		if (input instanceof OutputDataEditorInput) {
			outputData = (OutputDataEditorInput) input;
		} else {
			throw new PartInitException(new Status(Status.ERROR,
					RttPluginUI.PLUGIN_ID, "The given data is not a correct rtt output: " + input));
		}
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
}
