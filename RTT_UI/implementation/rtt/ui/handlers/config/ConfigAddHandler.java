package rtt.ui.handlers.config;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;

import rtt.core.archive.configuration.Configuration;
import rtt.core.exceptions.RTTException;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class ConfigAddHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		ProjectContent projectContent = this.getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		Configuration config = project.createEmptyConfiguration();
		
		ConfigurationDialog configDialog = new ConfigurationDialog(
				getParentShell(event), projectContent, config);
		
		configDialog.setTitle("Add configuration");
		configDialog.setMessage("Create a new configuration ...");

		if (configDialog.open() == Dialog.OK) {
			try {
				
				String configName = configDialog.getConfigName();
				String lexerClass = configDialog.getLexerName();
				String parserClass = configDialog.getParserName();
				List<String> cpEntries = configDialog.getClasspathEntries();

				project.setConfiguration(configName, lexerClass, parserClass, cpEntries ,configDialog.isDefault());
				project.save();
		
				projectContent.reload(new ReloadInfo(Content.CONFIGURATION));
				
			} catch (RTTException e) {
				throw new ExecutionException("Could not add configuration.", e);
			}
		}

		return null;
	}
}
