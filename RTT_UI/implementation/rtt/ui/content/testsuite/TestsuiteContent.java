package rtt.ui.content.testsuite;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;

import rtt.core.archive.testsuite.Testcase;
import rtt.core.archive.testsuite.Testsuite;
import rtt.core.exceptions.RTTException;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.ProjectContent;

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
	
	public void reload() {
		childs.clear();
		loadContent();
	}

	@Override
	public String getText() {
		return testsuite.getName();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.TESTSUITE;
	}

	public void addTestcase(Object[] objects) throws RTTException {
		List<File> files = new ArrayList<File>();
		
		for (Object object : objects) {
			if (object instanceof IFile) {
				files.add(((IFile) object).getLocation().toFile());
			}
		}
		
		ProjectContent projectContent = getContent(ProjectContent.class);
		if (projectContent != null) {
			projectContent.addTestcases(testsuite.getName(), files);
		}
		
		reload();
	}
	
	public void removeTestcase(String caseName) throws RTTException {
		ProjectContent projectContent = getContent(ProjectContent.class);
		if (projectContent != null) {
			projectContent.removeTestcase(testsuite.getName(), caseName);
		}
		
		reload();
	}

}