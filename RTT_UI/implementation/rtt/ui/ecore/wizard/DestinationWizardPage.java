package rtt.ui.ecore.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.wizards.IWizardDescriptor;

import rtt.ui.ecore.editor.util.AbstractSelectionChangedListener;
import rtt.ui.ecore.util.Messages;
import rtt.ui.utils.RttLog;

public class DestinationWizardPage extends WizardPage {

	private static final String TITLE = 
			"rtt.ecore.wizard.generate.destination.title";
	private static final String DESCRIPTION = 
			"rtt.ecore.wizard.generate.destination.message";
	private static final String PROJECT_LABEL = 
			"rtt.ecore.wizard.generate.destination.label.project";
	private static final String PACKAGE_LABEL = 
			"rtt.ecore.wizard.generate.destination.label.package";
	private static final String NEWBUTTON_LABEL = 
			"rtt.ecore.wizard.generate.destination.label.newPackage";
	
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;
	
	private List<IJavaProject> projects;
	private IJavaProject currentProject;
	
	private List<IPackageFragment> packages;
	private IPackageFragment currentPackage;
	
	private TableViewer packageViewer;
	private TableViewer projectViewer;
	private Button newPackageButton;	
	

	/**
	 * Create the wizard.
	 * @param generateAnntotationsWizard 
	 */
	public DestinationWizardPage() {
		super("wizardPage");
		setPageComplete(false);
		setTitle(Messages.getString(TITLE));
		setDescription(Messages.getString(DESCRIPTION));
		
		this.projects = new ArrayList<IJavaProject>();
		this.packages = new ArrayList<IPackageFragment>();
		
		loadProjects();
	}
	
	private void loadProjects() {
		projects.clear();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (root == null) {
			throw new RuntimeException("The workspace root was null.");
		}
		
		IProject[] projectArray = root.getProjects();
		for (IProject project : projectArray) {
			try {
				if (project.exists() && project.isOpen() && !project.isHidden()
						&& project.hasNature(JavaCore.NATURE_ID)) {
					projects.add(JavaCore.create(project));
				}
			} catch (CoreException e) {
				RttLog.log(e);
			}
		}
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		contentProvider = new ArrayContentProvider();
		labelProvider = new WorkbenchLabelProvider();
		
		Label projectLabel = new Label(container, SWT.NONE);
		projectLabel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 3, 1));
		projectLabel.setText(Messages.getString(PROJECT_LABEL));
		
		projectViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		projectViewer.setContentProvider(contentProvider);
		projectViewer.setLabelProvider(labelProvider);
		projectViewer.setInput(projects);
		projectViewer.addSelectionChangedListener(new AbstractSelectionChangedListener() {

			@Override
			public void handleObject(Object object) {
				if (object instanceof IJavaProject) {
					try {
						setProject((IJavaProject) object);
					} catch (JavaModelException e) {
						RttLog.log(e);
					}
				}			
			}
		});
		
		Table projectTable = projectViewer.getTable();
		projectTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		Label packageLabel = new Label(container, SWT.NONE);
		packageLabel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
		packageLabel.setText(Messages.getString(PACKAGE_LABEL));
		
		newPackageButton = new Button(container, SWT.NONE);
		newPackageButton.setEnabled(false);
		newPackageButton.setText(Messages.getString(NEWBUTTON_LABEL));
		newPackageButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWizardDescriptor descriptor = PlatformUI.getWorkbench().
						getNewWizardRegistry().findWizard("org.eclipse.jdt.ui.wizards.NewPackageCreationWizard");
				
				if (descriptor != null) {					
					try {
						INewWizard packageWizard = (INewWizard) descriptor.createWizard();
						IStructuredSelection selection = StructuredSelection.EMPTY;
						if (currentPackage != null) {
							selection = new StructuredSelection(currentPackage);
						} else if (currentProject != null) {
							selection = new StructuredSelection(currentProject);
						}
						packageWizard.init(PlatformUI.getWorkbench(), selection);
						
						WizardDialog dialog = new WizardDialog(getShell(), packageWizard);
						dialog.open();
						
						setProject(currentProject);
					} catch (Exception exception) {
						RttLog.log(exception);
					}
				}
			}
		});
		
		packageViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		packageViewer.setContentProvider(contentProvider);
		packageViewer.setLabelProvider(labelProvider);
		packageViewer.setInput(packages);
		packageViewer.addSelectionChangedListener(new AbstractSelectionChangedListener() {
			
			@Override
			public void handleObject(Object object) {
				setPageComplete(object != null);
				
				if (object instanceof IPackageFragment) {
					currentPackage = (IPackageFragment) object;
				}
			}
		});

		Table packageTable = packageViewer.getTable();
		packageTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	}
	
	protected void setProject(IJavaProject project) throws JavaModelException {
		currentProject = project;		
		newPackageButton.setEnabled(project != null);
		
		packages.clear();
		if (project != null) {
			IPackageFragment[] fragments = project.getPackageFragments();
			for (IPackageFragment packageFragment : fragments) {
				if (packageFragment.getKind() == IPackageFragmentRoot.K_SOURCE) {
					packages.add(packageFragment);
				}
			}
		}
		
		packageViewer.refresh();
		packageViewer.setSelection(StructuredSelection.EMPTY);
	}

	public IJavaProject getProject() {
		return currentProject;
	}

	public IPackageFragment getPackage() {
		return currentPackage;
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
		
		super.dispose();
	}

	public void setFile(IFile file) { }
}
