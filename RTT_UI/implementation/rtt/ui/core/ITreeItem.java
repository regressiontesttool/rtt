package rtt.ui.core;

import org.eclipse.swt.graphics.Image;

/**
 * This interface provides the methods for tree elements whithin a
 * JFace tree viewer.
 * 
 * @author C.Oelsner <C.Oelsner@web.de>
 *
 */
public interface ITreeItem {
	
	/**
	 * Returns the name of the tree item.
	 * @return the name
	 */
	String getName();
	
	/**
	 * Returns the icon image for this tree item.
	 * @return an icon
	 */
	Image getImage();
	
	/**
	 * Returns all childs for this tree item.
	 * @return an array with all children objects
	 */
	Object[] getChildren();
	
	/**
	 * Returns the parent of this tree item. Needed for ContentProvider.
	 * @return the parent tree item
	 */
	ITreeItem getParent();
	
	/**
	 * Returns the project which holds the data for this
	 * tree item
	 * @return a rtt project
	 */
	IRttProject getProject();
	
	/**
	 * Returns a object for a give class
	 * @param clazz the class of the returning object.
	 * @return an object of the given class
	 */
	Object getObject(Class<?> clazz);
	
	/**
	 * Returns true, if the item is empty (has no children)
	 * @return true, if item is empty
	 */
	boolean isEmpty();
}
