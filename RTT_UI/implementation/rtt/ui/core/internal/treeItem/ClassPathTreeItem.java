package rtt.ui.core.internal.treeItem;

import java.util.List;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.IPath;

public class ClassPathTreeItem extends AbstractTreeItem {

	private Object[] entries;
	
	public ClassPathTreeItem(ITreeItem parent, List<IPath> classPath) {
		super(parent);		
		setName("Classpath");
		setIcon(TreeItemIcon.CLASSPATH);
		
		if (classPath != null && classPath.size() > 0) {
			
			this.entries = new Object[classPath.size()];
			for (int i = 0; i < classPath.size(); i++) {
				entries[i] = new SimpleTreeItem(this, classPath.get(i).getValue());
			}
			
		} else {
			entries = new Object[] { new SimpleTreeItem(this, "No entries in classpath") };
		}
	}

	@Override
	public Object[] getChildren() {
		return entries;
	}
}
