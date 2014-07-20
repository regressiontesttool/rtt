package rtt.ant.newTasks;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;

public abstract class AbstractAntTask extends Task {
	
	private static final String NO_ARCHIVE_ATTRIBUTE =			
			"Archive attribute must be set.";	
	private static final String NO_INPUT_ARCHIVE =
			"No archive file was set.";
	
	private String inputArchive = null;
	
	public void setArchive(String archive) {
		this.inputArchive = archive;
	}
	
	@Override
	public final void execute() throws BuildException {
		File archiveFile = getInputArchive();
		Manager manager = createManager(archiveFile);
		
		checkIntegrity(archiveFile, manager);		
		execute(archiveFile, manager);	

		manager.close();
	}

	private File getInputArchive() {
		if (inputArchive == null) {
			error(NO_ARCHIVE_ATTRIBUTE);
			throw new BuildException(NO_ARCHIVE_ATTRIBUTE);
		}
		
		File archiveFile = getFile(inputArchive, NO_INPUT_ARCHIVE);
		info("Using archive: " + archiveFile);
		
		return archiveFile;
	}
	
	private Manager createManager(File archiveFile) {
		try {
			return new Manager(archiveFile, true);
		} catch (RTTException e) {
			log("Could not create archive manager.", e, Project.MSG_ERR);
			throw new BuildException(e);
		}
	}

	protected final File getFile(String filePath, String errorMessage) {
		if (filePath == null || filePath.equals("")) {
			error(errorMessage);
			throw new BuildException(errorMessage);
		}
		
		return new File(filePath);
	}
	
	public abstract void checkIntegrity(File archiveFile, Manager manager) throws BuildException;
	public abstract void execute(File archiveFile, Manager manager);
	
	protected void info(String message) {
		log(message, Project.MSG_INFO);
	}
	
	protected void warn(String message) {
		log(message, Project.MSG_WARN);
	}
	
	protected void error(String message) {
		log(message, Project.MSG_ERR);
	}

}
