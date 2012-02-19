package rtt.ui.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import rtt.ui.content.IContent;
import rtt.ui.content.IContentObserver;
import rtt.ui.content.internal.ProjectContent;
import rtt.ui.model.RttProject;

public class ProjectFinder implements IContentObserver {

	private static final ProjectFinder _instance = new ProjectFinder();
	private ProjectContent currentProjectContent = null;
	private Map<String, ProjectContent> projects;

	private List<IProjectListener> listeners;

	private ProjectFinder() {
		projects = new TreeMap<String, ProjectContent>();
		listeners = new ArrayList<IProjectListener>();
	}

	public static void loadProjects() {
		_instance.loadProjectsInternal();
	}

	private void loadProjectsInternal() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			try {

				RttProject newProject = new RttProject(project);
				ProjectContent content = new ProjectContent(newProject);
				projects.put(newProject.getName(), content);

				content.addObserver(this);

				if (currentProjectContent == null && content != null) {
					currentProjectContent = content;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static ProjectContent getCurrentProjectContent() {
		return _instance.currentProjectContent;
	}

	public static void setCurrentProjectContent(ProjectContent newContent) {
		_instance.setCurrentProjectContentInternal(newContent);
	}

	private void setCurrentProjectContentInternal(ProjectContent newContent) {
		if (newContent != null && newContent != currentProjectContent) {
			currentProjectContent = newContent;

			fireProjectContentChange(newContent);
		}
	}

	public static Collection<ProjectContent> getProjectContents() {
		return _instance.projects.values();
	}

	public static boolean addProjectListener(IProjectListener listener) {
		return _instance.addProjectListenerInternal(listener);
	}

	private boolean addProjectListenerInternal(IProjectListener listener) {
		return listeners.add(listener);
	}

	public static boolean removeProjectListener(IProjectListener listener) {
		return _instance.removeProjectListenerInternal(listener);
	}

	private boolean removeProjectListenerInternal(IProjectListener listener) {
		return listeners.remove(listener);
	}

	@Override
	public String getObserverID() {
		return "rtt.ui.core.ProjectFinder";
	}

	@Override
	public void update(IContent content) {
		System.out.println("FinderUpdate: " + content);
		ProjectContent projectContent = content
				.getContent(ProjectContent.class);
		if (projectContent != null
				&& projects.containsKey(projectContent.getText())) {
			fireProjectContentChange(projects.get(projectContent.getText()));
		}
	}

	private void fireProjectContentChange(ProjectContent projectContent) {
		System.out.println("FireProjectContentChange:" + projectContent);
		for (IProjectListener listener : listeners) {
			listener.updateContent(currentProjectContent);
		}
	}

	public static void fireChangeEvent() {
		_instance.fireProjectContentChange(_instance.currentProjectContent);
	}

}
