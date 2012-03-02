package rtt.ui.content.testsuite;

import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.ui.content.IContent;
import rtt.ui.content.IDeletableContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.core.ProjectFinder;
import rtt.ui.model.RttProject;

public class TestsuiteContent extends AbstractContent implements IDeletableContent {

	private String suiteName;

	public TestsuiteContent(IContent parent, Testsuite testsuite) {
		super(parent);
		this.suiteName = testsuite.getName();
		
		if (testsuite.getTestcase() != null) {
			for (Testcase testcase : testsuite.getTestcase()) {
				if (testcase.isDeleted() == false) {
					childs.add(new TestcaseContent(this, testsuite.getName(), testcase));
				}
			}
		}
	}

	@Override
	public String getText() {
		return suiteName;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.TESTSUITE;
	}
	
	@Override
	public void doDelete() throws RTTException {
		RttProject project = parent.getProject();
		
		if (project != null) {
			project.removeTestsuite(suiteName);
			project.save();
			
			parent.load();
			ProjectFinder.fireChangeEvent();
		}		
	}

}