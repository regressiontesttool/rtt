package rtt.ui.content.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.dialogs.ContainerGenerator;

import rtt.core.classpath.RTTClasspathContainer;
import rtt.core.manager.Manager;
import rtt.ui.RttLog;
import rtt.ui.RttPluginUI;
import rtt.ui.RttPreferenceStore;
import rtt.ui.content.IContent;
import rtt.ui.core.RttNature;
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

	private void addProject(ProjectContent projectContent) {
		if (!projectList.contains(projectContent)) {
			projectList.add(projectContent);
		}

		this.hasChanged = true;
	}
	
	public void addProject(IJavaProject javaProject) {
		if (javaProject != null) {			
			try {				
				RttProject newProject = new RttProject(javaProject);
				this.addProject(new ProjectContent(newProject));
			} catch (Exception exception) {
				RttLog.log(exception);
			}			
		}
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

	public ProjectContent findProjectContent(IProject project) {
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
