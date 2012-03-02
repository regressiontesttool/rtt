package rtt.ui;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import regression.test.util.TestResourceFactoryImpl;
import rtt.ui.core.ProjectFinder;

/**
 * The activator class controls the plug-in life cycle
 */
public class RttPluginUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "rtt.ui"; //$NON-NLS-1$

	// The shared instance
	private static RttPluginUI plugin;
	
	/**
	 * The constructor
	 */
	public RttPluginUI() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("rtt", new TestResourceFactoryImpl());
		
//		ProjectRegistry.loadData();
		ProjectFinder.loadProjects();
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
