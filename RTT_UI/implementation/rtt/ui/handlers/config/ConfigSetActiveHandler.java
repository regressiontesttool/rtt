package rtt.ui.handlers.config;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.ui.RttPluginUI;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.handlers.AbstractSelectionHandler;

public class ConfigSetActiveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ConfigurationContent config = getSelectedObject(ConfigurationContent.class, event);
		config.changeActive();
		RttPluginUI.refreshListener();
		
		return null;
	}

}
