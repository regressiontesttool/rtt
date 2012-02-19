package rtt.ui.core.internal.treeItem;

import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchPage;

import rtt.ui.core.IRttProject;
import rtt.ui.core.ITreeItem;
import rtt.ui.editors.IEditorCommand;
import rtt.ui.editors.IOpenable;

/**
 * This class provides a basic functionality for the ITreeItem interface.
 * The abstract tree item loads automatically the given icon for the tree item.
 * Tree items without a real parent must override the {@link #getProject()} method.
 * 
 * @author C.Oelsner <C.Oelsner@web.de>
 *
 */
public abstract class AbstractTreeItem implements ITreeItem, IOpenable {
	
	private String name;
	private Image iconImage;
	protected ITreeItem parent;
	private TreeItemIcon icon;
	protected IEditorCommand editorCommand;
	
	protected static final Object[] EMPTY_ARRAY = new Object[0];
	
	/**
	 * Creates a new abstract tree item. The new item will display automatically name and 
	 * specified icon. For icons see {@link TreeItemIcon}
	 * 
	 * @param parent the parental tree item
	 */
	public AbstractTreeItem(ITreeItem parent) {
		this.parent = parent;
		this.icon = TreeItemIcon.RTT_PROJECT;
		this.name = "NAME NOT SET";
	}
	
	public AbstractTreeItem(ITreeItem parent, IEditorCommand command) {
		this.parent = parent;
		this.editorCommand = command;
		this.icon = TreeItemIcon.RTT_PROJECT;
		this.name = "NAME NOT SET";
	}
	
	public void setIcon(TreeItemIcon icon) {
		this.icon = icon;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCommand(IEditorCommand command) {
		this.editorCommand = command;
	}
	
	@Override
	protected void finalize() throws Throwable {
		iconImage.dispose();
		super.finalize();
	}
	
	public String getName() {
		return name;
	}
	
	public Image getImage() {
		if (iconImage != null) {
			iconImage.dispose();
		}
		
		iconImage = icon.createImage(isEmpty());
		
		return iconImage;
	}
	
	/**
	 * This implementation of {@link ITreeItem#isEmpty()} always return false.
	 * This method should be overwritten, if the state of item is different.
	 */
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public ITreeItem getParent() {
		return parent;
	}
	
	@Override
	public Object getObject(Class<?> clazz) {
		if (clazz == IRttProject.class) {
			return getProject();
		}
		
		return null;
	}

	/**
	 * This implementation of {@link ITreeItem#getProject()} try to get the project 
	 * from the parent element. If the parent item 
	 * is null a {@link NullPointerException} will be thrown.
	 * 
	 * @see ITreeItem#getProject()
	 * @throws NullPointerException
	 */
	@Override
	public IRttProject getProject() {
		if (parent != null) {
			return parent.getProject();
		}
		
		throw new NullPointerException("Parent tree item of \"" + this.getClass().getSimpleName() + "\" is null.");		
	}
	
	@Override
	public void openEditor(IWorkbenchPage page) {
		if (editorCommand != null) {
			editorCommand.open(page);
		}
	}
	
	public abstract Object[] getChildren();

}
