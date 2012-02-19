package rtt.ui.core.internal.model;

import java.io.File;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

import rtt.ui.RttPluginUtil;
import rtt.ui.core.IRttProject;
import rtt.ui.core.archive.IArchive;
import rtt.ui.core.archive.IConfiguration;
import rtt.ui.core.archive.ILog;
import rtt.ui.core.archive.ITestSuite;

public class XMLRttProject implements IRttProject {
	
	private IProject project;
	private IArchive archive;
	private File archiveFile;
	private String activeConfiguration;

	public XMLRttProject(IProject project) {
		
		this.project = project;		
		archiveFile = RttPluginUtil.getArchiveFile(project);
		archive = RttPluginUtil.getArchive(project);
	}

	@Override
	public String getName() {
		try {
			return project.getDescription().getName();
		} catch (CoreException e) {
			return "Project Description not found";
		}
	}

	@Override
	public IArchive getArchive() {
		return archive;
	}

	@Override
	public ILog getLog() {
		return archive.getArchiveLog();
	}

	@Override
	public List<IConfiguration> getConfigurations() {
		return archive.getConfigurations();
	}

	@Override
	public List<ITestSuite> getTestSuites() {
		return archive.getTestSuites();
	}

	@Override
	public File getArchiveFile() {
		return archiveFile;
	}

	@Override
	public IProject getWorkspaceProject() {
		return project;
	}

	@Override
	public void reload() {
		archive = RttPluginUtil.getArchive(project, activeConfiguration);
	}

	@Override
	public void setActiveConfiguration(IConfiguration activeConfig) {
		this.activeConfiguration = "";
		for (IConfiguration config : getConfigurations()) {
			config.setActive(false);
			
			if (activeConfig.getName().equals(config.getName())) {
				config.setActive(true);
				this.activeConfiguration = config.getName();
			}
		}
	}
}
