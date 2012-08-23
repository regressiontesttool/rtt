package rtt.ui.content.main;

import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.configuration.ConfigurationDirectory;
import rtt.ui.model.RttProject;

public class ProjectContent extends AbstractContent {

	private RttProject project;

	private ConfigurationDirectory configDirectory;
	private LogDirectory logDirectory;
	private TestsuiteDirectory suiteDirectory;

	public ProjectContent(RttProject project) {
		super(null);

		this.project = project;

		configDirectory = new ConfigurationDirectory(this);		
		logDirectory = new LogDirectory(this);
		suiteDirectory = new TestsuiteDirectory(this);

		loadContents();
	}

	private void loadContents() {
		childs.add(configDirectory);
		childs.add(suiteDirectory);
	}

	public String getText() {
		return project.getName();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.PROJECT;
	}

	@Override
	public final RttProject getProject() {
		return project;
	}
	
	public ConfigurationDirectory getConfigDirectory() {
		return configDirectory;
	}

	public LogDirectory getLogDirectory() {
		return logDirectory;
	}

	public TestsuiteDirectory getTestsuiteDirectory() {
		return suiteDirectory;
	}

	public void reload(ReloadInfo info) {
		logDirectory.reload(info);
		
		if (info.contains(Content.TESTSUITE)) {
			suiteDirectory.reload(info);
			RttPluginUI.getProjectManager().setCurrentContent(this, true);
		}
		
		if (info.contains(Content.TESTCASE)) {
			suiteDirectory.reload(info);
		}
		
		if (info.contains(Content.CONFIGURATION)) {
			configDirectory.reload(info);
		}
		
		RttPluginUI.refreshManager();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectContent other = (ProjectContent) obj;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		return true;
	}	
	
}
