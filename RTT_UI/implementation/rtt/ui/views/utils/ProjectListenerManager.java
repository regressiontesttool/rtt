package rtt.ui.views.utils;

import rtt.ui.RttPluginUI;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.TestsuiteDirectory;
import rtt.ui.content.testsuite.TestsuiteContent;

public class ProjectListenerManager extends
		RttListenerManager<ProjectContent> {

	public ProjectListenerManager() {}
	
	private boolean setFirstTestsuite(ProjectContent content) {
		TestsuiteDirectory suiteDirectory = content.getTestsuiteDirectory();
		if (suiteDirectory != null) {
			TestsuiteContent suiteContent = suiteDirectory.getTestsuite(0);
			if (suiteContent != null) {
				return RttPluginUI.getSuiteManager().setCurrentContent(suiteContent);
			}
		}
		
		return false;
	}

	public boolean setCurrentContent(ProjectContent newContent) {
		if (super.setCurrentContent(newContent) == true) {
			return setFirstTestsuite(newContent);
		}

		return false;
	}
}
