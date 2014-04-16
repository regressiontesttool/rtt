package rtt.ui.launching;

import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.pde.ui.launcher.AbstractPDELaunchConfigurationTabGroup;
import org.eclipse.pde.ui.launcher.ConfigurationTab;
import org.eclipse.pde.ui.launcher.MainTab;
import org.eclipse.pde.ui.launcher.PluginsTab;
import org.eclipse.pde.ui.launcher.TracingTab;


public class RTTTabGroup extends AbstractPDELaunchConfigurationTabGroup {

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		
		ILaunchConfigurationTab[] tabs = null;
		RTTTab rttTab = new RTTTab();
		tabs = new ILaunchConfigurationTab[] {new MainTab(), rttTab, new JavaArgumentsTab(), new PluginsTab(), new ConfigurationTab(true), new TracingTab(), new EnvironmentTab(), new CommonTab()};
		setTabs(tabs);
		
		dialog.setActiveTab(rttTab);
	}

}
