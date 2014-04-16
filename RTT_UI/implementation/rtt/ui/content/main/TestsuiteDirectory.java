package rtt.ui.content.main;

import rtt.core.archive.Archive;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.manager.Manager;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.testsuite.TestsuiteContent;

public class TestsuiteDirectory extends AbstractContent {

	public TestsuiteDirectory(ProjectContent content) {
		super(content);
	}

	private void loadContents(Archive archive) {
		for (Testsuite suite : archive.getTestsuites()) {
			TestsuiteContent content = new TestsuiteContent(this, suite);
			childs.add(content);
		}
	}
	
	@Override
	public void reload(ReloadInfo info, Manager manager) {
		childs.clear();
		loadContents(manager.getArchive());
		
//		TODO lediglich die entsprechenden Elemente nachladen, statt alles 
//		if (info.contains(Content.TESTSUITE)) {
//			childs.clear();			
//			loadContents();
//		} else if (info.contains(Content.TESTCASE)) {
//			for (IContent content : childs) {
//				content.reload(info);
//			}
//		}
	}
	
	public boolean isEmpty() {
		return childs.isEmpty();
	}

	@Override
	public String getText() {
		return "Suites";
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
