package rtt.ui.editors.form;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import rtt.ui.RttPluginUI;
import rtt.ui.editors.input.IDetailInput;
import rtt.ui.editors.input.ITestCaseEditorInput;
import rtt.ui.editors.input.LexerOutputDetailInput;
import rtt.ui.editors.input.ParserOutputDetailInput;

public class MasterDetailFormEditor extends FormEditor {
	
	public class ParserPage extends MasterDetailFormPage {
		
		public static final String ID = "rtt.ui.editors.parser";

		public ParserPage(FormEditor editor) {
			super(editor, ID, "ParserOuput");
		}

		@Override
		protected String getPageTitle() {
			return "ParserOutput";
		}

		@Override
		protected IDetailInput getDetailInput() {
			return new ParserOutputDetailInput(ID, testCaseInput.getParserOuput());
		}
		
	}
	
	public class LexerPage extends MasterDetailFormPage {
		public static final String ID = "rtt.ui.editors.lexer";
		
		public LexerPage(FormEditor editor) {
			super(editor, ID, "LexerOutput");
		}
		
		@Override
		protected String getPageTitle() {
			return "LexerOutput";
		}
		
		@Override
		protected IDetailInput getDetailInput() {
			return new LexerOutputDetailInput(ID, testCaseInput.getLexerOutput());
		}
	}
	
	
	public static final String ID = "rtt.ui.editors.formeditor";

	public MasterDetailFormEditor() {	}
	protected ITestCaseEditorInput testCaseInput;

	@Override
	protected void addPages() {
		
		try {
//			addPage(new InputFormPage(this));
			addPage(new LexerPage(this));
			addPage(new ParserPage(this));
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error", "Could not open editor", 
					new Status(Status.ERROR, RttPluginUI.PLUGIN_ID, e.getMessage(),e));
		}
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {

		if (!(input instanceof ITestCaseEditorInput)) {
			throw new PartInitException(new Status(Status.ERROR, RttPluginUI.PLUGIN_ID, "Wrong Editor input: " + input));
		}
		
		this.testCaseInput = (ITestCaseEditorInput) input;
		super.init(site, input);
		this.setPartName(testCaseInput.getName());
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
