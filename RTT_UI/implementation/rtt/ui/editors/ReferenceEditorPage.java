package rtt.ui.editors;


import java.io.IOException;
import java.io.InputStream;

import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import rtt.ui.RttPluginUtil;

public class ReferenceEditorPage extends FormPage {
	
	protected FormToolkit toolkit;
	protected MasterDetailsBlock block;
	
	protected String title;
	protected ResourceSet refResourceSet;
	
	
	public ReferenceEditorPage(FormEditor editor, String id, String tabTitle) {
		super(editor, id, tabTitle);
		this.title = tabTitle;
		block = new ReferenceMasterDetailsBlock(this);
	}
	
	public void preparePageInput(InputStream pageInput) throws IOException {
		refResourceSet = RttPluginUtil.createResourceSet(pageInput, "reference.rtt");
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {		
		final ScrolledForm form = managedForm.getForm();
		form.setText("Reference data of the " + title);
		block.createContent(managedForm);
	}

	public String getMasterSectionTitle() {
		return title;
	}

	public ResourceSet getResourceSet() {
		return refResourceSet;
	}
}
