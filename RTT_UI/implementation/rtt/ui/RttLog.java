package rtt.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class RttLog {

	public static void log(IStatus status) {
		RttPluginUI.getDefault().getLog().log(status);
	}

	public static void log(Throwable throwable) {
		log(new Status(IStatus.ERROR, RttPluginUI.PLUGIN_ID,
				throwable.getMessage(), throwable));
	}

}
