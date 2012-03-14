package rtt.ui.views.utils;

import rtt.ui.RttPluginUI;
import rtt.ui.content.main.ProjectContent;

public abstract class AbstractProjectListener implements IRttListener<ProjectContent> {
	
	public AbstractProjectListener() {
		RttPluginUI.getProjectManager().addListener(this);
		update(RttPluginUI.getProjectManager().getCurrentContent());
	}
	
	public void removeListener() {
		RttPluginUI.getProjectManager().removeListener(this);
	}

	@Override
	public void refresh() {}

}
