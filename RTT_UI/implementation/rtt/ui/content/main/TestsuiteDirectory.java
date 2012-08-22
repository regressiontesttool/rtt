package rtt.ui.content.main;

import java.util.List;

import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.content.IContent;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.testsuite.TestsuiteContent;

public class TestsuiteDirectory extends AbstractContent {

	public TestsuiteDirectory(ProjectContent content) {
		super(content);		
		loadContents();
	}

	private void loadContents() {
		List<Testsuite> suites = getProject().getArchive().getTestsuites();
		
		if (suites != null) {
			for (Testsuite suite : suites) {
				TestsuiteContent content = new TestsuiteContent(this, suite);
				childs.add(content);
			}
		}
	}
	
	@Override
	public void reload(ReloadInfo info) {
		
		if (info.contains(Content.TESTSUITE)) {
			childs.clear();			
			loadContents();
		} else if (info.contains(Content.TESTCASE)) {
			for (IContent content : childs) {
				content.reload(info);
			}			
		}
	}
	
	public boolean isEmpty() {
		return childs.isEmpty();
	}

	@Override
	public String getText() {
		return "Testsuites";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.TESTSUITE;
	}

	public TestsuiteContent getTestsuite(int index) {
		if (childs.isEmpty() || index > childs.size()) {
			return null;
		}
		
		return (TestsuiteContent) childs.get(index);
	}
}
