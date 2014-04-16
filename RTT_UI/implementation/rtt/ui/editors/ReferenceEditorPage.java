package rtt.ui.editors;


import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import rtt.core.manager.data.history.OutputDataManager.OutputDataType;

public class ReferenceEditorPage extends FormPage {
	
	protected FormToolkit toolkit;
	protected MasterDetailsBlock block;
	
	protected String title;
	protected Resource resource;
	
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;
	
	
	public ReferenceEditorPage(FormEditor editor, OutputDataType type, String id, String tabTitle) {
		super(editor, id, tabTitle);
		this.title = type.getText() + " Data of the " + tabTitle;
		block = new ReferenceMasterDetailsBlock(this);
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {		
		final ScrolledForm form = managedForm.getForm();
		form.setText(title);
		block.createContent(managedForm);
	}

	public String getMasterSectionTitle() {
		return title;
	}

	public IContentProvider getContentProvider() {
		return contentProvider;
	}
	
	public void setContentProvider(IContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public ILabelProvider getLabelProvider() {
		return labelProvider;
	}
	
	public void setLabelProvider(ILabelProvider labelProvider) {
		this.labelProvider = labelProvider;
	}
}
