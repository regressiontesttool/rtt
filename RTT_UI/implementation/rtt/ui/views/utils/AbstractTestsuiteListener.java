package rtt.ui.views.utils;

import rtt.ui.RttPluginUI;
import rtt.ui.content.testsuite.TestsuiteContent;

public abstract class AbstractTestsuiteListener implements
		IRttListener<TestsuiteContent> {
	
	public AbstractTestsuiteListener() {
		RttPluginUI.getSuiteManager().addListener(this);
		update(RttPluginUI.getSuiteManager().getCurrentContent());
	}
	
	public void removeListener() {
		RttPluginUI.getSuiteManager().removeListener(this);
	}
}
