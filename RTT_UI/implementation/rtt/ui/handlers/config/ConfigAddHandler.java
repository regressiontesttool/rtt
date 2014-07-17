package rtt.ui.handlers.config;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

/**
 * This handler is used to add a new {@link Configuration}..
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class ConfigAddHandler extends AbstractSelectionHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {

		ProjectContent projectContent = this.getProjectContent(event);
		RttProject project = projectContent.getProject();

		Configuration config = project.createEmptyConfiguration();

		ConfigurationDialog configDialog = new ConfigurationDialog(
				getParentShell(event), project, config);
		
		configDialog.setTitle("New Configuration ...");
		configDialog.setMessage("Create a new Configuration.");

		if (configDialog.open() == Dialog.OK) {
			try {

				String configName = configDialog.getConfigName();
				String parserClass = configDialog.getParserName();
				List<String> cpEntries = configDialog.getClasspathEntries();

				project.setConfiguration(configName, parserClass,
						cpEntries, configDialog.isDefault());	

				projectContent.reload(new ReloadInfo(Content.CONFIGURATION));

			} catch (Exception e) {
				throw new ExecutionException("Could not add configuration.", e);
			}
		}
		
		configDialog.close();
		
		return null;
	}
}
