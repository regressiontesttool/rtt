package rtt.ui.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.exceptions.RTTException;
import rtt.core.utils.GenerationInformation;
import rtt.core.utils.GenerationInformation.GenerationResult;
import rtt.ui.RttLog;
import rtt.ui.model.RttProject;

public abstract class AbstractTestRunnable implements IRunnableWithProgress {

	protected List<Throwable> exceptions;
	protected String suiteName;
	protected RttProject project;
	protected GenerationInformation genInfo;
	private String title;
	private String taskTitle;

	public AbstractTestRunnable(String title, String taskTitle) {
		this.exceptions = new ArrayList<Throwable>();
		this.title = title;
		this.taskTitle = taskTitle;
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
	
	public GenerationInformation getInformation() {
		return genInfo;
	}
	
	@Override
	public final void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask(taskTitle + " '" + suiteName + "' ...", IProgressMonitor.UNKNOWN);

		try {
			genInfo = doWork(project);
			project.save();
		} catch (RTTException e) {
			RttLog.log(e);
			throw new InterruptedException("Generation exception: " + e.getMessage());
		}
		
		if (genInfo != null) {
			List<GenerationResult> results = genInfo.getResults(false);
			for (GenerationResult result : results) {
				RttLog.log(result.exception);
			}
		}
		
		monitor.done();
	}
	
	abstract GenerationInformation doWork(RttProject project) throws RTTException;
}
