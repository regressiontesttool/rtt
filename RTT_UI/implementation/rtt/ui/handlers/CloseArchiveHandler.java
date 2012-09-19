package rtt.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;

import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.core.RttNature;

public class CloseArchiveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		IProject project = getSelectedObject(IProject.class, event);
		if (project != null) {
			try {
				unsetNature(project);
			} catch (CoreException e) {
				throw new ExecutionException("Could not end testing.", e);
			}
			
			RttPluginUI.getProjectDirectory().removeProject(project);
			RttPluginUI.getProjectDirectory().reload(new ReloadInfo(Content.PROJECT));
		}
		
		return null;
	}
	
	private void unsetNature(IProject project) throws CoreException {
		if (project != null && project.getDescription() != null) {
			IProjectDescription description = project.getDescription();

			String[] oldNatureIds = description.getNatureIds();
			String[] newNatureIds = new String[oldNatureIds.length - 1];

			int i = 0;
			for (String natureId : oldNatureIds) {
				if (!natureId.equals(RttNature.NATURE_ID)) {
					newNatureIds[i] = natureId;
					i++;
				}
			}

			description.setNatureIds(newNatureIds);
			project.setDescription(description, new NullProgressMonitor());
		}
	}

}
