package rtt.ui.ecore.wizard;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.wizards.IWizardDescriptor;

import rtt.ui.ecore.editor.util.AbstractSelectionChangedListener;
import rtt.ui.ecore.util.Messages;
import rtt.ui.ecore.wizard.ExportAnnotationsWizard.ListWorkbenchAdapter;
import rtt.ui.ecore.wizard.data.GenModelData;
import rtt.ui.ecore.wizard.data.ProjectData;
import rtt.ui.utils.RttLog;
import rtt.ui.viewer.ViewerUtils;

public class DestinationWizardPage extends WizardPage implements Observer {
	
	public final class DestinationLabelDecorator implements ILabelDecorator {

		@Override
		public void addListener(ILabelProviderListener listener) { }

		@Override
		public void removeListener(ILabelProviderListener listener) { }


		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		
		@Override
		public Image decorateImage(Image image, Object element) {
			return image;
		}

		@Override
		public String decorateText(String text, Object element) {
			String labelText = text;
			
			if (element instanceof IProject) {
				IProject project = (IProject) element;
				if (project.equals(genModelData.getModelProject())) {
					labelText += " (parent project)";
				}
			}
			
			return labelText;
		}
		
		@Override
		public void dispose() { }		
	}

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

	private TableViewer packageViewer;
	private TableViewer projectViewer;
	private Button newPackageButton;
	
	private IContentProvider contentProvider;
	private ILabelProvider labelProvider;
	
	private GenModelData genModelData;	
	private ProjectData projectData;

	/**
	 * Create the wizard.
	 * @param genModelData 
	 * @param projectData
	 */
	public DestinationWizardPage(GenModelData genModelData, ProjectData projectData) {
		super("wizardPage");
		
		setPageComplete(false);
		setTitle(Messages.getString(TITLE));
		setDescription(Messages.getString(DESCRIPTION));
		
		this.genModelData = genModelData;
		genModelData.addObserver(this);
		
		this.projectData = projectData;
		projectData.addObserver(this);
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));
		
		contentProvider = new WorkbenchContentProvider();
		labelProvider = new DecoratingLabelProvider(new WorkbenchLabelProvider(), new DestinationLabelDecorator());
		
		Label projectLabel = new Label(container, SWT.NONE);
		projectLabel.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 3, 1));
		projectLabel.setText(Messages.getString(PROJECT_LABEL));
		
		projectViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		projectViewer.setContentProvider(contentProvider);
		projectViewer.setLabelProvider(labelProvider);
		projectViewer.setInput(new ListWorkbenchAdapter(projectData.getAvailableProjects()));
		
		projectViewer.addSelectionChangedListener(new AbstractSelectionChangedListener() {
			
			@Override
			public void handleObject(Object object) {
				if (object instanceof IProject) {
					projectData.setTargetProject((IProject) object);
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
						openWizard(descriptor);
					} catch (Exception exception) {
						RttLog.log(exception);
					}
				}
			}

			private void openWizard(IWizardDescriptor descriptor) throws CoreException {
				INewWizard packageWizard = (INewWizard) descriptor.createWizard();
				
				IPackageFragment selectedPackage = ViewerUtils.getSelection(
						packageViewer.getSelection(), IPackageFragment.class);
				IProject selectedProject = ViewerUtils.getSelection(
						projectViewer.getSelection(), IProject.class);
				
				IStructuredSelection selection = StructuredSelection.EMPTY;
				if (selectedPackage != null) {
					selection = new StructuredSelection(selectedPackage);
				} else if (selectedProject != null) {
					selection = new StructuredSelection(selectedProject);
				}
				
				packageWizard.init(PlatformUI.getWorkbench(), selection);
				
				WizardDialog dialog = new WizardDialog(getShell(), packageWizard);
				dialog.open();
				
				projectData.setTargetProject(selectedProject);
			}			
		});
		
		packageViewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION);
		packageViewer.setContentProvider(contentProvider);
		packageViewer.setLabelProvider(labelProvider);
		packageViewer.setInput(new ListWorkbenchAdapter(projectData.getAvailablePackages()));
		packageViewer.addSelectionChangedListener(new AbstractSelectionChangedListener() {
			
			@Override
			public void handleObject(Object object) {
				if (object instanceof IPackageFragment) {
					projectData.setTargetPackage((IPackageFragment) object);
				}
			}
		});

		Table packageTable = packageViewer.getTable();
		packageTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
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
		
		super.dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		projectViewer.refresh();
		packageViewer.refresh();
		
		newPackageButton.setEnabled(projectData.getTargetProject() != null);
		
		setPageComplete(projectData.hasTargetProject() && projectData.hasTargetPackage());
		
		if (genModelData.equals(o)) {
			IProject modelProject = genModelData.getModelProject();
			if (modelProject != null && projectViewer != null) {
				projectViewer.setSelection(new StructuredSelection(modelProject));
			}			
		}
	}
	
	
}
