package rtt.ui.editors.form;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import rtt.ui.content.ContentTreeViewer;
import rtt.ui.editors.input.IDetailInput;

public class RTTFormPage extends FormPage {
	
	protected class RTTDetailsBlock extends MasterDetailsBlock {
		
		protected RTTFormPage page;
		
		public RTTDetailsBlock(RTTFormPage page) {
			this.page = page;
		}

		@Override
		protected void createMasterPart(final IManagedForm managedForm,
				Composite parent) {
			FormToolkit toolkit = managedForm.getToolkit();
			
			Section section = toolkit.createSection(parent, Section.TITLE_BAR);
			section.setText(page.getDetailInput().getMasterSectionTitle());
			
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
			
			ContentTreeViewer viewer = new ContentTreeViewer(tree, getSite().getPage());
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {
				
				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					managedForm.fireSelectionChanged(sPart, event.getSelection());
				}
			});
			viewer.setInput(input.getRoot());
			
//			TreeViewerColumn treeColumn = new TreeViewerColumn(viewer, SWT.LEFT);
//			treeColumn.setLabelProvider(new ColumnLabelProvider() {
//				@Override
//				public String getText(Object element) {
//					return ((ITreeItem) element).getName();
//				}
//			});
//			treeColumn.getColumn().setText("classname");
//			treeColumn.getColumn().setWidth(80);
			
			
		}

		@Override
		protected void registerPages(DetailsPart detailsPart) {
			for (Class<?> clazz : getDetailInput().getDetailClasses()) {
				detailsPart.registerPage(clazz, new RTTDetailsPage(getDetailInput()));
			}
			
			
		}

		@Override
		protected void createToolBarActions(IManagedForm managedForm) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	protected FormToolkit toolkit;
	protected MasterDetailsBlock block;
	
	protected String title;
	protected IDetailInput input;
	
	
	public RTTFormPage(FormEditor editor, String id, String tabTitle) {
		super(editor, id, tabTitle);
		block = new RTTDetailsBlock(this);
	}
	
	@Override
	protected void createFormContent(IManagedForm managedForm) {		
		final ScrolledForm form = managedForm.getForm();
		form.setText(getPageTitle());
		block.createContent(managedForm);
	}

	protected String getPageTitle() {
		return title;
	}
	
	protected IDetailInput getDetailInput() {
		return input;		
	}
}
