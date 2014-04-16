package rtt.ui.handlers.archive;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.osgi.service.resolver.VersionRange;
import org.eclipse.pde.core.project.IBundleProjectDescription;
import org.eclipse.pde.core.project.IBundleProjectService;
import org.eclipse.pde.core.project.IPackageImportDescription;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import rtt.ui.RttPluginUI;

/**
 * <p>This utility class is designed to gather all
 * relevant methods which are needed to set up a 
 * PDE plugin project for testing with rtt.</p>
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 */
public class PluginProjectHelper {
	
	public static final String PACKAGE_IMPORT_NAME = "rtt.annotations";

	public static class RTTPackageImportDescription implements
			IPackageImportDescription {

		@Override
		public String getName() {
			return PACKAGE_IMPORT_NAME;
		}

		@Override
		public VersionRange getVersionRange() {
			return null;
		}

		@Override
		public boolean isOptional() {
			return false;
		}
	}

	/**
	 * <p>Basically adds "rtt.annotations" to the Imported Packages
	 * of a PDE plugin project</p>
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static IJavaProject handleProject(IProject project) throws CoreException {
		BundleContext context = 
				RttPluginUI.getPlugin().getBundle().getBundleContext();
		
		if (context == null) {
			throw new RuntimeException("Bundle context was null.");
		}
		
		ServiceReference<IBundleProjectService> reference = 
				context.getServiceReference(IBundleProjectService.class);
		
		if (reference == null) {
			throw new RuntimeException("Service reference was null.");
		}
		
		IBundleProjectService service = context.getService(reference);
		if (service == null) {
			throw new RuntimeException("Project service was null.");
		}
		
		IBundleProjectDescription description = service.getDescription(project);
		if (description == null) {
			throw new RuntimeException("Project description was null.");
		}
		
		IPackageImportDescription[] packages = description.getPackageImports();
		packages = getNewPackageImports(packages);
			
		description.setPackageImports(packages);
		description.apply(new NullProgressMonitor());
		
		return JavaCore.create(project);
	}
	
	private static IPackageImportDescription[] getNewPackageImports(
			IPackageImportDescription[] presentPackages) {
		// check if any other import is present
		if (presentPackages == null) {
			return new IPackageImportDescription[] {
				new RTTPackageImportDescription()
			};
		}
		
		// check if rtt annotation import already present
		for (IPackageImportDescription packageDescription : presentPackages) {
			if (packageDescription.getName().equals(PACKAGE_IMPORT_NAME)) {
				return presentPackages;
			}
		}

		IPackageImportDescription[] newPackages = 
				new IPackageImportDescription[presentPackages.length + 1];

		System.arraycopy(presentPackages, 0, newPackages, 0, presentPackages.length);
		newPackages[presentPackages.length] = new RTTPackageImportDescription();

		return newPackages;
	}
}
