package rtt.ui.handlers.config;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

/**
 * This handler is used to set the selected {@link Configuration} active.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class ConfigSetActiveHandler extends AbstractSelectionHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		ConfigurationContent config = getSelectedObject(ConfigurationContent.class, event);
		
		if (projectContent != null) {
			RttProject project = projectContent.getProject();
			project.setActiveConfiguration(config.getConfiguration());
			
			projectContent.reload(new ReloadInfo(Content.CONFIGURATION, Content.TESTSUITE));
			RttPluginUI.getProjectManager().setCurrentContent(projectContent, true);			
		}
		
		return null;
	}

}
