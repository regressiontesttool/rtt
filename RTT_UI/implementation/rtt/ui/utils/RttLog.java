package rtt.ui.utils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import rtt.ui.RttPluginUI;

public class RttLog {

	public static void log(IStatus status) {
		RttPluginUI.getPlugin().getLog().log(status);
	}

	public static IStatus log(Throwable throwable) {
		IStatus result = new Status(IStatus.ERROR, RttPluginUI.PLUGIN_ID,
				throwable.getMessage(), throwable); 
		
		log(result);
		
		return result;
	}

}
