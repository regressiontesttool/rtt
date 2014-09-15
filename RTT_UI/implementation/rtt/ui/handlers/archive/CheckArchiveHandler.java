package rtt.ui.handlers.archive;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Shell;

import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;
import rtt.ui.utils.Messages;
import rtt.ui.utils.RttPreferenceStore;

public class CheckArchiveHandler extends AbstractSelectionHandler implements IHandler {

	private static final String RECREATE_ARCHIVE = "rtt.ui.commands.archive.check.recreate.message";

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		if (projectContent == null) {
			throw new ExecutionException("Project content was null.");
		}

		Shell parent = getParentShell(event);
		
		RttProject rttProject = projectContent.getProject();
		IFile archiveFile = rttProject.getArchiveFile();
		
		if (archiveFile == null) {
			IProject project = rttProject.getIProject();
			
			String prefArchiveLocation = RttPreferenceStore.get(project,
					RttPreferenceStore.PREF_ARCHIVE_PATH, 
					null);
			if (prefArchiveLocation == null || prefArchiveLocation.equals("")) {
				try {
					archiveFile = setRTTArchive(project);
				} catch (CoreException e) {
					return new ExecutionException("Could not create archive.", e);
				}
			} else {
				archiveFile = project.getFile(prefArchiveLocation);
			}
		}
		
		if (!archiveFile.exists() && Messages.openQuestion(parent, RECREATE_ARCHIVE)) {
			try {
				rttProject.createArchive(archiveFile);
			} catch (CoreException e) {
				throw new ExecutionException("Could not create archive", e);
			}
		}
		
		RttPluginUI.getProjectDirectory().reload(new ReloadInfo(Content.PROJECT));
		RttPluginUI.getProjectManager().setCurrentContent(projectContent, true);
	
		return null;
	}
	
	public IFile setRTTArchive(IProject project) throws CoreException {
		IFile archiveFile = project.getFile("./rtt/archive.zip");
		IPath path = archiveFile.getProjectRelativePath();
		
		RttPreferenceStore.put(project, RttPreferenceStore.PREF_ARCHIVE_PATH,
				path.toPortableString());
		
		return archiveFile;
	}
}
