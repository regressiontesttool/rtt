package rtt.ant.newTasks;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;

public abstract class ReadArchiveTask extends AbstractAntTask {
	
	private static final String ARCHIVE_NOT_EXISTS = 
			"The given archive does not exists.";
	private static final String COULD_NOT_LOAD = 
			"Could not load archive.";
	
	private String config = "";
	
	public void setConfig(String config) {
		this.config = config;
	}

	@Override
	protected void checkIntegrity(File archiveFile, Manager manager)
			throws BuildException {
		
		if (!archiveFile.exists()) {
			error(ARCHIVE_NOT_EXISTS);
			throw new BuildException(ARCHIVE_NOT_EXISTS);
		}
		
		loadArchive(archiveFile, manager);
		checkIntegrity(manager);
	}
	
	private void loadArchive(File archiveFile, Manager manager) {
		try {
			manager.loadArchive(archiveFile, config);
		} catch (RTTException e) {
			log(COULD_NOT_LOAD, e, Project.MSG_ERR);
			throw new BuildException(e);
		}
	}
	
	protected abstract void checkIntegrity(Manager manager);
}
