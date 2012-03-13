package rtt.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import regression.test.util.TestResourceFactoryImpl;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.ProjectDirectoryContent;
import rtt.ui.content.testsuite.TestsuiteContent;
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
	private RttListenerManager<ProjectContent> projectManager;
	private RttListenerManager<TestsuiteContent> suiteManager;
	
	/**
	 * The constructor
	 */
	public RttPluginUI() {
		suiteManager = new RttListenerManager<TestsuiteContent>();
	}
	
	public static void addProject(IProject project) {
		getDefault().addProjectInternal(project);
	}
	
	private void addProjectInternal(IProject project) {
		try {
			if (project.hasNature(JavaCore.NATURE_ID)) {
				IJavaProject javaProject = JavaCore.create(project);
				RttProject newProject = new RttProject(javaProject);
				projectDirectory.addProject(new ProjectContent(newProject));
				refreshManager();
			}			
		} catch (Exception exception) {
			RttLog.log(exception);
		}
	}
	
	public static void refreshManager() {
		getProjectManager().refreshListener();
		getSuiteManager().refreshListener();
	}
	
	public static ProjectDirectoryContent getProjectDirectory() {
		return getDefault().projectDirectory;
	}
	
	public static RttListenerManager<ProjectContent> getProjectManager() {
		return getDefault().projectManager;
	}
	
	public static RttListenerManager<TestsuiteContent> getSuiteManager() {
		return getDefault().suiteManager;
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
	
	private synchronized void initProjects() {
		List<ProjectContent> projects = new ArrayList<ProjectContent>();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		for (IProject project : root.getProjects()) {
			try {
				IProjectDescription description = project.getDescription();
				if (description.hasNature(RttNature.NATURE_ID) && description.hasNature(JavaCore.NATURE_ID)) {
					IJavaProject javaProject = JavaCore.create(project);
					RttProject newProject = new RttProject(javaProject);
					ProjectContent content = new ProjectContent(newProject);
					projects.add(content);
					
					if (projectManager == null) {
						projectManager = new RttListenerManager<ProjectContent>(content);
					}
				}				

			} catch (Exception exception) {
				RttLog.log(exception);
			}
		}
		
		projectDirectory = new ProjectDirectoryContent(projects);
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
}
