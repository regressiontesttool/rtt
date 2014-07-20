package rtt.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import rtt.ant.newTasks.RTTAntTask;
import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;

public class AbstractAntTask extends Task {
	
	private static final String NO_ARCHIVE_ATTRIBUTE =			
			"Archive attribute must be set.";	
	private static final String NO_INPUT_ARCHIVE =
			"No archive file was set.";
	private static final String NO_OUTPUT_ARCHIVE =
			"The output attribute is used, but no file was given.";
	private static final String NO_OVERWRITE = 
			"Overwrite flag was false. Discard any archive changes.";
	private static final String NO_ARCHIVE_CHANGES = 
			"No archive changes detected. Save is omitted.";
	private static final String ARCHIVE_NONEXISTS = 
			"The given archive does not exists.";
	
	private String inputArchive = null;
	private String outputArchive = null;
	private boolean overwrite = true;
	
	private List<RTTAntTask> tasks = new ArrayList<RTTAntTask>();
	
	public void setArchive(String archive) {
		this.inputArchive = archive;
	}
	
	public void setOutput(String output) {
		this.outputArchive = output;
	}
	
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}	
	
	public void addConfigured(RTTAntTask task) {
		tasks.add(task);
	}

	@Override
	public void execute() throws BuildException {
		checkTasks();
		
		File archiveFile = getInputArchive();
		Manager manager = createManager(archiveFile);
		
		boolean hasArchiveChanged = executeTasks(manager);		
		if (hasArchiveChanged) {
			if (outputArchive != null) {
				File outputFile = getFile(outputArchive, NO_OUTPUT_ARCHIVE);
				saveArchive(manager, outputFile);		
			} else {
				saveArchive(manager, archiveFile);
			}
		} else {
			info(NO_ARCHIVE_CHANGES);
		}
	}

	private void checkTasks() throws BuildException {
		for (RTTAntTask task : tasks) {
			task.checkIntegrity();
		}
	}

	private File getInputArchive() {
		if (inputArchive == null) {
			error(NO_ARCHIVE_ATTRIBUTE);
			throw new BuildException(NO_ARCHIVE_ATTRIBUTE);
		}
		
		File archiveFile = getFile(inputArchive, NO_INPUT_ARCHIVE);
		info("Using archive: " + archiveFile);
		
		if (!archiveFile.exists()) {
			warn(ARCHIVE_NONEXISTS);
		}
		
		return archiveFile;
	}

	private File getFile(String filePath, String errorMessage) {
		if (filePath == null || filePath.equals("")) {
			error(errorMessage);
			throw new BuildException(errorMessage);
		}
		
		return new File(filePath);
	}
	
	private Manager createManager(File archiveFile) {
		try {
			return new Manager(archiveFile, true);
		} catch (RTTException e) {
			log("Could not create archive manager.", e, Project.MSG_ERR);
			throw new BuildException(e);
		}
	}
	
	private boolean executeTasks(Manager manager) {
		boolean archiveChanged = false;
		for (RTTAntTask task : tasks) {
			task.execute(manager);
			if (task.hasArchiveChanged() && !archiveChanged) {
				archiveChanged = true;
			}
		}
		
		return archiveChanged;
	}
	
	private void saveArchive(Manager manager, File filePath) {
		info("Save to: " + filePath);
		
		if (!filePath.exists() || overwrite) {
			try {
				manager.saveArchive(filePath);
			} catch (RTTException e) {
				log("Could not save archive.", e, Project.MSG_ERR);
				throw new BuildException(e);
			}
		} else {
			info(NO_OVERWRITE);
		}			
	}	
	
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
