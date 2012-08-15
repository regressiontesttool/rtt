package rtt.ui.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.exceptions.RTTException;
import rtt.core.utils.GenerationInformation;
import rtt.ui.RttLog;
import rtt.ui.content.main.ProjectContent;

public class GenerateTestRunnable extends AbstractTestRunnable implements IRunnableWithProgress {

	private GenerationInformation results;

	public GenerateTestRunnable(ProjectContent projectContent, String suiteName) {
		this();
		setProjectContent(projectContent);
		setSuiteName(suiteName);
	}
	
	public GenerateTestRunnable() {
		super("Generating new tests ...");
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		monitor.beginTask("Generating reference data for test suite '" + suiteName + "' ...", IProgressMonitor.UNKNOWN);

		try {
			results = projectContent.generateTest(suiteName);			
		} catch (RTTException e) {
			RttLog.log(e);
			throw new InterruptedException("Generation exception: " + e.getMessage());
		}
		
		projectContent.reload();
		
		monitor.done();
	}
	
	@Override
	public GenerationInformation getResults() {
		return results;
	}
}
