package rtt.ui.ecore.wizard;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import rtt.ui.ecore.editor.util.AbstractSelectionChangedListener;
import rtt.ui.ecore.wizard.ExportAnnotationsWizard.ListWorkbenchAdapter;
import rtt.ui.ecore.wizard.data.GenModelData;
import rtt.ui.ecore.wizard.data.ProjectData;

public class SourceWizardPage extends WizardPage implements Observer {
	
	private class SourceViewerFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {			
			
			if (element instanceof IFile) {
				return GenModelData.checkModelFile((IFile) element); 
			}
			
			return true;
		}		
	}

	private TreeViewer fileTreeViewer;
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;
	
	private GenModelData genModelData;
	private ProjectData projectData;

	/**
	 * Create the wizard.
	 * @param genModelData 
	 * @param projects 
	 */
	public SourceWizardPage(GenModelData genModelData, ProjectData projectData) {
		super("wizardPage");
		
		this.genModelData = genModelData;
		genModelData.addObserver(this);
		
		this.projectData = projectData;
		projectData.addObserver(this);
		
		setPageComplete(false);
		setTitle("Select source ...");
		setDescription("Select the generator model which contains the annotations.");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(1, false));
		
		contentProvider = new WorkbenchContentProvider();
		labelProvider = new WorkbenchLabelProvider();
		
		fileTreeViewer = new TreeViewer(container, SWT.BORDER);		
		fileTreeViewer.setContentProvider(contentProvider);
		fileTreeViewer.setLabelProvider(labelProvider);
		
		fileTreeViewer.setInput(new ListWorkbenchAdapter(projectData.getAvailableProjects()));
		fileTreeViewer.expandToLevel(2);
		
		fileTreeViewer.setFilters(new ViewerFilter[] {
				new SourceViewerFilter()
		});
		
		fileTreeViewer.addSelectionChangedListener(new AbstractSelectionChangedListener() {
			
			@Override
			public void handleObject(Object object) {
				setPageComplete(false);
				
				if (object instanceof IFile) {
					IFile file = (IFile) object;					
					if (GenModelData.checkModelFile(file)) {
						genModelData.setModelFile(file);
					}
				}
			}
		});
		
		Tree sourceTree = fileTreeViewer.getTree();		
		sourceTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}	
	
	@Override
	public void dispose() {
		genModelData.deleteObserver(this);
		genModelData = null;
		
		projectData.deleteObserver(this);
		projectData = null;
		
		if (contentProvider != null) {
			contentProvider.dispose();
			contentProvider = null;
		}
		
		if (labelProvider != null) {
			labelProvider.dispose();
			labelProvider = null;
		}
		
		if (fileTreeViewer != null) {
			fileTreeViewer = null;
		}
		
		super.dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (genModelData.equals(o)) {
			setPageComplete(genModelData.getModelFile() != null);
		}
		
		if (projectData.equals(o) && fileTreeViewer != null) {
			fileTreeViewer.refresh();
		}
	}	
}
