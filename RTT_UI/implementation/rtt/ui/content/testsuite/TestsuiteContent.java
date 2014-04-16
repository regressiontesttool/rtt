package rtt.ui.content.testsuite;

import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.manager.Manager;
import rtt.ui.content.IContent;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

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
	
	@Override
	public void reload(ReloadInfo info, Manager manager) {
		if (info.contains(Content.TESTCASE)) {
			childs.clear();
			loadContent();
		}		
	}

	@Override
	public String getText() {
		return testsuite.getName();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.TESTSUITE;
	}
}