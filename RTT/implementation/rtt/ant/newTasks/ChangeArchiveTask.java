package rtt.ant.newTasks;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;

public class ChangeArchiveTask extends AbstractAntTask {
	
	public static abstract class ChangeTask extends Task {	
		
		private boolean archiveChanged = false;

		public abstract void checkIntegrity() throws BuildException;
		public abstract void execute(Manager manager);
		
		public final boolean hasArchiveChanged() {
			return archiveChanged;
		}
		
		protected final void setChanged() {
			this.archiveChanged = true;
		}
		
		@Override
		public final void execute() throws BuildException {
			throw new BuildException("The '" + this.getClass().getName() 
					+ "' Task cannot be executed standalone.");
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
	
	private static final String NO_OUTPUT_ARCHIVE =
			"The output attribute is used, but no file was given.";
	private static final String NO_OVERWRITE = 
			"Overwrite flag was false. Discard any archive changes.";
	private static final String NO_ARCHIVE_CHANGES = 
			"No archive changes detected. Save is omitted.";
	private static final String ARCHIVE_NOT_EXISTS = 
			"The given archive does not exists.";
	private static final String COULD_NOT_LOAD = 
			"Could not load archive.";
	
	private String outputArchive = null;
	private String config = "";
	private boolean overwrite = true;
	
	private List<ChangeTask> tasks = new ArrayList<ChangeTask>();
	
	public void setOutput(String output) {
		this.outputArchive = output;
	}
	
	public void setConfig(String config) {
		this.config = config;
	}
	
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}	
	
	public void addConfigured(ChangeTask task) {
		tasks.add(task);
	}
	
	@Override
	public void checkIntegrity(File archiveFile, Manager manager)
			throws BuildException {
		
		if (!archiveFile.exists()) {
			error(ARCHIVE_NOT_EXISTS);
			throw new BuildException(ARCHIVE_NOT_EXISTS);
		}
		
		for (ChangeTask task : tasks) {
			task.checkIntegrity();
		}
	}

	@Override
	public void execute(File archiveFile, Manager manager) {
		loadArchive(archiveFile, manager);
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

	private void loadArchive(File archiveFile, Manager manager) {
		try {
			manager.loadArchive(archiveFile, config);
		} catch (RTTException e) {
			log(COULD_NOT_LOAD, e, Project.MSG_ERR);
			throw new BuildException(e);
		}
	}
	
	private boolean executeTasks(Manager manager) {
		boolean archiveChanged = false;
		for (ChangeTask task : tasks) {
			task.execute(manager);
			if (task.hasArchiveChanged() && !archiveChanged) {
				archiveChanged = true;
			}
		}
		
		return archiveChanged;
	}
	
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
