package rtt.ui.ecore.wizard;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.ContainerGenerator;
import org.eclipse.ui.model.WorkbenchAdapter;

import rtt.ui.ecore.EcoreAnnotation;
import rtt.ui.ecore.templates.NodeAspect;
import rtt.ui.ecore.templates.ParserClassAspect;
import rtt.ui.ecore.util.GeneratorUtil;
import rtt.ui.ecore.util.Messages;
import rtt.ui.ecore.wizard.data.GenModelData;
import rtt.ui.ecore.wizard.data.ProjectData;
import rtt.ui.utils.RttLog;
import rtt.ui.utils.RttPluginUtil;
import rtt.ui.viewer.ViewerUtils;

public class ExportAnnotationsWizard extends Wizard implements IExportWizard {
	
	public static class ListWorkbenchAdapter extends WorkbenchAdapter {
		
		List<?> objects;
		
		public ListWorkbenchAdapter(List<?> objects) {
			this.objects = objects;
		}
		
		@Override
		public Object[] getChildren(Object object) {
			return objects.toArray();
		}		
	}
	
	private static final String PARSER_ASPECT_FILENAME = "ParserAspect.aj";
	private static final String NODE_ASPECT_FILENAME = "NodeAspect.aj";

	public static final String ID = 
			"rtt.ui.ecore.wizard.exportAnnotations";
	
	private static final String ERROR_TITLE = 
			"rtt.ecore.wizard.generate.resource_null.title";
	private static final String ERROR_MESSAGE = 
			"rtt.ecore.wizard.generate.resource_null.message";
	
	private static final String PROJECT_NULL_TITLE = 
			"rtt.ecore.wizard.generate.project_null.title";
	private static final String PROJECT_NULL_MESSAGE = 
			"rtt.ecore.wizard.generate.project_null.message";
	
	private static final String PACKAGE_NULL_TITLE = 
			"rtt.ecore.wizard.generate.package_null.title";
	private static final String PACKAGE_NULL_MESSAGE = 
			"rtt.ecore.wizard.generate.package_null.message";

	private static final String GENMODEL_NULL_TITLE = 
			"rtt.ecore.wizard.generate.genmodel_null.title";
	private static final String GENMODEL_NULL_MESSAGE =
			"rtt.ecore.wizard.generate.genmodel_null.message";
	
	
	SourceWizardPage sourcePage;
	DestinationWizardPage destinationPage;
	
	GenModelData genModelData;
	ProjectData projectData;

	public ExportAnnotationsWizard() {
		setWindowTitle("Generate annotations");
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		initGenModelData(selection);
		initProjectData();
		initPages();
	}	

	private void initGenModelData(IStructuredSelection selection) {
		genModelData = new GenModelData();
		
		IFile selectedFile = ViewerUtils.getSelection(selection, IFile.class);
		if (selectedFile != null && GenModelData.checkModelFile(selectedFile)) {
			genModelData.setModelFile(selectedFile);
		}	
	}

	private void initProjectData() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		if (root == null) {
			throw new IllegalStateException("Workspace root was null.");
		}
		
		projectData = ProjectData.create(root.getProjects());
	}

	private void initPages() {
		sourcePage = new SourceWizardPage(genModelData, projectData);
		destinationPage = new DestinationWizardPage(genModelData, projectData);
	}

	@Override
	public void addPages() {
		if (genModelData.getModelFile() == null) {		
			addPage(sourcePage);
		}
		
		addPage(destinationPage);
	}	
	
	@Override
	public boolean performFinish() {
		if (isFeatureMissing()) {
			return false;
		}
		
		ComposedAdapterFactory adapterFactory = RttPluginUtil.createFactory();
		EditingDomain editingDomain = RttPluginUtil.createEditingDomain(adapterFactory);
	    
	    editingDomain.getResourceSet().getURIConverter().getURIMap().putAll(
				EcorePlugin.computePlatformURIMap());
	    
	    URI resourceURI = URI.createPlatformResourceURI(
	    		genModelData.getModelFile().getFullPath().toString(), true);
	    
	    Resource resource = editingDomain.getResourceSet().getResource(resourceURI, true);
	    if (resource == null) {
	    	Messages.openError(getShell(), ERROR_TITLE, ERROR_MESSAGE);
	    	
	    	throw new RuntimeException("Resource was null.");
	    }
		
	    IPath destinationPath = projectData.getTargetPackage().getPath();
	    if (destinationPath == null) {
	    	throw new RuntimeException("Target path was null.");
	    }
		
		IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(destinationPath);
		if (folder == null) {
			throw new RuntimeException("Folder was null.");
		}
		
		if (!folder.exists()) {
			ContainerGenerator gen = new ContainerGenerator(
					folder.getFullPath());
			try {
				gen.generateContainer(new NullProgressMonitor());
			} catch (CoreException e) {
				RttLog.log(e);
			}
		}
		
		GenModel genModel = GeneratorUtil.getGenModel(editingDomain.getResourceSet());
		
		if (hasAnnotatedClasses(genModel, EcoreAnnotation.PARSER)) {
			writeParser(folder, genModel);
		}
				
		if (hasAnnotatedClasses(genModel, EcoreAnnotation.NODE)) {
			writeNodes(folder, genModel);
		}		
		
		return true;
	}
	
	private boolean isFeatureMissing() {
		if (genModelData.getModelFile() == null) {
			Messages.openError(getShell(), 
					GENMODEL_NULL_TITLE, GENMODEL_NULL_MESSAGE);
			return true;
		}
		
		if (!projectData.hasTargetProject()) {
			Messages.openError(getShell(), 
					PROJECT_NULL_TITLE, PROJECT_NULL_MESSAGE);
			return true;
		}
		
		if (!projectData.hasTargetPackage()) {
			Messages.openError(getShell(), 
					PACKAGE_NULL_TITLE, PACKAGE_NULL_MESSAGE);
			return true;
		}
		
		return false;
	}
	
	private boolean hasAnnotatedClasses(GenModel genModel, EcoreAnnotation annotation) {
		List<GenClass> genClasses = GeneratorUtil.getAnnotatedClasses(genModel, annotation);
		return genClasses != null && !genClasses.isEmpty();
	}
	
	private void writeNodes(IFolder folder, GenModel genModel) {
		NodeAspect aspect = new NodeAspect();
		String code = aspect.generate(new Object[] {
				genModel, projectData.getTargetPackage().getElementName()
		});
		
		try {
			writeAspectFile(folder.getFile(NODE_ASPECT_FILENAME), code);
		} catch (CoreException e) {
			RttLog.log(e);
		}		
	}

	private void writeParser(IFolder folder, GenModel genModel) {
		ParserClassAspect aspect = new ParserClassAspect();
		String code = aspect.generate(new Object[] {
				genModel, projectData.getTargetPackage().getElementName()
		});
		
		try {
			writeAspectFile(folder.getFile(PARSER_ASPECT_FILENAME), code);
		} catch (Exception e) {
			RttLog.log(e);
		}		
	}
	
	private void writeAspectFile(IFile destination, String aspectCode) throws CoreException {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(aspectCode.getBytes());
		IProgressMonitor progressMonitor = new NullProgressMonitor();
		
		if (!destination.exists()) {
			destination.create(byteStream, false, progressMonitor);
		} else {
			destination.setContents(byteStream, false, true, progressMonitor);
		}
	}
}
