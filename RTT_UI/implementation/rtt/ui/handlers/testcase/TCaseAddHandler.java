package rtt.ui.handlers.testcase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import rtt.core.exceptions.RTTException;
import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

public class TCaseAddHandler extends AbstractSelectionHandler {

	List<Exception> exceptions;

	public TCaseAddHandler() {
		exceptions = new ArrayList<Exception>();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getSelectedObject(ProjectContent.class, event);
		RttProject project = projectContent.getProject();
		
		TestsuiteContent suite = getSelectedObject(TestsuiteContent.class,
				event);

		ResourceSelectionDialog d = new ResourceSelectionDialog(
				getParentShell(event), project.getWorkspaceProject(),
				"Select File...");

		d.setBlockOnOpen(true);
		int savedTestCases = 0;

		if (d.open() == ResourceSelectionDialog.OK) {
			for (Object o : d.getResult()) {
				if (o instanceof IFile) {
					try {
						File file = new File(((IFile) o).getLocation().toOSString());
						project.addTestcase(suite.getText(), file);
						savedTestCases++;
					} catch (Exception e) {
						addException("Could not add test case.", e);
					}
				}
			}
		}

		checkExceptions(savedTestCases, getParentShell(event));

		try {
			project.save();
			projectContent.reload();
		} catch (RTTException e) {
			throw new ExecutionException("Could not save archive for project '"
					+ project.getName() + "'.", e);
		}

		return null;
	}

	private void checkExceptions(int successCount, Shell parentShell) {
		if (!exceptions.isEmpty()) {
			for (Exception exception : exceptions) {
				RttLog.log(new Status(Status.ERROR, RttPluginUI.PLUGIN_ID,
						exception.getMessage(), exception.getCause()));
			}

			String message = "";
			if (successCount > 0) {
				message = "Some files could not be added to the test suite. See Error Log for more information";
			} else {
				message = "No file added to the test suite. See Error Log for more information.";
			}

			MessageDialog.openInformation(parentShell, "Add test cases",
					message);
		}
	}

	private void addException(String message, Throwable cause) {
		exceptions.add(new ExecutionException(message, cause));
	}

}
