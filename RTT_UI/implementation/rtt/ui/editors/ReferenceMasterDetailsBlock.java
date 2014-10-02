package rtt.ui.editors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import regression.test.DocumentRoot;
import rtt.ui.RttPluginUI;

class ReferenceMasterDetailsBlock extends MasterDetailsBlock {
		
	protected ReferenceEditorPage page;
	
	public ReferenceMasterDetailsBlock(ReferenceEditorPage page) {
		this.page = page;
	}

	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		
		Section section = toolkit.createSection(parent, Section.TITLE_BAR);
		section.setText(page.getMasterSectionTitle());
		
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;	
		layout.marginHeight = 2;
		layout.marginWidth = 2;
		client.setLayout(layout);
		
		Composite treeComposite = toolkit.createComposite(client, SWT.WRAP);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		treeComposite.setLayoutData(gd);
		
		TreeColumnLayout columnLayout = new TreeColumnLayout(); 
		treeComposite.setLayout(columnLayout);
		
		Tree tree = toolkit.createTree(treeComposite, SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		TreeColumn column = new TreeColumn(tree, SWT.NONE);
		columnLayout.setColumnData(column, new ColumnWeightData(1, 100, true));
		column.setText("Node Element");
		
		TreeViewer viewer = new TreeViewer(tree);
		viewer.setContentProvider(page.getContentProvider());
		viewer.setLabelProvider(page.getLabelProvider());		
		
		toolkit.paintBordersFor(client);			
		section.setClient(client);
		
		final SectionPart sPart = new SectionPart(section);
		managedForm.addPart(sPart);		
		
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
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		sashForm.setOrientation(SWT.VERTICAL);
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
		final ScrolledForm form = managedForm.getForm();
		
		Action vaction = new Action("ver", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(true);
		vaction.setToolTipText("Vertical"); //$NON-NLS-1$
		vaction.setImageDescriptor(RttPluginUI.getImageDescriptor(
				RttPluginUI.IMG_VERTICAL));
		
		
		Action haction = new Action("hor", Action.AS_RADIO_BUTTON) { //$NON-NLS-1$
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		haction.setChecked(false);
		haction.setToolTipText("Horizontal"); //$NON-NLS-1$
		haction.setImageDescriptor(RttPluginUI.getImageDescriptor(
				RttPluginUI.IMG_HORIZONTAL));
		
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}
}