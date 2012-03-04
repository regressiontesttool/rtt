package rtt.ui.content.testsuite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.core.exceptions.RTTException.Type;
import rtt.ui.RttLog;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.model.RttProject;

public class TestsuiteContent extends AbstractContent {
	private Testsuite testsuite;

	public TestsuiteContent(IContent parent, Testsuite testsuite) {
		super(parent);
		this.testsuite = testsuite;
		
		loadContent();		
	}
	
	private void loadContent() {
		if (testsuite.getTestcase() != null) {
			for (Testcase testcase : testsuite.getTestcase()) {
				if (testcase.isDeleted() == false) {
					childs.add(new TestcaseContent(this, testsuite.getName(), testcase));
				}
			}
		}
	}
	
	private void reload() {
		childs.clear();
		loadContent();
	}

	@Override
	public String getText() {
		return testsuite.getName();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.TESTSUITE;
	}

	public void addTestcase(Object[] objects) throws RTTException {
		RttProject project = this.getProject();
		List<Exception> exceptions = new ArrayList<Exception>();
		int savedTestCases = 0;
		
		for (Object object : objects) {
			try  {
				if (object instanceof IFile) {
					project.addTestcase(testsuite.getName(), (IFile) object);
					savedTestCases++;
				}							
			} catch (RTTException e) {
				exceptions.add(e);
			}
		}
		
		if (exceptions.isEmpty() == false) {
			for (Exception exception : exceptions) {
				RttLog.log(exception);
			}
			
			String message = "";
			if (savedTestCases > 0) {
				message = "Some files could not be added to the test suite. See Error Log for more information";
			} else {
				message = "No file added to the test suite. See Error Log for more information.";
			}
			
			throw new RTTException(Type.TESTCASE, message);
		}
		
		project.save();
		
		reload();
	}

	

	public void removeTestcase(String caseName) throws RTTException {
		RttProject project = this.getProject();
		
		project.removeTestcase(testsuite.getName(), caseName);
		project.save();
		
		reload();
	}

}