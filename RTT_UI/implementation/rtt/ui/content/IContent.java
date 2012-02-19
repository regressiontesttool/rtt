package rtt.ui.content;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.graphics.Image;

import rtt.ui.model.RttProject;

public interface IContent {
	
	String getText();
	Image getImage(ResourceManager manager);
	
	Object getParent();
	
	boolean hasChildren();
	Object[] getChildren();	
	
	<T> T getContent(Class<T> clazz);
	RttProject getProject();
	void load();
}
