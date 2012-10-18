package rtt.ui.handlers.tests;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;

import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.handlers.AbstractSelectionHandler;
import rtt.ui.dialogs.JUnitExportDialog;

/**
 * @author C. BŸrger
 */
public class JUnitExportHandler extends AbstractSelectionHandler implements IHandler {
	public static final String ID = "rtt.ui.commands.tests.junitexport";
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ProjectContent projectContent = getProjectContent(event);
		TestsuiteContent suiteContent = getSelectedObject(TestsuiteContent.class, event);
		
		JUnitExportDialog exportDialog = new JUnitExportDialog(getParentShell(event), projectContent);
		
		if (exportDialog.open() == Dialog.OK) {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // ISO 8601 date format
			    df.setTimeZone(TimeZone.getTimeZone("UTC")); // ISO 8601 requires UTC time zone!
				
				StringBuilder content = new StringBuilder();
				if (!exportDialog.getAdaperPackage().getElementName().isEmpty()) {
					content.append("package ");
					content.append(exportDialog.getAdaperPackage().getElementName());
					content.append(";\n\n");
				}
				content.append("@javax.annotation.Generated(\n");
				content.append("\tvalue = \"");
				content.append(this.getClass().getCanonicalName());
				content.append("\",\n");
				content.append("\tdate = \"");
				content.append(df.format(new Date()));
				content.append("\",\n");
				content.append("\tcomments = \"JUnit adapter executing RTT test suite [");
				content.append(suiteContent.getText());
				content.append("] of project [");
				content.append(projectContent.getText());
				content.append("] with configuration [");
				content.append(projectContent.getProject().getActiveConfiguration().getName());
				content.append("].\")\n");
				content.append("public class ");
				content.append(exportDialog.getAdapterClass());
				content.append(" {\n");
				content.append("\t@org.junit.Test\n");
				content.append("\tpublic void runRTTTestsuite() {\n");
				content.append("\t\t// TODO\n");
				content.append("\t}\n");
				content.append("}\n");
				
				exportDialog.getAdaperPackage().createCompilationUnit(
						exportDialog.getAdapterClass() + ".java",
						content.toString(),
						true,
						null);
			} catch (Exception exception) {
				throw new ExecutionException("Could not export JUnit adapter.", exception);
			}
		}
		
		return null;
	}
}
