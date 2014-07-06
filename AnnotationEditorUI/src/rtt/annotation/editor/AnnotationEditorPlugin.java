package rtt.annotation.editor;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class AnnotationEditorPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "AnnotationEditorUI"; //$NON-NLS-1$

	public static final String ANNOTATED_COLOR = "ANNOTATED_COLOR";
	public static final String EXTENDED_COLOR = "EXTENDED_COLOR";
	
	public static final String COMPARE_COLOR = "COMPARE_COLOR";
	public static final String INFORMATIONAL_COLOR = "INFORMATIONAL_COLOR";

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
		
		JFaceResources.getColorRegistry().put(ANNOTATED_COLOR, new RGB(0, 0, 255));
		JFaceResources.getColorRegistry().put(EXTENDED_COLOR, new RGB(255, 0, 0));
		
		JFaceResources.getColorRegistry().put(COMPARE_COLOR, new RGB(0, 0, 255));
		JFaceResources.getColorRegistry().put(INFORMATIONAL_COLOR, new RGB(0, 155, 255));
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

}
