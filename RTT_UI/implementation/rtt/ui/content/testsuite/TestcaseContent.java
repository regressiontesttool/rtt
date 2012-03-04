package rtt.ui.content.testsuite;

import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.VersionData;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public class TestcaseContent extends AbstractContent {

	private Testcase testcase;

	public TestcaseContent(IContent parent, String suiteName, Testcase testcase) {
		super(parent);
		VersionData versionData = testcase.getVersionData();
		
		this.testcase = testcase;

		childs.add(new InputContent(this, suiteName, testcase.getName(), versionData.getInput()));
		childs.add(new ReferenceContent(this, suiteName, testcase.getName(), versionData.getReference()));
	}

	@Override
	public String getText() {
		return testcase.getName();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.TESTCASE;
	}

	public Testcase getTestcase() {
		return testcase;
	}
}
