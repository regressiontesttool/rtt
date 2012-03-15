package rtt.ui.content.main;

import java.util.List;

import rtt.core.archive.testsuite.Testsuite;
import rtt.ui.content.IContent;
import rtt.ui.content.testsuite.TestsuiteContent;

public class TestsuiteDirectory extends AbstractContent {

	public TestsuiteDirectory(ProjectContent content) {
		super(content);
		loadContents();
	}

	private void loadContents() {
		List<Testsuite> suites = getProject().getArchive().getTestsuites(false);
		
		if (suites != null) {
			for (Testsuite testsuite : suites) {
				childs.add(new TestsuiteContent(this, testsuite));
			}
		}
	}
	
	@Override
	public void reload() {
		for (IContent content : childs) {
			content.reload();
		}
	}
	
	public void addTestsuite(Testsuite newSuite) {
		if (newSuite != null) {
			childs.add(new TestsuiteContent(this, newSuite));
		}
	}
	
	public void removeTestsuite(TestsuiteContent suite) {
		childs.remove(suite);
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
