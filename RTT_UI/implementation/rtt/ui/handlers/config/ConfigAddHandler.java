package rtt.ui.handlers.config;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;

import rtt.core.archive.configuration.Configuration;
import rtt.core.exceptions.RTTException;
import rtt.ui.RttPluginUI;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.handlers.AbstractSelectionHandler;

public class ConfigAddHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = this.getProjectContent(event);
		Configuration config = projectContent.createEmptyConfiguration();
		
		ConfigurationDialog configDialog = new ConfigurationDialog(
				getParentShell(event), projectContent, config);
		
		configDialog.setTitle("Add configuration");
		configDialog.setMessage("Create a new configuration ...");

		if (configDialog.open() == Dialog.OK) {
			try {
				config = configDialog.getConfiguration();
				projectContent.addConfiguration(config, configDialog.isDefault());
				RttPluginUI.refreshManager();
			} catch (RTTException e) {
				throw new ExecutionException("Could not add configuration.", e);
			}
		}

		return null;
	}
}
