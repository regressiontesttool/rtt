/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.ant;

import java.io.*;
import java.util.*;

import org.apache.tools.ant.*;
import org.apache.tools.ant.types.FileSet;

import rtt.annotations.Lexer;
import rtt.managing.Manager;


/**
 * This task is intended to update existing archives. <br>
 * An update can be adding, removing or changing tests and testsuites.<br>
 * No new results will be generated, if a testcase is changed.<br>
 * See {@link rtt.ant.UpdateTests} Task.<br>
 * <br>
 * Example:<br>
 * <br>
 * <pre>
 * &lt;updateArchive
 *	archive="path-to-archive"&gt;
 *	&lt;testsuite name="name-of-testsuite" overwrite="false"&gt;
 *		&lt;fileset dir="testfile-directory" casesensitive="yes"&gt;
 *			&lt;include name="*.testcase"/&gt;
 *		&lt;/fileset&gt;
 *	&lt;/testsuite&gt;
 * &lt;/updateArchive&gt;
 * </pre>
 * 
 * 
 * @author Peter Mucha
 *
 */
public class UpdateArchive extends Task {
	public static class Testcase extends Task {
		private String testcase = null;
		private String testsuit = null;
		
		public Testcase() {}
		
		public String getTestsuit() {return testsuit;}
		public void setTestsuit(String testsuit) {this.testsuit = testsuit;}
		public String getTestcase() {return testcase;}
		public void setTestcase(String testcase) {this.testcase = testcase;}
	}
	
	public static class Testsuite extends Task {
		private String name;
		private boolean owrite = false;
		private Vector<FileSet> filesets = new Vector<FileSet>();
		
		public Testsuite() {}
		
		public String getName() {return name;}
		public void setName(String name) {this.name = name;}
		public boolean isOverwrite() {return owrite;}
		public void setOverwrite(boolean owrite) {this.owrite = owrite;}
		
		public void addFileSet(FileSet fileset) {
			if (!filesets.contains(fileset))
				filesets.add(fileset);
		}
		
		public List<File> getFiles() {
			List<File> result = new LinkedList<File>();
			int filesProcessed = 0;
			DirectoryScanner ds;
			for (FileSet fileset : filesets) {
				ds = fileset.getDirectoryScanner(getProject());
				File dir = ds.getBasedir();
				String[] filesInSet = ds.getIncludedFiles();
				for (String filename : filesInSet) {
					File file = new File(dir, filename);
					result.add(file);
					filesProcessed++;
				}
			}
			return result;
		}
	}
	
	private String archive = null;
	private List<Testsuite> toUpdate = new LinkedList<Testsuite>();
	private List<Testcase> toRemove = new LinkedList<Testcase>();
	private String log = null;
	
	public String getArchive() {return archive;}
	public void setArchive(String archive) {this.archive = archive;}
	public void addTestsuite(Testsuite ts) {toUpdate.add(ts);}
	public void addRemoveTest(Testcase tc) {toRemove.add(tc);}
	public String getLog() {return log;}
	public void setLog(String log) {this.log = log;}
	
	public void execute() throws BuildException { 
		Manager m = new Manager(new File(archive), true);
		try {
			m.loadArchive();
			System.out.println("Archive loaded");

			for (Testsuite t : toUpdate) {
				int mode = t.isOverwrite() ? 3 : 0;
				List<File> fs = t.getFiles();
				// Create test suite, iff it does not exist yet:
				
				m.createTestSuite(t.name);
				// Add new or changed test cases:
				m.addAllFiles(fs, t.name, mode);
			}
			for (Testcase tc : toRemove)
				m.removeTest(tc.testsuit, tc.testcase);

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
