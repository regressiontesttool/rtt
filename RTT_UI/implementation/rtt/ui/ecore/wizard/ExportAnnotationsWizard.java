package rtt.ui.ecore.wizard;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.codegen.ecore.genmodel.GenClass;
import org.eclipse.emf.codegen.ecore.genmodel.GenModel;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.ContainerGenerator;

import rtt.ui.ecore.EcoreAnnotation;
import rtt.ui.ecore.templates.NodeAspect;
import rtt.ui.ecore.templates.ParserClassAspect;
import rtt.ui.ecore.util.GeneratorUtil;
import rtt.ui.ecore.util.Messages;
import rtt.ui.utils.RttLog;
import rtt.ui.utils.RttPluginUtil;

public class ExportAnnotationsWizard extends Wizard implements IExportWizard {
	
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

	public ExportAnnotationsWizard() {
		setWindowTitle("Generate annotations");		

		sourcePage = new SourceWizardPage();
		destinationPage = new DestinationWizardPage();
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if (selection != null && !selection.isEmpty()) {
			Object selectedObject = selection.getFirstElement();
			if (selectedObject instanceof IFile) {
				sourcePage.setGenModel((IFile) selectedObject);
			}
		}		
	}

	@Override
	public void addPages() {
		if (sourcePage.getGenModel() == null) {
			addPage(sourcePage);
		}
		
		addPage(destinationPage);
	}

	@Override
	public boolean performFinish() {
		if (sourcePage.getGenModel() == null) {
			MessageDialog.openError(getShell(), 
					Messages.getString(GENMODEL_NULL_TITLE),
					Messages.getString(GENMODEL_NULL_MESSAGE));
			return false;
		}
		
		if (destinationPage.getProject() == null) {
			MessageDialog.openError(getShell(), 
					Messages.getString(PROJECT_NULL_TITLE),
					Messages.getString(PROJECT_NULL_MESSAGE));
			return false;
		}
		
		if (destinationPage.getPackage() == null) {
			MessageDialog.openError(getShell(), 
					Messages.getString(PACKAGE_NULL_TITLE),
					Messages.getString(PACKAGE_NULL_MESSAGE));
			return false;
		}
		
		ComposedAdapterFactory adapterFactory = RttPluginUtil.createFactory();
		EditingDomain editingDomain = RttPluginUtil.createEditingDomain(adapterFactory);
	    
	    editingDomain.getResourceSet().getURIConverter().getURIMap().putAll(
				EcorePlugin.computePlatformURIMap());
	    
	    URI resourceURI = URI.createPlatformResourceURI(
	    		sourcePage.getGenModel().getFullPath().toString(), true);
	    
	    Resource resource = editingDomain.getResourceSet().getResource(resourceURI, true);
	    if (resource == null) {
	    	MessageDialog.openError(getShell(), 
	    			Messages.getString(ERROR_TITLE),
	    			Messages.getString(ERROR_MESSAGE));
	    	
	    	throw new RuntimeException("Resource was null.");
	    }
		
	    IPath destinationPath = destinationPage.getPackage().getPath();
	    if (destinationPath == null) {
	    	throw new RuntimeException("Destination path was null.");
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
		List<GenClass> parserClasses = GeneratorUtil.getAnnotatedClasses(
				genModel, EcoreAnnotation.PARSER);
		
		if (parserClasses != null && !parserClasses.isEmpty()) {
			try {
				writeParser(folder, genModel);
			} catch (CoreException e) {
				RttLog.log(e);
			}
		}
		
		List<GenClass> nodeClasses = GeneratorUtil.getAnnotatedClasses(
				genModel, EcoreAnnotation.NODE);
				
		if (nodeClasses != null && !nodeClasses.isEmpty()) {
			try {
				writeNodes(folder, genModel);
			} catch (CoreException e) {
				RttLog.log(e);
			}
		}
		
		
		return true;
	}
	
	private void writeNodes(IFolder folder, GenModel genModel) throws CoreException {
		IFile destFile = folder.getFile("NodeAspect.aj");
		
		NodeAspect aspect = new NodeAspect();
		String code = aspect.generate(new Object[] {
				genModel, destinationPage.getPackage().getElementName()
		});
		
		if (!destFile.exists()) {
			destFile.create(new ByteArrayInputStream(code.getBytes()), 
					false, new NullProgressMonitor());
		} else {
			destFile.setContents(new ByteArrayInputStream(code.getBytes()), 
					false, true, new NullProgressMonitor());
		}
	}

	private void writeParser(IFolder folder, GenModel genModel) throws CoreException {
		IFile destFile = folder.getFile("ParserAspect.aj");
		
		ParserClassAspect aspect = new ParserClassAspect();
		String code = aspect.generate(new Object[] {
				genModel, destinationPage.getPackage().getElementName()
		});
		
		if (!destFile.exists()) {
			destFile.create(new ByteArrayInputStream(code.getBytes()), 
					false, new NullProgressMonitor());
		} else {
			destFile.setContents(new ByteArrayInputStream(code.getBytes()), 
					false, true, new NullProgressMonitor());
		}
	}
}
