package rtt.ui.handlers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.dialogs.ContainerGenerator;

import rtt.core.classpath.RTTClasspathContainer;
import rtt.core.manager.Manager;
import rtt.ui.RttPluginUI;
import rtt.ui.RttPreferenceStore;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.core.RttNature;
import rtt.ui.perspectives.ProjectPerspectiveFactory;

public class OpenArchiveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IJavaProject javaProject = getSelectedObject(IJavaProject.class, event);
		if (javaProject != null) {
			
			try {
				setArchive(javaProject.getProject());
				setNature(javaProject.getProject());
				setClasspath(javaProject);
			} catch (Exception e) {
				throw new ExecutionException("Could not start testing.", e);
			}
			
			RttPluginUI.getProjectDirectory().addProject(javaProject);
			RttPluginUI.getProjectDirectory().reload(new ReloadInfo(Content.PROJECT));			
		}
		
		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			workbench.showPerspective(ProjectPerspectiveFactory.ID,
					workbench.getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			throw new ExecutionException("Could not open perspective", e);
		}
		
		return null;
	}
	
	private boolean setNature(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();

		for (int i = 0; i < natures.length; ++i) {
			if (RttNature.NATURE_ID.equals(natures[i])) {
				return false;
			}
		}

		// Add the nature
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = RttNature.NATURE_ID;
		description.setNatureIds(newNatures);
		project.setDescription(description, null);

		return true;
	}

	private boolean setClasspath(IJavaProject jProject)
			throws JavaModelException {
		// Set classpath for RTT runtime engine
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		for (IClasspathEntry entry : jProject.getRawClasspath()) {
			if (entry.getPath().equals(RTTClasspathContainer.ID)) {
				return false;
			}

			entries.add(entry);
		}

		entries.add(JavaCore.newContainerEntry(RTTClasspathContainer.ID));

		jProject.setRawClasspath(
				entries.toArray(new IClasspathEntry[entries.size()]),
				new NullProgressMonitor());
		return true;
	}

	private void setArchive(IProject project) throws CoreException {
		IFolder folder = project.getFolder("./rtt/");

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
		folder.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());

		RttPreferenceStore.put(project, RttPreferenceStore.PREF_ARCHIVE_PATH,
				archive.getProjectRelativePath().toPortableString());

	}
}
