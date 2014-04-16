package rtt.ui.content;

import org.eclipse.swt.graphics.Image;

import rtt.core.manager.Manager;
import rtt.ui.model.RttProject;

public interface IContent {
	
	/**
	 * Returns a text that describes the content.
	 * @return content text
	 */
	String getText();
	
	/**
	 * Returns a tool tip text.
	 * @return a tool tip
	 */
	String getToolTip();
	
	/**
	 * Returns a image for the content.
	 * @return content image
	 */
	Image getImage();
	
	/**
	 * Returns a parent content if existing, otherwise null.
	 * @return the parent
	 */
	IContent getParent();
	
	/**
	 * Returns true, if content has children
	 * @return true, if any children.
	 */
	boolean hasChildren();
	
	/**
	 * Returns an array of all children, if no 
	 * children exists an empty array will be returned
	 * 
	 * @return an array of children
	 */
	IContent[] getChildren();	
	
	/**
	 * Returns the content for the given class.
	 * If no content will be found, null will be returned.
	 * 
	 * @param clazz a class object, which should be searched.
	 * @return a instance of the class
	 */
	<T> T getContent(Class<T> clazz);
	
	/**
	 * Returns the rtt project to the given content.
	 * @return a rtt project
	 */
	RttProject getProject();
	
	void reload(ReloadInfo info, Manager manager);
	
	void notifyChanges();

	
}
