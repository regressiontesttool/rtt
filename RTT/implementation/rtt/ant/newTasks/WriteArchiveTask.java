package rtt.ant.newTasks;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;

public abstract class WriteArchiveTask extends ReadArchiveTask {
	
	private static final String NO_OUTPUT_ARCHIVE =
			"The output attribute is used, but no file was given.";
	private static final String NO_OVERWRITE = 
			"Overwrite flag was false. Discard any archive changes.";
	private static final String NO_ARCHIVE_CHANGES = 
			"No archive changes detected. Save is omitted.";
	
	private String outputArchive = null;
	private boolean overwrite = true;	
	
	public void setOutput(String output) {
		this.outputArchive = output;
	}
	
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	@Override
	public void execute(File archiveFile, Manager manager) {		
		boolean hasArchiveChanged = executeTask(manager);
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
	
	protected abstract boolean executeTask(Manager manager);

	private void saveArchive(Manager manager, File filePath) {
		if (!filePath.exists() || overwrite) {
			try {
				info("Save to: " + filePath);
				manager.saveArchive(filePath);
			} catch (RTTException e) {
				log("Could not save archive.", e, Project.MSG_ERR);
				throw new BuildException(e);
			}
		} else {
			info(NO_OVERWRITE);
		}			
	}
}
