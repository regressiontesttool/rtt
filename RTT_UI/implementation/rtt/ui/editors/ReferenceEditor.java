package rtt.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import rtt.ui.RttPluginUI;
import rtt.ui.editors.input.OutputDataEditorInput;
import rtt.ui.utils.RttPluginUtil;

public class ReferenceEditor extends FormEditor {

	public static final String ID = "rtt.ui.editors.reference";
	public static final String LEXER_PAGE_ID = "rtt.ui.editors.lexer";
	public static final String PARSER_PAGE_ID = "rtt.ui.editors.parser";

	private OutputDataEditorInput outputData;
	
	private ComposedAdapterFactory adapterFactory;
	
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;	

	@Override
	protected void addPages() {
		adapterFactory = RttPluginUtil.createFactory();
		
		contentProvider = new AdapterFactoryContentProvider(adapterFactory);
		labelProvider = new AdapterFactoryLabelProvider(adapterFactory);

		ReferenceEditorPage lexPage = new ReferenceEditorPage(this, outputData.getType(), LEXER_PAGE_ID, "Lexer");
		lexPage.setResource(outputData.getResource(OutputDataEditorInput.LEXER_URI));
		lexPage.setContentProvider(contentProvider);
		lexPage.setLabelProvider(labelProvider);
		
		ReferenceEditorPage parPage = new ReferenceEditorPage(this, outputData.getType(), PARSER_PAGE_ID, "Parser");
		parPage.setResource(outputData.getResource(OutputDataEditorInput.PARSER_URI));
		parPage.setContentProvider(contentProvider);
		parPage.setLabelProvider(labelProvider);		
		
		try {			
			addPage(lexPage);
			addPage(parPage);
			
			setActivePage(PARSER_PAGE_ID);
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
	
	@Override
	public void dispose() {
		
		if (contentProvider != null) {
			contentProvider.dispose();
			contentProvider = null;
		}
		
		if (labelProvider != null) {
			labelProvider.dispose();
			labelProvider = null;
		}
		
		if (adapterFactory != null) {
			adapterFactory.dispose();
			adapterFactory = null;
		}
		
		super.dispose();
	}

}
