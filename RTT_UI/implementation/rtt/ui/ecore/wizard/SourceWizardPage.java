package rtt.ui.ecore.wizard;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
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

public class SourceWizardPage extends WizardPage {
	
	public class SourceViewerFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (element instanceof IFile) {
				return isGenModel((IFile) element); 
			}
			
			return true;
		}		
	}

	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;
	
	protected IFile genModel;

	/**
	 * Create the wizard.
	 */
	public SourceWizardPage() {
		super("wizardPage");
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
		
		TreeViewer sourceTreeViewer = new TreeViewer(container, SWT.BORDER);
		sourceTreeViewer.setContentProvider(contentProvider);
		sourceTreeViewer.setLabelProvider(labelProvider);
		sourceTreeViewer.setInput(ResourcesPlugin.getWorkspace());
		sourceTreeViewer.expandToLevel(3);
		sourceTreeViewer.setFilters(new ViewerFilter[] {
				new SourceViewerFilter()
		});
		sourceTreeViewer.addSelectionChangedListener(new AbstractSelectionChangedListener() {
			
			@Override
			public void handleObject(Object object) {
				if (object instanceof IFile) {
					IFile file = (IFile) object;
					setPageComplete(isGenModel(file));
					if (isGenModel(file)) {
						genModel = file;
					}
				}
			}
		});
		
		Tree sourceTree = sourceTreeViewer.getTree();		
		sourceTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	private boolean isGenModel(IFile file) {
		if (file != null) {
			String extension = file.getFileExtension();
			if (extension != null) {
				return extension.equalsIgnoreCase("genmodel");
			}
		}
		
		return false;
	}

	public boolean setGenModel(IFile file) {
		if (isGenModel(file)) {
			this.genModel = file;
			return true;
		}
		
		return false;		
	}
	
	public IFile getGenModel() {
		return genModel;
	}

}
