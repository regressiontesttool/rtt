package rtt.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.utils.GenerationInformation;
import rtt.ui.model.RttProject;

public abstract class AbstractTestRunnable implements IRunnableWithProgress {

	protected List<Throwable> exceptions;
	protected String suiteName;
	protected RttProject project;
	private String title;

	public AbstractTestRunnable(String title) {
		this.exceptions = new ArrayList<Throwable>();
		this.title = title;
	}
	
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	public void setProject(RttProject project) {
		this.project = project;
	}
	
	public String getMessageTitle() {
		return title;
	}
	
	public abstract GenerationInformation getResults();
}
