package rtt.ui.core.internal.treeItem;

import java.util.List;

import rtt.ui.core.IRttProject;
import rtt.ui.core.archive.IConfiguration;
import rtt.ui.core.archive.ILog;
import rtt.ui.core.archive.ITestSuite;

public class ProjectTreeItem extends AbstractTreeItem {
	
	public enum ChildType {
		CONFIGURATION,
		TESTSUITE,
		LOG
	}
	
	private IRttProject project;
	private ChildType type;
	
	public ProjectTreeItem(IRttProject project, ChildType type) {
		super(null);
		setName(project.getName());
		setIcon(TreeItemIcon.RTT_PROJECT);
		this.project = project;
		this.type = type;
	}
	
	@Override
	public Object[] getChildren() {
		switch (type) {
		case CONFIGURATION:
			return getConfigurations();
		
		case TESTSUITE:
			return getTestSuites();
			
		case LOG:
			return getLog();

		default:
			throw new IllegalArgumentException("ChildType '" + type + "' is not supported by ProjectTreeItem");
		}
	}
	
	public Object[] getConfigurations() {
		List<IConfiguration> configList = project.getConfigurations();
		
		if (configList.size() > 0) {
			ConfigurationTreeItem[] result = new ConfigurationTreeItem[configList.size()];
			int i = 0;
			
			for (IConfiguration config : configList) {
				result[i] = new ConfigurationTreeItem(this, config);
				i++;
			}
			
			return result;
		}
		
		return new String[] { "No configurations available." };
	}
	
	public Object[] getTestSuites() {
		List<ITestSuite> suiteList = project.getTestSuites();
		
		if (suiteList.size() > 0) {
			TestSuiteTreeItem[] result = new TestSuiteTreeItem[suiteList.size()];
			int i = 0;
			
			for (ITestSuite suite : suiteList) {
				result[i] = new TestSuiteTreeItem(this, suite);
				i++;
			}
			
			return result;
		}
		
		return new String[] { "No test suites available." };	
	}
	
	public Object[] getLog() {
		ILog log = project.getLog();
		if (log != null) {
			
			//CHRISTIAN
			LogTreeItem[] result = new LogTreeItem[1];
			result[0] = new LogTreeItem(this, "Log found.");
			
			return result;
		}
		
		return new String[] { "No log available" };
	}
	
	@Override
	public IRttProject getProject() {
		return project;
	}
}
