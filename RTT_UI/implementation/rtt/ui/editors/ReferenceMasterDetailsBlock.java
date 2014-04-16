package rtt.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import regression.test.Attribute;
import regression.test.DocumentRoot;
import regression.test.Node;
import regression.test.ParserOutputType;
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
		viewer.setContentProvider(page.getContentProvider());
		viewer.setLabelProvider(page.getLabelProvider());
		
		// set viewer input 
		Resource resource = page.getResource();
		if (resource.getContents().get(0) instanceof DocumentRoot) {
			DocumentRoot root = (DocumentRoot) resource.getContents().get(0);
			viewer.setInput(root);			
		} else {
			viewer.setInput(resource);
		}	
		
		viewer.expandToLevel(4);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(sPart, event.getSelection());
			}
		});
		
		page.getSite().setSelectionProvider(viewer);
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.setPageProvider(new IDetailsPageProvider() {
			
			@Override
			public Object getPageKey(Object object) {
				return object;
			}
			
			@Override
			public IDetailsPage getPage(Object key) {
				if (key instanceof EObject) {
					return new PropertyDescriptorDetailsPage();
				}
				
				return null;
			}
		});
	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub
		
	}
}