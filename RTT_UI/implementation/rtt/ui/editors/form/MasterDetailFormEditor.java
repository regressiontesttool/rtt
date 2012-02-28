package rtt.ui.editors.form;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import rtt.ui.RttPluginUI;
import rtt.ui.editors.input.LexerOutputDetailInput;
import rtt.ui.editors.input.ParserOutputDetailInput;
import rtt.ui.editors.input.RTTEditorInput;

public class MasterDetailFormEditor extends FormEditor {

	public static final String ID = "rtt.ui.editors.formeditor";
	public static final String LEXER_ID = "rtt.ui.editors.lexer";
	public static final String PARSER_ID = "rtt.ui.editors.parser";

	private RTTEditorInput editorInput;

	public MasterDetailFormEditor() {

	}

	@Override
	protected void addPages() {

		try {
			RTTFormPage lexPage = new RTTFormPage(this, LEXER_ID, "Lexer");
			lexPage.input = new LexerOutputDetailInput(LEXER_ID,
					editorInput.getLexerOutput());

			RTTFormPage parPage = new RTTFormPage(this, PARSER_ID, "Parser");
			parPage.input = new ParserOutputDetailInput(PARSER_ID,
					editorInput.getParserOuput());

			addPage(lexPage);
			addPage(parPage);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error",
					"Could not open editor", new Status(Status.ERROR,
							RttPluginUI.PLUGIN_ID, e.getMessage(), e));
		}
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		if (input instanceof RTTEditorInput) {
			editorInput = (RTTEditorInput) input;
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
