package rtt.ui.content.main;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;

import rtt.ui.RttPluginUI;
import rtt.ui.content.IContent;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.model.RttProject;
import rtt.ui.utils.RttLog;

// TODO check possible refactorings

public class ProjectDirectory extends AbstractContent {

	private List<ProjectContent> projectList;
	private boolean hasChanged = false;
	private boolean needToReload = false;

	public ProjectDirectory(Collection<ProjectContent> projects) {
		super(null);
		projectList = new LinkedList<ProjectContent>();
		childs = null;

		for (ProjectContent projectContent : projects) {
			addProjectInternal(projectContent);
		}
	}

	@Override
	public IContent[] getChildren() {
		return projectList.toArray(new IContent[projectList.size()]);
	}

	private void addProjectInternal(ProjectContent projectContent) {
		if (!projectList.contains(projectContent)) {
			projectList.add(projectContent);
		}

		this.hasChanged = true;
	}
	
	public void addProject(IJavaProject project) {
		if (project == null) {
			throw new IllegalArgumentException("The project must not be null.");
		}
		
		try {				
			RttProject newProject = new RttProject(project);
			newProject.createArchive(newProject.getArchiveFile());
			this.addProjectInternal(new ProjectContent(newProject));
		} catch (Exception exception) {
			RttLog.log(exception);
		}
	}

	public void removeProject(IProject project) {
		ProjectContent projectContent = findProjectContent(project);
		if (projectContent != null) {
			projectList.remove(projectContent);

			RttProject rttProject = projectContent.getProject();
			rttProject.close(true);

			this.hasChanged = true;
		}
	}

	public ProjectContent findProjectContent(IProject project) {
		for (ProjectContent projectContent : projectList) {
			if (projectContent.getProject().getIProject() == project) {
				return projectContent;
			}
		}

		return null;
	}

	@Override
	public String getText() {
		return "ProjectDirectory";
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.PLACEHOLDER;
	}

	public boolean hasChanged() {		
		if (needToReload) {
			needToReload = false;
			
			for (ProjectContent content : projectList) {
				RttProject project = content.getProject();
				project.close(false);
			}
		}
		
		if (hasChanged) {
			hasChanged = false;

			ProjectContent newContent = null;
			if (projectList.size() > 0) {
				newContent = projectList.get(0);
			}
			RttPluginUI.getProjectManager().setCurrentContent(newContent, true);

			return true;
		}

		return false;
	}
	
	public void reload(ReloadInfo info) {
		if (info.contains(Content.PROJECT)) {
			for (ProjectContent projectContent : projectList) {
				projectContent.reload(new ReloadInfo(Content.TESTSUITE, Content.LOG, Content.CONFIGURATION));
			}
		}
	}

	public void needToReload() {
		hasChanged = true;
		needToReload = true;
	}
}
