package rtt.ui.handlers.config;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.ui.RttPluginUI;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.handlers.AbstractSelectionHandler;

public class ConfigSetActiveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		ConfigurationContent config = getSelectedObject(ConfigurationContent.class, event);
		
		if (projectContent != null) {
			projectContent.setConfigActive(config);
		}
		
		RttPluginUI.refreshManager();
		
		return null;
	}

}
