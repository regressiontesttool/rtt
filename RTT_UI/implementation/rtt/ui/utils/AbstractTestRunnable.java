package rtt.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.testsuite.TestsuiteContent;

public abstract class AbstractTestRunnable implements IRunnableWithProgress {

	protected List<Throwable> exceptions;
	protected String suiteName;
	protected ProjectContent projectContent;
	private String title;

	public AbstractTestRunnable(String title) {
		this.exceptions = new ArrayList<Throwable>();
		this.title = title;
	}
	
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public void setProjectContent(ProjectContent projectContent) {
		this.projectContent = projectContent;
	}
	
	public String getMessageTitle() {
		return title;
	}
}
