package rtt.ui.core.internal.treeItem;

import rtt.ui.core.ITreeItem;
import rtt.ui.editors.IEditorCommand;

public class SimpleTreeItem extends AbstractTreeItem {	

	public SimpleTreeItem(ITreeItem parent, String name, TreeItemIcon icon) {
		super(parent);
		setName(name);
		setIcon(icon);
	}
	
	public SimpleTreeItem(ITreeItem parent, String name, TreeItemIcon icon, IEditorCommand command) {
		super(parent, command);
		setName(name);
		setIcon(icon);
	}
	
	public SimpleTreeItem(ITreeItem parent, String name) {
		super(parent);
		setName(name);
	}

	@Override
	public Object[] getChildren() {
		return EMPTY_ARRAY;
	}

}
