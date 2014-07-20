package rtt.ant.newTasks;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import rtt.core.manager.Manager;

public abstract class ArchiveChangeAntTask extends Task {	
	
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
