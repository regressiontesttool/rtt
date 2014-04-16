package rtt.ui.launching;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.pde.launching.EclipseApplicationLaunchConfiguration;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import rtt.core.RTTApplication;
import rtt.core.RTTApplication.MapKeys;

public class RTTLaunchConfiguration extends
		EclipseApplicationLaunchConfiguration implements
		ILaunchConfigurationDelegate {
	
	public enum Constant {
		ARCHIVE("rtt.launching.archive", MapKeys.ARCHIVE, ""),
		SUITE("rtt.launching.suite", MapKeys.SUITE, ""),
		ACTION("rtt.lauching.action", MapKeys.ACTION, RTTApplication.GENERATE),
		PROJECT("rtt.launching.bundle", MapKeys.PROJECT, ""),
		TYPE("rtt.launching.type", MapKeys.TYPE, RTTApplication.JAVA_TYPE), 
		CONFIG("rtt.launching.config", MapKeys.CONFIG, "");
		
		private String id;
		private String argument;
		private String defaultValue;

		private Constant(String id, MapKeys key, String defaultValue) {
			this.id = id;
			this.argument = key.getArgumentCode();
			this.defaultValue = defaultValue;
		}
		
		public String getArgument() {
			return argument;
		}
		
		public String getID() {
			return id;
		}
		
		public String getDefaultValue() {
			return defaultValue;
		}
	}
	
	public static final String ID = "rtt.ui.launchConfigurationType";
	
	public RTTLaunchConfiguration() {
		super();
	}
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		boolean configurationComplete = true;
		for (Constant constant : Constant.values()) {
			String value = configuration.getAttribute(constant.getID(), "");
			if (value.isEmpty()) {
				configurationComplete = false;
				break;
			}
		}
		
		if (configurationComplete) {
			super.launch(configuration, mode, launch, monitor);
		} else {
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					Shell shell = Display.getDefault().getActiveShell();					
					MessageDialog.openError(
							shell, "Error", "Not sufficient arguments to run RTT.");
				}
			});
		}
	}

	@Override
	public String[] getProgramArguments(ILaunchConfiguration configuration)
			throws CoreException {
		String[] baseArguments = super.getProgramArguments(configuration);
		
		List<String> argumentList = new ArrayList<String>();
		for (String argument : baseArguments) {
			argumentList.add(argument);
		}
		
		for (Constant constant : Constant.values()) {			
			argumentList.add(constant.getArgument());
			argumentList.add(configuration.getAttribute(constant.getID(), ""));
		}
		
		return argumentList.toArray(new String[argumentList.size()]);
	}
	
}
