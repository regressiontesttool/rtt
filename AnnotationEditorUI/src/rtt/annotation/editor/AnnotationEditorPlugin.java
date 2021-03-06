package rtt.annotation.editor;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class AnnotationEditorPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "AnnotationEditorUI"; //$NON-NLS-1$

	// The shared instance
	private static AnnotationEditorPlugin plugin;
	
	/**
	 * The constructor
	 */
	public AnnotationEditorPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
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
	public static AnnotationEditorPlugin getDefault() {
		return plugin;
	}

	public static void log(int severity, String message) {
		plugin.getLog().log(new Status(severity, PLUGIN_ID, message));
	}
	
	public static void logException(String message, Throwable throwable) {
		plugin.getLog().log(new Status(Status.ERROR, PLUGIN_ID, message, throwable));
	}

}
