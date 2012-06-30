package rtt.ui.editors;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.FeatureMapEntryWrapperItemProvider;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import regression.test.provider.TestItemProviderAdapterFactory;

class ReferenceMasterDetailsBlock extends MasterDetailsBlock {
		
	protected ReferenceEditorPage page;
	
	public ReferenceMasterDetailsBlock(ReferenceEditorPage page) {
		this.page = page;
	}

	@Override
	protected void createMasterPart(final IManagedForm managedForm,
			Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		section.setText(page.getMasterSectionTitle());
		
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;	
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		client.setLayout(layout);
		
		Tree tree = toolkit.createTree(client, SWT.NULL);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		tree.setLayoutData(gd);
		
		toolkit.paintBordersFor(client);			
		section.setClient(client);
		
		final SectionPart sPart = new SectionPart(section);
		managedForm.addPart(sPart);
		
		TreeViewer viewer = new TreeViewer(tree);
		prepareViewer(viewer);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(sPart, event.getSelection());
			}
		});			
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(DelegatingWrapperItemProvider.class, new PropertySourceDetailsPage());
		detailsPart.registerPage(FeatureMapEntryWrapperItemProvider.class, new PropertySourceDetailsPage());
	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub
		
	}
	
	private void prepareViewer(StructuredViewer viewer) {
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new TestItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		
		viewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		viewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		
		AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory, new BasicCommandStack(), page.getResourceSet());
		page.getSite().setSelectionProvider(viewer);
		ResourceSet resourceSet = editingDomain.getResourceSet();
		Resource documentRoot = resourceSet.getResources().get(0);
		
		viewer.setInput(documentRoot.getContents().get(0));			
	}	
}