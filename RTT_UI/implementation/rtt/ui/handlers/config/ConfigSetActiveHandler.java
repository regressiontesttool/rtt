package rtt.ui.handlers.config;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

import rtt.ui.content.internal.ProjectContent;
import rtt.ui.content.internal.configuration.ConfigurationContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class ConfigSetActiveHandler extends AbstractSelectionHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getSelectedObject(ProjectContent.class, event);
		RttProject project = projectContent.getProject();
		
		ConfigurationContent config = getSelectedObject(ConfigurationContent.class, event);
		
		try {
			project.setActiveConfiguration(config.getText());
			projectContent.reload();
		} catch (Exception e) {
			throw new ExecutionException("Could not activate configuration.", e); 
		}

		return null;
	}

}