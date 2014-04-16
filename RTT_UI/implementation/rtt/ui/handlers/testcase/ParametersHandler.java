package rtt.ui.handlers.testcase;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import rtt.core.archive.testsuite.Testcase;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestcaseContent;
import rtt.ui.dialogs.ParametersDialog;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.model.RttProject;

/**
 * This handler is used to modify parameters of the selected {@link Testcase}.
 * @author Christian Oelsner <C.Oelsner@gmail.com>
 *
 */
public class ParametersHandler extends AbstractSelectionHandler implements
		IHandler {

	@Override
	public Object doExecute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = this.getProjectContent(event);
		TestcaseContent testcaseContent = getSelectedObject(TestcaseContent.class, event);
		
		String suiteName = testcaseContent.getSuiteName();
		String caseName = testcaseContent.getCaseName();

		ParametersDialog parametersDialog = new ParametersDialog(getParentShell(event), testcaseContent);
		
		parametersDialog.setTitle("Modify Parameters ...");
		parametersDialog.setMessage("Modify parameters of the test case '" + suiteName + "/" + caseName + "'.");
		
		RttProject project = projectContent.getProject();

		if (parametersDialog.open() == Dialog.OK) {
			try {
				
				project.addParameters(suiteName, caseName, parametersDialog.getParameters());
				
				projectContent.reload(new ReloadInfo(Content.TESTCASE));				
			} catch (Exception exception) {
				throw new ExecutionException("Could not modify test case.",
						exception);
			}
		}

		return null;
	}

}
