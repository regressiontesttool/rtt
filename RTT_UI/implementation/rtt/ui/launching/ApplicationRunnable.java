package rtt.ui.launching;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.ILaunchesListener2;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.operation.IRunnableWithProgress;

import rtt.core.RTTApplication;
import rtt.core.archive.configuration.Configuration;
import rtt.ui.model.RttProject;
import rtt.ui.utils.Messages;
import rtt.ui.utils.RttPluginUtil;

public class ApplicationRunnable implements IRunnableWithProgress {
	
	static class LaunchListener implements ILaunchesListener2 {

		private boolean finished = false;
		private List<ILaunch> launches = null;
		
		public LaunchListener() {
			this.launches = new ArrayList<ILaunch>();
		}

		@Override
		public void launchesRemoved(ILaunch[] launches) {
//			System.out.println("Launches removed: " + Arrays.toString(launches));
			
			for (ILaunch launch : launches) {
				if (this.launches.contains(launch)) {
					this.launches.remove(launch);
				}
			}		
		}

		@Override
		public void launchesAdded(ILaunch[] launches) {
//			System.out.println("Launches added: " + Arrays.toString(launches));
			
			for (ILaunch launch : launches) {
				if (!this.launches.contains(launch)) {
					this.launches.add(launch);
				}
			}
		}

		@Override
		public void launchesChanged(ILaunch[] launches) { }

		@Override
		public void launchesTerminated(ILaunch[] launches) {
//			System.out.println("Launches terminated: " + Arrays.toString(launches));
			
			for (ILaunch terminatedLaunch : launches) {
				if (this.launches.contains(terminatedLaunch)) {
//					System.out.println("Found launch.");
					finished = true;
				}
			}
		}

		public boolean hasFinished() {
			return finished;
		}		
	}

	private static final String NO_CONFIG_MESSAGE = "rtt.ui.application.error.no_config.message";
	
	private RttProject project;
	private String suiteName;
	private String action;
	private String taskText;
	private String archiveLocation;
	private Configuration configuration;

	public ApplicationRunnable(RttProject project, String suiteName, String action) {
		this(action);
		setProject(project);
		setSuiteName(suiteName);
	}
	
	public ApplicationRunnable(String action) {
		setAction(action);
	}
	
	public void setProject(RttProject project) {
		this.project = project;
		if (project == null) {
			throw new IllegalArgumentException("Project was null.");
		}
		
		this.configuration = project.getActiveConfiguration();
		if (configuration == null) {
			throw new IllegalArgumentException("Current active configuration was null.");
		}
		
		// get archive file
		IPath archivePath = RttPluginUtil.getArchivePath(project.getIProject());
		if (archivePath == null || archivePath.isEmpty()) {
			throw new IllegalArgumentException("Could not find archive path.");
		} 

		this.archiveLocation = project.getArchiveFile().getLocation().toPortableString();
	}
	
	public void setSuiteName(String suiteName) {
		if (suiteName == null || suiteName.equals("")) {
			throw new IllegalArgumentException("Test suite was null.");
		}
		
		this.suiteName = suiteName;
	}
	
	public void setAction(String action) {
		if (action.equals(RTTApplication.GENERATE)) {
			taskText = "Generate reference results ...";
		} else if (action.equals(RTTApplication.RUN)) {
			taskText = "Run tests ...";
		} else {
			throw new IllegalArgumentException("The action must be GENERATE or RUN.");
		}
		
		this.action = action;
	}
	
	public String getTaskText() {
		return taskText;
	}

	@Override
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {

		monitor.beginTask(taskText, IProgressMonitor.UNKNOWN);
		
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		if (manager == null) {
			throw new RuntimeException("Could not find launch manager.");
		}
		
		LaunchListener listener = new LaunchListener();
		manager.addLaunchListener(listener);
		
		try {
			ILaunchConfiguration launchConfig = getLaunchConfiguration(manager, project);
			
			ILaunchConfigurationWorkingCopy copy = launchConfig.getWorkingCopy();
			copy.setAttribute(RTTLaunchConfiguration.Constant.PROJECT.getID(), project.getName());
			copy.setAttribute(RTTLaunchConfiguration.Constant.ACTION.getID(), action);
			copy.setAttribute(RTTLaunchConfiguration.Constant.SUITE.getID(), suiteName);
			copy.setAttribute(RTTLaunchConfiguration.Constant.ARCHIVE.getID(), archiveLocation);
			copy.setAttribute(RTTLaunchConfiguration.Constant.CONFIG.getID(), configuration.getName());
			
			monitor.subTask("Creating data ...");
			DebugUITools.launch(copy, ILaunchManager.RUN_MODE);
			while (!listener.hasFinished()) {
				if (monitor.isCanceled()) {
					throw new ExecutionException("User has canceled the operation.");
				}
				Thread.sleep(500);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			manager.removeLaunchListener(listener);
		}
	}
	
	private ILaunchConfiguration getLaunchConfiguration(ILaunchManager manager, RttProject project) throws CoreException {

		ILaunchConfigurationType configType = manager.getLaunchConfigurationType(RTTLaunchConfiguration.ID);
		if (configType == null) {
			throw new RuntimeException("Could not get launch configuration type.");
		}
		
		ILaunchConfiguration[] configs = manager.getLaunchConfigurations(configType);
		for (ILaunchConfiguration config : configs) {
			String projectName = config.getAttribute(RTTLaunchConfiguration.Constant.PROJECT.getID(), "");
			if (projectName.equals(project.getName())) {
				return config;				
			}
		}
		
		throw new RuntimeException(Messages.getString(NO_CONFIG_MESSAGE));		
	}
}
