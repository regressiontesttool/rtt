package rtt.ui.handlers.archive;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.widgets.Shell;

import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.ecore.util.Messages;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

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
			throw new ExecutionException("The archive file was null, but should not.");
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
}
