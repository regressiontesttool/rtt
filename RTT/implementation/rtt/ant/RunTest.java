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

/**
 * 
 * This task represents a test run.<br>
 * If matching is true, the different ASTs that are returned will be compared by <br>
 * tree edit distance and the closest ones will be compared for regression testing.<br>
 * <br>
 * Example:<br>
 * <br>
 * <pre>
 * &lt;test
 *		archive="path-to-archive"
 *		configuration="c1"
 *		matching="false"/&gt;
 * </pre>
 * 
 * 
 * 
 * @author Peter Mucha
 * 
 */
public class RunTest extends Task {
	private String archive = null;
	private String config = null;
	private String testsuite = null;
	private boolean matching = false;
	private String log = null;

	public String getTestSuite() {return testsuite;}
	public void setLog(String log) {this.log = log;}
	public void setTestSuite(String suite) {this.testsuite = suite;}
	public boolean isMatching() {return matching;}
	public void setMatching(boolean matching) {this.matching = matching;}
	public String getArchive() {return archive;}
	public void setArchive(String archive) {this.archive = archive;}
	public String getConfiguration() {return config;}
	public void setConfiguration(String config) {this.config = config;}
	
	public void execute() throws BuildException {
		if (archive == null || archive.length() == 0)
			throw new BuildException("Parameter <path> is required!");
		log("Testing Path: <" + archive + ">");
		
		boolean noErrors = false;
		File archivePath = new File(archive);
		Manager m = new Manager(archivePath, true);
		try {
			if (config != null && config.length() > 0)
				m.loadArchive(config);
			else
				m.loadArchive();
			log("Archive loaded");
			noErrors = m.runTests(getTestSuite(), matching);
			m.saveArchive(archivePath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BuildException(e.toString());
		} finally {
			if (log != null) {
				try {
					m.exportLog(new File(log));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!noErrors) {
			throw new BuildException(
					"There were errors during regression Testing.");
		}
	}
}
