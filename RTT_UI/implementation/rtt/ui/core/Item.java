package rtt.ui.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import rtt.ui.RttPluginUI;

public abstract class Item {
	
	private IProject project;
	private String name;
	private Image iconImage;
	
	public Item(IProject project, String name, String iconPath) {
		this.project = project;
		this.name = name;
		
		if (iconPath != null && !iconPath.equals("")) {
			ImageDescriptor descriptor = RttPluginUI.imageDescriptorFromPlugin(RttPluginUI.PLUGIN_ID, iconPath);
			if (descriptor != null) {
				iconImage = descriptor.createImage();
			}
		}				
	}
	
	public IProject getProject() {
		return project;
	}
	
	public String getName() {
		return name;
	}
	
	public abstract Object[] getChildren();

	public Image getImage() {
		return iconImage;
	}
}
