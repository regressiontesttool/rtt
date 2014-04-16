package rtt.ui.content.main;

import org.eclipse.core.resources.IFile;

import rtt.core.exceptions.RTTException;
import rtt.core.manager.Manager;
import rtt.ui.RttPluginUI;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.configuration.ConfigurationDirectory;
import rtt.ui.content.logging.LogDirectory;
import rtt.ui.model.RttProject;

public class ProjectContent extends AbstractContent {

	private RttProject project;

	private ConfigurationDirectory configDirectory;
	private LogDirectory logDirectory;
	private TestsuiteDirectory suiteDirectory;

	public ProjectContent(RttProject project) throws RTTException {
		super(null);

		this.project = project;
		configDirectory = new ConfigurationDirectory(this);
		suiteDirectory = new TestsuiteDirectory(this);
		logDirectory = new LogDirectory(this);		
		
		reload(new ReloadInfo(Content.PROJECT));
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
		IFile archiveFile = project.getArchiveFile();
		
		// if archive exists update data
		if (archiveFile.exists()) {
			
			// check if childs contains config and suitedirectory 
			if (!childs.contains(configDirectory) 
					|| childs.contains(suiteDirectory)) {
				
				childs.clear();
				childs.add(configDirectory);
				childs.add(suiteDirectory);
			}
			
			// reload data
			Manager manager = null;
			try {
				manager = project.getManager();
				if (manager != null) {
					logDirectory.reload(info, manager);
					
					if (info.contains(Content.TESTSUITE) || info.contains(Content.PROJECT)) {
						suiteDirectory.reload(info, manager);
					}
					
					if (info.contains(Content.TESTCASE) || info.contains(Content.PROJECT)) {
						suiteDirectory.reload(info, manager);
					}
					
					if (info.contains(Content.CONFIGURATION) || info.contains(Content.PROJECT)) {
						configDirectory.reload(info, manager);
					}
				}		
			} catch (RTTException e) {
				throw new RuntimeException("Could not open manager.");
			} finally {
				if (manager != null) {
					manager.close();
				}
			}
			
		// if archive does not exist, then display warning item
		} else {
			logDirectory = new LogDirectory(this);
			
			childs.clear();
			childs.add(new ArchiveContent(this));			
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
