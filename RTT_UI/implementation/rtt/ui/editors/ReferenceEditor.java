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

	public static final String ID = "rtt.ui.editors.formeditor";
	public static final String LEXER_PAGE_ID = "rtt.ui.editors.lexer";
	public static final String PARSER_PAGE_ID = "rtt.ui.editors.parser";

	private OutputDataEditorInput editorInput;

	@Override
	protected void addPages() {

		try {
			ReferenceEditorPage lexPage = new ReferenceEditorPage(this, editorInput.getType(), LEXER_PAGE_ID, "Lexer");
			lexPage.preparePageInput(editorInput.getLexerOutputStream());

			ReferenceEditorPage parPage = new ReferenceEditorPage(this, editorInput.getType(), PARSER_PAGE_ID, "Parser");
			parPage.preparePageInput(editorInput.getParserOutputStream());

			addPage(lexPage);
			addPage(parPage);
		} catch (Exception e) {
			ErrorDialog.openError(getSite().getShell(), "Error",
					"Could not open editor", new Status(Status.ERROR,
							RttPluginUI.PLUGIN_ID, e.getMessage(), e));
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		if (input instanceof OutputDataEditorInput) {
			editorInput = (OutputDataEditorInput) input;
		} else {
			throw new PartInitException(new Status(Status.ERROR,
					RttPluginUI.PLUGIN_ID, "Wrong Editor input: " + input));
		}
		
		

		super.init(site, input);
		super.setPartName(input.getName());
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
