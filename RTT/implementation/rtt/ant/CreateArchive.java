/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.ant;

import java.io.*;

import org.apache.tools.ant.*;

import rtt.managing.Manager;

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
		Manager m = new Manager(new File(archive), true);
		try {
			boolean loaded = false;
			try {
				if (!owrite) {
					m.loadArchive();
					System.out.println("Archive loaded");
					loaded = true;
				}
			} catch (Exception e) { /* no archive exists */}
			if (!loaded) {
				System.out.println("Create Archive");
				m.createArchive();
			}
			
			// Make appropriate log entry if it changed!
			m.setDefaultConfiguration(dconfig);
			
			
			System.out.println("Save archive to: "+ archive);
			m.saveArchive(new File(archive));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e);
		} finally {
			if (log != null) {
				try {
					m.exportLog(new File(log));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
