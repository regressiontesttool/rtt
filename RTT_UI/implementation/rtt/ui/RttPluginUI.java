package rtt.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import regression.test.util.TestResourceFactoryImpl;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.ProjectDirectoryContent;
import rtt.ui.core.RttNature;
import rtt.ui.model.RttProject;

/**
 * The activator class controls the plug-in life cycle
 */
public class RttPluginUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "rtt.ui"; //$NON-NLS-1$

	// The shared instance
	private static RttPluginUI plugin;
	
	private ProjectDirectoryContent projectDirectory;
	private List<IRttListener> listeners;
	private ProjectContent currentProjectContent;
	
	/**
	 * The constructor
	 */
	public RttPluginUI() {
		listeners = new ArrayList<IRttListener>();
	}
	
	public static void addListener(IRttListener listener) {
		getDefault().listeners.add(listener);
	}
	
	public static void removeListener(IRttListener listener) {
		getDefault().listeners.remove(listener);
	}
	
	private synchronized void initProjects() {
		List<ProjectContent> projects = new ArrayList<ProjectContent>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			try {
				IProjectDescription description = project.getDescription();
				if (description.hasNature(RttNature.NATURE_ID)) {
					RttProject newProject = new RttProject(project);
					ProjectContent content = new ProjectContent(newProject);
					projects.add(content);
					
					if (currentProjectContent == null) {
						currentProjectContent = content; 
					}
				}				

			} catch (Exception exception) {
				RttLog.log(exception);
			}
		}
		
		projectDirectory = new ProjectDirectoryContent(projects);
	}
	
	public static void addProject(IProject project) {
		getDefault().addProjectInternal(project);
	}
	
	private void addProjectInternal(IProject project) {
		try {
			RttProject newProject = new RttProject(project);
			projectDirectory.addProject(new ProjectContent(newProject));
			
			for (IRttListener listener : listeners) {
				listener.refresh();
			}
		} catch (Exception exception) {
			RttLog.log(exception);
		}
	}
	
	public static void setCurrentProjectContent(ProjectContent newContent) {
		getDefault().setCurrentProjectContentInternal(newContent);
	}
	
	public void setCurrentProjectContentInternal(ProjectContent newContent) {
		if (currentProjectContent != newContent) {
			currentProjectContent = newContent;
			for (IRttListener listener : getDefault().listeners) {
				listener.update(newContent);
			}
		}		
	}	
	
	public static ProjectDirectoryContent getProjectDirectory() {
		return getDefault().projectDirectory;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		initProjects();
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("rtt", new TestResourceFactoryImpl());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static RttPluginUI getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static ProjectContent getCurrentProjectContent() {
		return getDefault().currentProjectContent;
	}
}
