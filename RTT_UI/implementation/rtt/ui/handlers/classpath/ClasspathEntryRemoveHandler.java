package rtt.ui.handlers.classpath;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.core.exceptions.RTTException;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class ClasspathEntryRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = this.getProjectContent(event);
		RttProject project = projectContent.getProject();
		
		ConfigurationContent config = getSelectedObject(ConfigurationContent.class, event);
		SimpleTypedContent entry = getSelectedObject(SimpleTypedContent.class, event);
		
		if (entry.getType() == ContentType.CLASSPATHENTRY) {
			try {
				project.removeClasspathEntry(config.getText(), entry.getText());
				project.save();
				
				projectContent.reload(true);
			} catch (RTTException e) {
				throw new ExecutionException("Could not remove class path entry.", e);
			}
		}		
		
		return null;
	}

}
