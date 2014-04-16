package rtt.ui.launching;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaLaunchTab;
import org.eclipse.pde.launching.IPDELauncherConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import rtt.core.RTTApplication;
import rtt.ui.RttPluginUI;
import rtt.ui.content.main.ProjectContent;
import rtt.ui.launching.RTTLaunchConfiguration.Constant;
import rtt.ui.utils.RttPluginUtil;

public class RTTTab extends JavaLaunchTab implements
		ILaunchConfigurationTab {

	private Map<Constant, Text> textMap;
	
	public RTTTab() {
		textMap = new HashMap<RTTLaunchConfiguration.Constant, Text>();
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 2;
		composite.setLayout(topLayout);

		textMap.put(Constant.PROJECT, createText("Bundle: ", composite));
	}
	
	private Text createText(String label, Composite composite) {
		Label textLabel = new Label(composite, SWT.NONE);
		textLabel.setText(label);
		
		Text text = new Text(composite, SWT.BORDER);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				scheduleUpdateJob();
			}
		});
		
		return text;
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy config) {
		
		for (Constant constant : Constant.values()) {
			config.setAttribute(constant.getID(), constant.getDefaultValue());
		}
		
		IJavaProject javaProject = getCurrentProject();
						
		// get project name
		String projectName = javaProject.getElementName();
		if (projectName != null && !projectName.equals("")) {
			config.setAttribute(Constant.PROJECT.getID(), projectName);
		}
		
		IProject project = javaProject.getProject();
		
		// get archive file
		IPath archivePath = RttPluginUtil.getArchivePath(project);
		if (archivePath != null && !archivePath.isEmpty()) {
			config.setAttribute(Constant.ARCHIVE.getID(), archivePath.toPortableString());
		}
		
		// get project type
		try {
			IProjectDescription description = project.getDescription();
			for (String natureId : description.getNatureIds()) {
				if (natureId.equals("org.eclipse.pde.PluginNature")) {
					config.setAttribute(Constant.TYPE.getID(), RTTApplication.PLUGIN_TYPE);
					break;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}		
		
		config.setAttribute(IPDELauncherConstants.USE_PRODUCT, false);
		config.setAttribute(IPDELauncherConstants.APPLICATION, "rtt.core.testapplication");
	}

	private IJavaProject getCurrentProject() {
		ProjectContent projectContent = RttPluginUI.getProjectManager().getCurrentContent();
		if (projectContent != null && projectContent.getProject() != null) {
			return projectContent.getProject().getJavaProject();
		}
		
		IJavaElement context = getContext();
		if (context != null) {
			IJavaProject javaProject = context.getJavaProject();
			if (javaProject != null && javaProject.exists()) {
				return javaProject;
			}		
		}
		
		throw new IllegalArgumentException("Could not find current java project");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		super.initializeFrom(config);
		
		for (Entry<Constant, Text> entry : textMap.entrySet()) {
			initText(entry.getValue(), config, entry.getKey());
		}
	}
	
	private void initText(Text text, ILaunchConfiguration config, Constant constant) {
		try {
			text.setText(config.getAttribute(constant.getID(), ""));
		} catch (CoreException e) {
			text.setText("Exception: " + e.getMessage());
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy config) {
		for (Entry<Constant, Text> entry : textMap.entrySet()) {
			config.setAttribute(entry.getKey().getID(), entry.getValue().getText().trim());
		}
	}

	@Override
	public String getName() {
		return "RTT Tab";
	}
}
