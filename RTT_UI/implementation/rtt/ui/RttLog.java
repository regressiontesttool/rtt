package rtt.ui;

import org.eclipse.core.runtime.IStatus;


public class RttLog {
	
	public static void log(IStatus status) {
		RttPluginUI.getDefault().getLog().log(status);
	}

}
