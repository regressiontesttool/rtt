package rtt.ui.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public class ProjectRegistry {
	
	public static final ProjectRegistry INSTANCE = new ProjectRegistry();
	
	private List<IRttProject> projects;
	private Map<String, IModelObserver> observers;
	
	private ProjectRegistry() {
		projects = new ArrayList<IRttProject>();		
		observers = new HashMap<String, IModelObserver>();
	}
	
	public static void loadData() {
		INSTANCE.loadDataInternal();
	}
	
	private synchronized void loadDataInternal() {
		projects.clear();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			IRttProject rttProject = ProjectFactory.create(project);
			
			if (rttProject != null) {
				projects.add(rttProject);
			}			
		}
	}
	
	public static void addObserver(IModelObserver observer) {
		INSTANCE.addObserverInternal(observer);
	}
	
	private synchronized void addObserverInternal(IModelObserver observer) {
		observers.put(observer.getID(), observer);
	}
	
	public static void removeObserver(IModelObserver observer) {
		INSTANCE.removeObserverInternal(observer);
	}
	
	private synchronized void removeObserverInternal(IModelObserver observer) {
		observers.remove(observer.getID());
	}
	
	public static List<IRttProject> getProjects() {
		return INSTANCE.getProjectsInternal();
	}
	
	private List<IRttProject> getProjectsInternal() {
		return projects;
	}

	protected static synchronized void fireModelChanged(IRttProject project) {
		project.reload();
		
		for (IModelObserver observer : INSTANCE.observers.values()) {
			observer.update(project);
		}
	}
}
