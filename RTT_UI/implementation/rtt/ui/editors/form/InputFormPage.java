package rtt.ui.editors.form;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class InputFormPage extends FormPage implements IFormPage {
	
	public static final String ID = "rtt.ui.editors.inputFormPage";

	public InputFormPage(FormEditor editor) {
		super(editor, ID, "Input");
	}
	
	@Override
	public String getId() {
		System.out.println("ID: " + super.getId());
		return super.getId();
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		form.setText("Input");
	}

}
