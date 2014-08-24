package rtt.ui.handlers.config;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

/**
 * This handler is used to edit an existing {@link Configuration}.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class ConfigEditHandler extends AbstractSelectionHandler implements
		IHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = this.getProjectContent(event);
		ConfigurationContent configContent = getSelectedObject(
				ConfigurationContent.class, event);
		
		RttProject project = projectContent.getProject();

		ConfigurationDialog configDialog = new ConfigurationDialog(
				getParentShell(event), project,
				configContent.getConfiguration());
		
		configDialog.setTitle("Modify Configuration ...");
		configDialog.setMessage("Modify an existing Configuration.");
		
		configDialog.setNameEditable(false);
		configDialog.setDefault(configContent.isDefault());

		if (configDialog.open() == Dialog.OK) {
			try {

				String configName = configDialog.getConfigName();
				String parserClass = configDialog.getInitNodeName();
				List<String> cpEntries = configDialog.getClasspathEntries();
				
				project.setConfiguration(configName, parserClass, cpEntries, configDialog.isDefault());
				
				projectContent.reload(new ReloadInfo(Content.CONFIGURATION));				
			} catch (Exception exception) {
				throw new ExecutionException("Could not modify configuration.",
						exception);
			}
		}

		return null;
	}

}
