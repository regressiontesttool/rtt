package rtt.ui.content.main;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IProject;

import rtt.ui.RttPluginUI;
import rtt.ui.content.IContent;
import rtt.ui.model.RttProject;

public class ProjectContentDirectory extends AbstractContent {
	
	private List<ProjectContent> projectList;
	private boolean hasChanged;

	public ProjectContentDirectory(Collection<ProjectContent> projects) {
		super(null);
		projectList = new LinkedList<ProjectContent>();
		childs = null;
		
		for (ProjectContent projectContent : projects) {
			addProject(projectContent);
		}
	}
	
	@Override
	public IContent[] getChildren() {
		return projectList.toArray(new IContent[projectList.size()]);
	}
	
	public void addProject(ProjectContent projectContent) {
		if (!projectList.contains(projectContent)) {
			projectList.add(projectContent);
		}
		
		this.hasChanged = true;
	}
	
	public void removeProject(IProject project) {
		ProjectContent projectContent = findProjectContent(project);
		if (projectContent != null) {
			projectList.remove(projectContent);
			
			RttProject rttProject = projectContent.getProject();			
			rttProject.close();
			
			this.hasChanged = true;
		}
	}
	
	private ProjectContent findProjectContent(IProject project) {
		for (ProjectContent projectContent : projectList) {
			if (projectContent.getProject().getJavaProject().getProject() == project) {
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
}
