/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.ant;

import java.util.*;

import org.apache.tools.ant.*;

/**
 * This task is intended for running all included tests. It will still continue<br>
 * running following tests, if one test failes.<br>
 * If fail_with_exception is set to true, an BuildException is thrown at the end of all testcases, if one of them failed.<br>
 * By default, fail_with_exception is set to true resulting in failed builds, if errors in the regression test occured.<br>
 * 
 * 
 * @author Peter Mucha
 * 
 */
public class RunTests extends Task {
	private List<RunTest> toTest = new LinkedList<RunTest>();
	private String log = null;
	private boolean fail_with_exception = true;
	
	
	public void setLog(String log) {this.log = log;}
	public void addTest(RunTest t) {this.toTest.add(t);}
	public void setFail_with_exception(boolean f) {this.fail_with_exception = f;}
	
	public void execute() throws BuildException {
		boolean errorsOccured = false;
		for (RunTest task : toTest) {
			try {
				if (log != null)
					task.setLog(log);
				task.execute();
			} catch (Exception e) {
				errorsOccured = true;
			}
		}
		if (errorsOccured && fail_with_exception)
			throw new BuildException(
					"There were errors during regression Testing.");
	}
}
