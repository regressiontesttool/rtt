package rtt.ui.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.dialogs.ContainerGenerator;

import rtt.core.manager.Manager;
import rtt.ui.RttPluginUI;
import rtt.ui.RttPreferenceStore;

public class RttNature implements IProjectNature {

	public static final String NATURE_ID = "rtt.ui.projectnature";
	private IProject project;

	@Override
	public void configure() throws CoreException {
		generateArchive();
	}

	private void generateArchive() throws CoreException {
		IFolder folder = project.getFolder("./.rtt/");

		if (!folder.exists()) {
			ContainerGenerator gen = new ContainerGenerator(
					folder.getFullPath());
			gen.generateContainer(new NullProgressMonitor());
		}

		IFile archive = folder.getFile("./archive.zip");
		if (!archive.exists()) {
			Manager m = new Manager(archive.getLocation().toFile(), true);
			try {
				m.createArchive();
				m.saveArchive(archive.getLocation().toFile());
			} catch (Exception e) {
				throw new CoreException(new Status(IStatus.ERROR,
						RttPluginUI.PLUGIN_ID,
						"Could not create archive file: " + archive, e));
			}
		}

		RttPreferenceStore.put(project, RttPreferenceStore.PREF_ARCHIVE_PATH,
				archive.getLocation().toOSString());

	}

	@Override
	public void deconfigure() throws CoreException {

	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public void setProject(IProject project) {
		this.project = project;
	}

}
