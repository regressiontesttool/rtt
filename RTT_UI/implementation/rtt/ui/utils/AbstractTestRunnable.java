package rtt.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.ui.content.ProjectContent;

public abstract class AbstractTestRunnable implements IRunnableWithProgress {

	protected List<Throwable> exceptions;
	protected String suiteName;
	protected ProjectContent projectContent;

	public AbstractTestRunnable() {
		this.exceptions = new ArrayList<Throwable>();
	}
	
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}
	
	public void setProjectContent(ProjectContent projectContent) {
		this.projectContent = projectContent;
	}
	
	public boolean isInitialized() {
		if (projectContent == null) {
			return false;
		}
		
		if (suiteName == null || suiteName.equals("")) {
			return false;
		}
		
		return true;
	}
	
	public abstract String getMessageTitle();

	
}
