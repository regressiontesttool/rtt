package rtt.ui.handlers.classpath;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import rtt.core.exceptions.RTTException;
import rtt.ui.RttPluginUI;
import rtt.ui.content.configuration.ConfigurationContent;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;
import rtt.ui.handlers.AbstractSelectionHandler;

public class ClasspathEntryRemoveHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ConfigurationContent config = getSelectedObject(ConfigurationContent.class, event);
		SimpleTypedContent entry = getSelectedObject(SimpleTypedContent.class, event);
		
		if (entry.getType() == ContentType.CLASSPATHENTRY) {
			try {
				config.removeClasspathEntry(entry.getText());
				RttPluginUI.refreshListener();
			} catch (RTTException e) {
				throw new ExecutionException("Could not remove class path entry.", e);
			}
		}		
		
		return null;
	}

}
