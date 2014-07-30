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
import rtt.core.utils.GenerationInformation;
import rtt.core.utils.RTTLogging;

/**
 * Task for regenerating the results.<br>
 * New results or all results will be generated.<br>
 * Example:<br>
 * <br>
 * 
 * <pre>
 * &lt;updateTests
 * 	archive="path-to-archive"
 * 	configuration="configuration-name"
 * /&gt;
 * </pre>
 * 
 * @author Peter Mucha
 * 
 */
public class UpdateTests extends Task {
	private String archive = null;
	private String config = null;
	private String testsuite;
	private String log = null;
	
	public String getTestSuite() {return testsuite;}
	public void setTestSuite(String testSuite) {this.testsuite = testSuite;}
	public String getArchive() {return archive;}
	public void setArchive(String archive) {this.archive = archive;}
	public String getConfiguration() {return config;}
	public void setConfiguration(String config) {this.config = config;}
	public void setLog(String log) {this.log = log;}

	public void execute() throws BuildException {
		if (archive == null || archive.length() == 0)
			throw new BuildException("Parameter <path> is required!");

		RTTLogging.info("regenerating Archive: <" + archive + ">");
		Manager m = null;
		File archiveFile = new File(archive);
		
		try {
			m = new Manager(archiveFile, true);
			if (config != null && config.length() > 0)
				m.loadArchive(archiveFile, config);
			else
				m.loadArchive(archiveFile);
			RTTLogging.info("Archive loaded (Configuration: " + config + ")");
			RTTLogging.info("Generating Testresults for Testcases");
			GenerationInformation infos = m.generateTests(getTestSuite());
			if (infos.hasErrors()) {
				throw new BuildException("Error during data generation");
			}
			m.saveArchive(new File(archive));
		} catch (Throwable e) {
			e.printStackTrace();
			throw new BuildException(e.getMessage());
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
