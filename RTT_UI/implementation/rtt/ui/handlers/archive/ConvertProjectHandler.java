package rtt.ui.handlers.archive;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.pde.core.project.IBundleProjectDescription;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;

import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.core.RttNature;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.perspectives.ProjectPerspectiveFactory;
import rtt.ui.utils.Messages;
import rtt.ui.utils.RttPreferenceStore;

/**
 * This handler is used to open the archive and convert the current selected project.<p>
 * If the archive does not exists, then it will be created. <br>
 * If the selected project does not contain the {@link RttNature}, then it will be set.<br>
 * If the selected project does not have the RTT classpath, then it will be added.
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class ConvertProjectHandler extends AbstractSelectionHandler {

	private static final String PERSPECTIVE_TITLE = "rtt.ui.handler.convert_project.perspective.title";
	private static final String PERSPECTIVE_MESSAGE = "rtt.ui.handler.convert_project.perspective.message";
	

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		
		try {
			IProject project = getSelectedObject(IProject.class, event);
			if (project != null) {
				
				IJavaProject javaProject = null;
				if (project.hasNature(IBundleProjectDescription.PLUGIN_NATURE)) {
					javaProject = PluginProjectHelper.handleProject(project);
				} else if (project.hasNature(JavaCore.NATURE_ID)) {
					javaProject = JavaProjectHelper.handleProject(project);
				}
				
				if (javaProject != null) {
					setRTTArchive(project);
					setRTTNature(project);
					
					RttPluginUI.getProjectDirectory().addProject(javaProject);
					RttPluginUI.getProjectDirectory().reload(
							new ReloadInfo(Content.PROJECT));
				}
			}
		} catch (Exception e) {
			throw new ExecutionException("Could not start testing.", e);
		}
		
		boolean openPerspective = Messages.openQuestion(getParentShell(event), 
				PERSPECTIVE_TITLE, PERSPECTIVE_MESSAGE);
		
		if (openPerspective) {
			try {
				IWorkbench workbench = PlatformUI.getWorkbench();
				workbench.showPerspective(ProjectPerspectiveFactory.ID,
						workbench.getActiveWorkbenchWindow());
			} catch (WorkbenchException e) {
				throw new ExecutionException("Could not open perspective", e);
			}
		}		
		
		return null;
	}

	private boolean setRTTNature(IProject project) throws CoreException {
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

	public void setRTTArchive(IProject project) throws CoreException {
		IFile archiveFile = project.getFile("./rtt/archive.zip");
		IPath path = archiveFile.getProjectRelativePath();
		
		RttPreferenceStore.put(project, RttPreferenceStore.PREF_ARCHIVE_PATH,
				path.toPortableString());

	}
	
	
}
