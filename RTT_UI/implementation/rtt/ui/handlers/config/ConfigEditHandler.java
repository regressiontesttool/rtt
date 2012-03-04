package rtt.ui.handlers.config;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.RttPluginUI;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.handlers.AbstractSelectionHandler;

public class ConfigEditHandler extends AbstractSelectionHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = this.getProjectContent(event);
		ConfigurationContent configContent = getSelectedObject(
				ConfigurationContent.class, event);
		
		ConfigurationDialog configDialog = new ConfigurationDialog(
				getParentShell(event), configContent);

		if (configDialog.open() == Dialog.OK) {
			try {				
				Configuration config = configContent.getConfiguration();
				projectContent.addConfiguration(config, configDialog.isDefault());
				RttPluginUI.refreshListener();
			} catch (Exception e) {
				throw new ExecutionException("Could not modify configuration.", e);
			}
		}

		return null;
	}

}
