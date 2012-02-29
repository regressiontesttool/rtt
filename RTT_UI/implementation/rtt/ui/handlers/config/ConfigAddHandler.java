package rtt.ui.handlers.config;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.configuration.Path;
import rtt.ui.content.internal.ProjectContent;
import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class ConfigAddHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getSelectedObject(ProjectContent.class, event);
		
		ConfigurationDialog configDialog = new ConfigurationDialog(
				getParentShell(event), projectContent);

		if (configDialog.open() == Dialog.OK) {
			try {
				Configuration config = configDialog.getConfiguration();
				RttProject project = projectContent.getProject();
				
				String lexerClass = config.getLexerClass().getValue();
				String parserClass = config.getParserClass().getValue();
				String configName = config.getName();
				boolean makeDefault = configDialog.isDefault();
				
				List<String> cp = new LinkedList<String>();
				if (config.getClasspath() != null) {
					for (Path path : config.getClasspath().getPath()) {
						if (path.getValue() != null) {
							cp.add(path.getValue());
						}						
					}
				}
				
				project.addConfiguration(lexerClass, parserClass, configName,
						makeDefault, cp);
				project.save();
				
				projectContent.reload();
			} catch (Exception e) {
				throw new ExecutionException("Could not add configuration.", e);
			}
		}

		return null;
	}
}
