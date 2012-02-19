package rtt.ui.content.internal.data;

import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.VersionData;
import rtt.core.exceptions.RTTException;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.IContent;
import rtt.ui.content.IDeletableContent;
import rtt.ui.content.internal.ContentIcon;
import rtt.ui.core.ProjectFinder;
import rtt.ui.model.RttProject;

public class TestcaseContent extends AbstractContent implements
		IDeletableContent {

	private String suiteName;
	private String caseName;

	public TestcaseContent(IContent parent, String suiteName, Testcase testcase) {
		super(parent);
		this.caseName = testcase.getName();
		this.suiteName = suiteName;
		VersionData versionData = testcase.getVersionData();

		childs.add(new InputContent(this, suiteName, caseName, versionData.getInput()));
		childs.add(new ReferenceContent(this, suiteName, caseName, versionData.getReference()));
	}

	@Override
	public String getText() {
		return caseName;
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.TESTCASE;
	}

	@Override
	public void doDelete() throws RTTException {
		RttProject project = parent.getProject();
		
		if (project != null) {
			project.removeTestcase(suiteName, caseName);
			project.save();
			
			parent.load();
			ProjectFinder.fireChangeEvent();			
		}		
	}
}
