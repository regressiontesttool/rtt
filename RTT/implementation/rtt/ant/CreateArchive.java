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
 * 
 * This class represents the Ant-task for building a new Archive. <br>
 * An empty archive will be initialized. A default configuration can be set.<br>
 * According taskdefs are needed<br>
 * <br>
 * Example:<br>
 * <br>
 * <pre>
 * &lt;target name=&quot;create&quot;&gt;
 *	&lt;createArchive
 *			archive="$path-to-resulting-archive"
 *			overwrite="false"
 *			defaultConfiguration="defConfig"&gt;
 *		&lt;/createArchive&gt;
 * &lt;/target&gt;
 *</pre>
 * 
 * 
 * 
 * @author Peter Mucha
 * 
 */
public class CreateArchive extends Task {
	private String archive = null;
	private String dconfig = null;
	private boolean owrite = false;
	private String log = null;
	
	public String getArchive() {return archive;}
	public void setArchive(String archive) {this.archive = archive;}
	public boolean isOverwrite() {return owrite;}
	public void setOverwrite(boolean owrite) {this.owrite = owrite;}
	public String getDefaultConfiguration() {return dconfig;}
	public void setDefaultConfiguration(String c) {this.dconfig = c;}
	public void setLog(String log) {this.log = log;}
	
	public void execute() throws BuildException {
		Manager manager = null;
		File archiveFile = new File(archive);
		try {
			manager = new Manager(archiveFile, true);
			
			boolean loaded = false;
			try {
				if (!owrite) {
					manager.loadArchive(archiveFile);
					RTTLogging.info("Archive loaded");
					loaded = true;
				}
			} catch (Exception e) { /* no archive exists */}
			if (!loaded) {
				RTTLogging.info("Create Archive");
				manager.createArchive(archiveFile);
			}
			
			// Make appropriate log entry if it changed!
			manager.setDefaultConfiguration(dconfig);
			
			
			RTTLogging.info("Save archive to: "+ archive);
			manager.saveArchive(new File(archive));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		} finally {
			if (log != null) {
				try {
					if (manager != null) {
						manager.exportLog(new File(log));
					}					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
