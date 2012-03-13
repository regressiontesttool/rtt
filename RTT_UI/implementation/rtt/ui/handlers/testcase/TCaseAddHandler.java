package rtt.ui.handlers.testcase;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import rtt.core.exceptions.RTTException;
import rtt.ui.RttPluginUI;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;

public class TCaseAddHandler extends AbstractSelectionHandler {

	List<Exception> exceptions;

	public TCaseAddHandler() {
		exceptions = new ArrayList<Exception>();
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TestsuiteContent suite = getSelectedObject(TestsuiteContent.class,
				event);

		ResourceSelectionDialog resDialog = new ResourceSelectionDialog(
				getParentShell(event), suite.getProject().getJavaProject().getProject(),
				"Select File...");

		resDialog.setBlockOnOpen(true);
		
		if (resDialog.open() == ResourceSelectionDialog.OK) {
			try {
				suite.addTestcase(resDialog.getResult());
				RttPluginUI.refreshListener();
			} catch (RTTException e) {
				throw new ExecutionException("Errors occured during " +
						"test case creation. Check error log for details.", e);
			}
		}
		
		
		return null;
	}
}
