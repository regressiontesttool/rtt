package rtt.ant.newTasks;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import rtt.core.manager.Manager;

public class CreateArchiveTask extends AbstractAntTask {

	private static final String ARCHIVE_EXISTS = 
			"The archive already exists.";
	private static final String COULD_NOT_CREATE = 
			"Could not create archive.";
	

	@Override
	public void checkIntegrity(File archiveFile, Manager manager)
			throws BuildException {
		
		if (archiveFile.exists()) {
			error(ARCHIVE_EXISTS);
			throw new BuildException(ARCHIVE_EXISTS);
		}
	}

	@Override
	public void execute(File archiveFile, Manager manager) {
		try {
			manager.createArchive(archiveFile);
		} catch (Exception e) {
			log(COULD_NOT_CREATE, e, Project.MSG_ERR);
			throw new BuildException(e);
		}
	}

}
