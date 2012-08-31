package rtt.ui.handlers.testcase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.ui.RttLog;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
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
		TestsuiteContent suiteContent = getSelectedObject(TestsuiteContent.class,
				event);
		
		ProjectContent projectContent = getProjectContent(event);
		RttProject project = projectContent.getProject();		

		ResourceSelectionDialog resDialog = new ResourceSelectionDialog(
				getParentShell(event), project.getJavaProject().getProject(),
				"Select files for new test cases.");

		resDialog.setBlockOnOpen(true);
		
		if (resDialog.open() == ResourceSelectionDialog.OK) {
			try {
				List<File> files = new ArrayList<File>();
				
				for (Object object : resDialog.getResult()) {
					if (object instanceof IFile) {
						files.add(((IFile) object).getLocation().toFile());
					}
				}
				
				List<RTTException> exceptions = project.addTestcase(suiteContent.getText(), files);
				project.save();
				
				projectContent.reload(new ReloadInfo(Content.TESTCASE));
				
				if (exceptions.isEmpty() == false) {
					for (Exception exception : exceptions) {
						RttLog.log(exception);
					}			
					
					throw new RTTException(Type.OPERATION_FAILED, "Could not add test cases. See Error Log for details ...");
				}				
			} catch (RTTException e) {				
				throw new ExecutionException("Some files could not be added to the test suite. See Error Log for details ...");				
			}
		}
		
		
		return null;
	}
}
