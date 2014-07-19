package rtt.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class AbstractAntTask extends Task {
	
	private static final String NO_ARCHIVE_ATTRIBUTE =			
			"Archive attribute must be set.";	
	private static final String NO_INPUT_ARCHIVE =
			"No archive file was set.";
	private static final String NO_OUTPUT_ARCHIVE =
			"The output attribute was used, but no file was set.";	
	
	private String inputArchive = null;
	private String outputArchive = null;
	private boolean overwrite = true;	
	
	public void setArchive(String archive) {
		this.inputArchive = archive;
	}
	
	public void setOutput(String output) {
		this.outputArchive = output;
	}
	
	public void setOverwrite(boolean overwrite) {
		this.overwrite = overwrite;
	}

	@Override
	public void execute() throws BuildException {
		checkArchiveAttribute();
		
		File archiveFile = getArchiveFile(inputArchive, NO_INPUT_ARCHIVE);
		info("Using archive: " + archiveFile);
		
		if (outputArchive != null) {
			File outputFile = getArchiveFile(outputArchive, NO_OUTPUT_ARCHIVE);
			info("Saving to: " + outputFile);
		}
	}

	private void checkArchiveAttribute() {
		if (inputArchive == null) {
			error(NO_ARCHIVE_ATTRIBUTE);
			throw new BuildException(NO_ARCHIVE_ATTRIBUTE);
		}
	}

	protected File getArchiveFile(String archivePath, String errorMessage) {
		if (archivePath == null || archivePath.equals("")) {
			error(errorMessage);
			throw new BuildException(errorMessage);
		}
		
		return new File(archivePath);
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
