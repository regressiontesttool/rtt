package rtt.ant.newTasks;

import java.io.File;

import org.apache.tools.ant.BuildException;

import rtt.core.manager.Manager;

public class ExportLogTask extends ReadArchiveTask {

	private static final String DEST_NOT_SET = 
			"No log destination was set.";

	private static final String COULD_NOT_EXPORT = 
			"Could not export log.";
	
	private String destination;
	private File destFile;
	
	public void setDest(String destination) {
		this.destination = destination;
	}
	
	@Override
	public void checkIntegrity(Manager manager)
			throws BuildException {		
		destFile = getFile(destination, DEST_NOT_SET);
	}

	@Override
	public void execute(File archiveFile, Manager manager) {				
		try {
			manager.exportLog(destFile);
		} catch (Exception e) {
			error(COULD_NOT_EXPORT);
			throw new BuildException(COULD_NOT_EXPORT);
		}
	}
}
