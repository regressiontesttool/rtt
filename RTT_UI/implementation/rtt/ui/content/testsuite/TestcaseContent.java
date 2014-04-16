package rtt.ui.content.testsuite;

import java.util.List;

import rtt.core.archive.configuration.Configuration;
import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.VersionData;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public class TestcaseContent extends AbstractContent {

	private Testcase testcase;

	public TestcaseContent(IContent parent, String suiteName, Testcase testcase) {
		super(parent);
		
		Configuration activeConfig = getProject().getActiveConfiguration();
		
		this.testcase = testcase;
		childs.add(new InputContent(this, suiteName, testcase.getName(), testcase.getInputID()));
		
		List<VersionData> versionList = testcase.getVersionData();
		for (VersionData versionData : versionList) {
			if (versionData.getConfig().equals(activeConfig.getName()) && versionData.getReferenceID() > 0) {
				childs.add(new ReferenceContent(this, suiteName, testcase.getName(), versionData.getReferenceID()));				
			}
		}
		
		if (testcase.getParameter() != null && testcase.getParameter().size() > 0) {
			childs.add(new ParameterContent(this, testcase.getParameter()));
		}		
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

	public String getCaseName() {
		return testcase.getName();
	}
	
	public String getSuiteName() {
		return parent.getText();
	}
}
