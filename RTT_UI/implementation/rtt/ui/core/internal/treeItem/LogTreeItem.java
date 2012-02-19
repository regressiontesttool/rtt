package rtt.ui.core.internal.treeItem;

import rtt.ui.core.ITreeItem;

public class LogTreeItem extends AbstractTreeItem {

	public LogTreeItem(ITreeItem parent, String string) {
		super(parent);
		setName(string);
	}

	@Override
	public Object[] getChildren() {
		return EMPTY_ARRAY;
	}

}
