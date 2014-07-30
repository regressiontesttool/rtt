/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.ant;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import rtt.core.manager.Manager;
import rtt.core.utils.RTTLogging;

/**
 * Task for exporting a log and its stylesheet from an archive<br>
 * <br>
 * Example: <br>
 *  
 *  <pre>
 *  &lt;exportLog archive="path-to-archive" destination="directory/"&gt;
 *  </pre>
 * 
 * 
 * 
 * @author Peter Mucha
 * 
 */
public class ExportLog extends Task {
	private String archive = null;
	private String dest = null;
	
	public String getArchive() {return archive;}
	public void setArchive(String archive) {this.archive = archive;}
	public String getDestination() {return dest;}
	public void setDestination(String dest) {this.dest = dest;}
	
	public void execute() throws BuildException {
		if (archive == null || archive.length() == 0)
			throw new BuildException("Parameter <path> is required!");
		if (dest == null)
			dest = ".";
		RTTLogging.info("Loading Archive: <" + archive + ">");
		
		File archiveFile = new File(archive);
		
		try {
			Manager manager = new Manager(archiveFile, true);
			manager.loadArchive(archiveFile);
			RTTLogging.info("Archive loaded");
			manager.exportLog(new File(dest));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		}
	}
}
