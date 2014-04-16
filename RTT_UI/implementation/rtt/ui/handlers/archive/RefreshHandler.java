package rtt.ui.handlers.archive;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;

public class RefreshHandler extends AbstractSelectionHandler implements
		IHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		RttPluginUI.getProjectDirectory().reload(new ReloadInfo(Content.PROJECT));		

		RttPluginUI.getProjectManager().setCurrentContent(projectContent, true);
		
		return null;
	}

}
