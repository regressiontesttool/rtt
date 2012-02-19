package rtt.ui.handlers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
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
import rtt.ui.RttPreferenceStore;
import rtt.ui.core.ProjectFinder;
import rtt.ui.perspectives.ProjectPerspectiveFactory;

public class InitArchiveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// CHRISTIAN create Wizard

		IProject project = getSelectedProject(event);

		IJavaProject jProject = getSelectedObject(IJavaProject.class, event);
		if (jProject != null) {
			try {
				List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
				for (IClasspathEntry entry : jProject.getRawClasspath()) {
					entries.add(entry);
				}
				
				entries.add(JavaCore.newContainerEntry(RTTClasspathContainer.ID));

				jProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Could not find Project.");
		}

		IFolder archiveFolder = project.getProject().getFolder("./.rtt/");
		if (!archiveFolder.exists()) {
			try {
				ContainerGenerator gen = new ContainerGenerator(
						archiveFolder.getFullPath());
				gen.generateContainer(new NullProgressMonitor());
			} catch (CoreException e) {
				throw new ExecutionException(
						"Archive folder can not be created.", e);
			}
		}

		IFile archiveFile = archiveFolder.getFile("./archive.zip");
		if (!archiveFile.exists()) {
			try {
				File path = new File(archiveFile.getLocation().toOSString());
				Manager m = new Manager(path, true);
				m.createArchive();
			} catch (Exception e) {
				throw new ExecutionException(
						"Archive file can not be created. ", e);
			}
		}

		RttPreferenceStore.put(project, RttPreferenceStore.PREF_ARCHIVE_PATH,
				archiveFile.getLocation().toOSString());
		RttPreferenceStore.putBoolean(project,
				RttPreferenceStore.PREF_ARCHIVE_EXIST, true);

		// CHRISTIAN reload projects !!!
		ProjectFinder.loadProjects();
		ProjectFinder.fireChangeEvent();

		try {
			IWorkbench workbench = PlatformUI.getWorkbench();
			workbench.showPerspective(ProjectPerspectiveFactory.ID,
					workbench.getActiveWorkbenchWindow());
		} catch (WorkbenchException e) {
			throw new ExecutionException("Could not open perspective", e);
		}

		// MessageDialog.openInformation(
		// HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell(),
		// "Archive",
		// "Archive created"
		// );

		return null;
	}

}
