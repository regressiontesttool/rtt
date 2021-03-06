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
import java.util.LinkedList;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import rtt.core.manager.Manager;
import rtt.core.utils.RTTLogging;

/**
 * 
 * This task is intended for adding and changing configurations in an existing archive.<br>
 * For every configuration, several classpath entries can be added.<br>
 * <br>
 * Example:<br>
 * <br>
 * <pre>
 * &lt;updateConfigurations
 * 	archive="path-to-archive"&gt;
 * 	&lt;configuration
 * 		name="configuration-name"
 * 		initial="initial-node-class"&gt;
 * 		&lt;classpathElement path="classpath-directory"/&gt;
 * 	&lt;/configuration&gt;
 * &lt;/updateConfigurations&gt;
 *</pre>
 * 
 * 
 * 
 * @author Peter Mucha
 * 
 */
public class UpdateConfiguration extends Task {
	public static class Configuration extends Task {
		private String name = null;
		private String initialNode = null;
		private boolean owrite = false;
		private List<ClassPathElement> cpes =
			new LinkedList<ClassPathElement>();
		
		public Configuration() {}
		
		public boolean isOverwrite() {return owrite;}
		public void setOverwrite(boolean owrite) {this.owrite = owrite;}
		public String getName() {return name;}
		public void setName(String name) {this.name = name;}
		public String getInitial() {return initialNode;}
		public void setInitial(String initialNode) {this.initialNode = initialNode;}
		public List<ClassPathElement> getClassPathElements() {return cpes;}
		public void addClassPathElement(ClassPathElement cpe) {
			this.cpes.add(cpe);
		}
	}
	
	public static class ClassPathElement extends Task {
		private String path = "";
		public ClassPathElement() {}
		public String getPath() {return path;}
		public void setPath(String path) {this.path = path;}
	}
	
	private String archive = null;
	private String log = null;
	private List<Configuration> configs = new LinkedList<Configuration>();
	
	public void addConfiguration(Configuration c) {this.configs.add(c);}
	public String getArchive() {return archive;}
	public void setArchive(String archive) {this.archive = archive;}
	public void setLog(String log) {this.log = log;}
	
	public void execute() throws BuildException {
		Manager m = null;
		File archiveFile = new File(archive);
		try {
			m = new Manager(archiveFile, true);
			m.loadArchive(archiveFile);
			RTTLogging.info("Archive loaded");			
			
			for (Configuration c : configs) 
			{
				List<String> cpEntries = new LinkedList<String>(); 
				for (ClassPathElement cpe : c.getClassPathElements())
					cpEntries.add(cpe.getPath());
				
				m.setConfiguration(
						c.getName(),
						c.getInitial(),
						cpEntries,
						false,
						c.isOverwrite()) ;
			}
			
			RTTLogging.info("Save archive to: "+ archive);
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
