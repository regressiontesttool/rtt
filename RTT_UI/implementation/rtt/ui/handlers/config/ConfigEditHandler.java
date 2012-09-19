package rtt.ui.handlers.config;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class ConfigEditHandler extends AbstractSelectionHandler implements
		IHandler {
	
	public static final String ID = "rtt.ui.commands.config.edit";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = this.getProjectContent(event);
		ConfigurationContent configContent = getSelectedObject(
				ConfigurationContent.class, event);

		ConfigurationDialog configDialog = new ConfigurationDialog(
				getParentShell(event), projectContent,
				configContent.getConfiguration());
		
		configDialog.setTitle("Modify Configuration ...");
		configDialog.setMessage("Modify an existing configuration.");
		configDialog.setNameEditable(false);
		configDialog.setDefault(configContent.isDefault());
		
		RttProject project = projectContent.getProject();

		if (configDialog.open() == Dialog.OK) {
			try {

				String configName = configDialog.getConfigName();
				String lexerClass = configDialog.getLexerName();
				String parserClass = configDialog.getParserName();
				List<String> cpEntries = configDialog.getClasspathEntries();
				
				project.setConfiguration(configName, lexerClass, parserClass, cpEntries, configDialog.isDefault());
				project.save();
				
				projectContent.reload(new ReloadInfo(Content.CONFIGURATION));				
			} catch (Exception exception) {
				throw new ExecutionException("Could not modify configuration.",
						exception);
			}
		}

		return null;
	}

}
