package rtt.ui.handlers.classpath;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;

import rtt.core.exceptions.RTTException;
import rtt.ui.content.internal.ProjectContent;
import rtt.ui.content.internal.configuration.ConfigurationContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class ClasspathEntryAddHandler extends AbstractSelectionHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getSelectedObject(ProjectContent.class, event);
		RttProject project = projectContent.getProject();
		
		ConfigurationContent config = getSelectedObject(
				ConfigurationContent.class, event);

		InputDialog inputDialog = new InputDialog(getParentShell(event), "New",
				"Enter a new classpath entry.", "", null);
		inputDialog.setBlockOnOpen(true);

		if (inputDialog.open() == Dialog.OK) {
			String value = inputDialog.getValue();
			if (value != null && !value.equals("")) {
				try {
					project.addClassPathEntry(config.getText(), value);
					project.save();
					projectContent.reload(true);
				} catch (RTTException e) {
					throw new ExecutionException(
							"Could not add class path entry.", e);
				}
			}
		}

		return null;
	}

}
