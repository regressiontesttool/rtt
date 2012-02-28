package rtt.ui.handlers.config;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import rtt.core.archive.configuration.Configuration;
import rtt.ui.content.internal.ProjectContent;
import rtt.ui.content.internal.configuration.ConfigurationContent;
import rtt.ui.dialogs.ConfigurationDialog;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class ConfigEditHandler extends AbstractSelectionHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getSelectedObject(ProjectContent.class,
				event);
		RttProject project = projectContent.getProject();
		ConfigurationContent configContent = getSelectedObject(
				ConfigurationContent.class, event);
		Configuration config = configContent.getConfiguration();

		ConfigurationDialog configDialog = new ConfigurationDialog(
				getParentShell(event));

		configDialog.setContent(config.getName(), config.getLexerClass()
				.getValue(), config.getParserClass().getValue(), true);

		if (configDialog.open() == Dialog.OK) {
			try {
				String lexerClass = configDialog.getLexerName();
				String parserClass = configDialog.getParserName();
				String configName = configDialog.getConfigName();
				boolean makeDefault = configDialog.getMakeDefault();

				// CHRISTIAN todo
				List<String> cp = new LinkedList<String>();
				cp.add(project.getWorkspaceProject().getLocation().toOSString()
						+ "\\bin\\");

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
