package rtt.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

import regression.test.TestPackage;
import regression.test.util.TestResourceFactoryImpl;
import rtt.ui.content.ReloadInfo;
import rtt.ui.content.ReloadInfo.Content;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.content.main.ProjectDirectory;
import rtt.ui.content.main.TestsuiteDirectory;
import rtt.ui.content.testsuite.TestsuiteContent;
import rtt.ui.core.RttNature;
import rtt.ui.model.RttProject;
import rtt.ui.utils.RttLog;
import rtt.ui.views.utils.RttListenerManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class RttPluginUI extends EMFPlugin {
	
	// The plug-in ID
	public static final String PLUGIN_ID = "rtt.ui"; //$NON-NLS-1$
	
	public static final RttPluginUI INSTANCE = new RttPluginUI();

	public static final String IMG_VERTICAL = "icons/th_vertical.gif";
	public static final String IMG_HORIZONTAL = "icons/th_horizontal.gif";
	
	private static Implementation plugin;
	
	public static class Implementation extends EclipseUIPlugin implements IResourceChangeListener {
		
		private ProjectDirectory projectDirectory;
		private RttListenerManager<ProjectContent> projectManager;
		private RttListenerManager<TestsuiteContent> suiteManager;
		
		public Implementation() {
			super();
			plugin = this;
			
			suiteManager = new RttListenerManager<TestsuiteContent>();
			projectManager = new RttListenerManager<ProjectContent>() {
				@Override
				protected void additionalOperations(ProjectContent content) {
					if (content != null) {
						TestsuiteDirectory suiteDirectory = content
								.getTestsuiteDirectory();
						if (suiteDirectory != null) {
							TestsuiteContent suiteContent = suiteDirectory
									.getTestsuite(0);
							if (suiteContent != null) {
								suiteManager.setCurrentContent(suiteContent, true);
							}
						}
					}
				}
			};
		}
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
		 * )
		 */
		public void start(BundleContext context) throws Exception {
			super.start(context);
			plugin = this;

			initProjects();
			ResourcesPlugin.getWorkspace().addResourceChangeListener(
					this, IResourceChangeEvent.PRE_DELETE	| IResourceChangeEvent.PRE_CLOSE);				
			
			Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
					"rtt", new TestResourceFactoryImpl());
			EPackage.Registry.INSTANCE.put(TestPackage.eNS_URI, TestPackage.eINSTANCE);
		}	

		private synchronized void initProjects() {
			List<ProjectContent> projects = new ArrayList<ProjectContent>();
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

			for (IProject project : root.getProjects()) {
				try {
					IProjectDescription description = project.getDescription();
					if (description.hasNature(RttNature.NATURE_ID)
							&& description.hasNature(JavaCore.NATURE_ID)) {
						IJavaProject javaProject = JavaCore.create(project);
						RttProject newProject = new RttProject(javaProject);
						
						ProjectContent content = new ProjectContent(newProject);
						projects.add(content);

						if (projectManager.getCurrentContent() == null) {
							projectManager.setCurrentContent(content);
						}						
					}

				} catch (Exception exception) {
					RttLog.log(exception);
				}
			}

			projectDirectory = new ProjectDirectory(projects);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
		 * )
		 */
		public void stop(BundleContext context) throws Exception {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);		
			
			plugin = null;
			super.stop(context);
		}
		
		@Override
		public void resourceChanged(IResourceChangeEvent event) {

			IResource resource = event.getResource();
			if (resource instanceof IProject) {
				IProject project = (IProject) resource;

				projectDirectory.removeProject(project);
				projectDirectory.reload(new ReloadInfo(Content.PROJECT));
			}
			
			System.out.println(resource.toString());
		}
	}
	
	public RttPluginUI() {
		super(new ResourceLocator[]{});
	}

	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}
	
	public static Implementation getPlugin() {
		return plugin;
	}
	
	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return Implementation.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static ProjectDirectory getProjectDirectory() {
		return getPlugin().projectDirectory;
	}

	public static RttListenerManager<ProjectContent> getProjectManager() {
		return getPlugin().projectManager;
	}

	public static RttListenerManager<TestsuiteContent> getSuiteManager() {
		return getPlugin().suiteManager;
	}
	
	public static void refreshManager() {
		getProjectManager().refreshListener();
		getSuiteManager().refreshListener();
	}	
}
