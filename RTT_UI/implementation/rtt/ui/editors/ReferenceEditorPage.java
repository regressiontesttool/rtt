package rtt.ui.editors;


import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import rtt.core.manager.data.history.OutputDataManager.OutputDataType;
import rtt.ui.utils.RttPluginUtil;

public class ReferenceEditorPage extends FormPage {
	
	protected FormToolkit toolkit;
	protected MasterDetailsBlock block;
	
	protected String title;
	protected Resource resource;
	
	private ITreeContentProvider contentProvider;
	private ITableLabelProvider labelProvider;
	
	private ComposedAdapterFactory adapterFactory;
	
	
	public ReferenceEditorPage(FormEditor editor, OutputDataType type, String id, String tabTitle) {
		super(editor, id, tabTitle);
		this.title = type.getText() + " Output Data";
		
		adapterFactory = RttPluginUtil.createFactory();
		contentProvider = new AdapterFactoryContentProvider(adapterFactory); 
		labelProvider = new AdapterFactoryLabelProvider(adapterFactory);
		
		block = createMasterDetails();
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	private MasterDetailsBlock createMasterDetails() {
		return new ReferenceMasterDetailsBlock(this);
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

	public ITreeContentProvider getContentProvider() {
		return contentProvider;
	}

	public ITableLabelProvider getLabelProvider() {
		return labelProvider;
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
