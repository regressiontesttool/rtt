package rtt.ui.core.internal.treeItem;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import rtt.ui.core.ITreeItem;

public class TreeItemLabelProvider extends LabelProvider {
	
	private static final TreeItemLabelProvider instance = new TreeItemLabelProvider();
	
	private TreeItemLabelProvider() {}
	
	public static TreeItemLabelProvider getInstance() {
		return instance;
	}
	
	@Override
	public String getText(Object element) {
		
		if (element instanceof ITreeItem) {
			return ((ITreeItem) element).getName();
		}
		
		return super.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if (element instanceof ITreeItem) {
			return ((ITreeItem) element).getImage();
		}
		
		return super.getImage(element);
	}
}
