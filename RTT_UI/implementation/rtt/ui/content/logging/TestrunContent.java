package rtt.ui.content.logging;

import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.Result;
import rtt.core.archive.logging.Testrun;
import rtt.ui.content.IContent;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;

public class TestrunContent extends LogEntryContent {

	Testrun testrun;

	public TestrunContent(IContent parent, Entry entry) {
		super(parent, entry);

		if (entry instanceof Testrun) {
			this.testrun = (Testrun) entry;
			loadContents();
		}
	}
	
	private void loadContents() {		
		for (Result result : testrun.getResult()) {
			childs.add(new TestResultContent(this, result));
		}
	}
	
	@Override
	public void reload(ReloadInfo info) {
		if (info.contains(Content.TESTRUN)) {
			childs.clear();
			loadContents();
		}
	}

	@Override
	public String getText() {
		return "Testrun: " + getFormatedDate() + " ["
				+ testrun.getConfiguration() + "]";
	}

	public String getTestsuite() {
		return testrun.getTestsuite();
	}

	public String getConfiguration() {
		return testrun.getConfiguration();
	}

}
