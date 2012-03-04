package rtt.ui.content.logging;

import rtt.core.archive.logging.Entry;
import rtt.core.archive.logging.Result;
import rtt.core.archive.logging.Testrun;
import rtt.ui.content.IContent;

public class TestrunContent extends LogEntryContent {

	Testrun testrun;

	public TestrunContent(IContent parent, Entry entry) {
		super(parent, entry);

		if (entry instanceof Testrun) {
			this.testrun = (Testrun) entry;

			for (Result result : ((Testrun) entry).getResult()) {
				childs.add(new TestResultContent(this, result));
			}
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
