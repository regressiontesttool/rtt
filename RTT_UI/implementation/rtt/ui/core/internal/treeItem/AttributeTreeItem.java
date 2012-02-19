package rtt.ui.core.internal.treeItem;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.IAttribute;

public class AttributeTreeItem extends AbstractTreeItem {
	
//	private IAttribute attribute;
	
	public AttributeTreeItem(ITreeItem parent, IAttribute attribute) {
		super(parent);
		setName(attribute.getName() + "(" + attribute.getValue() + ")");
//		this.attribute = attribute;
	}

	@Override
	public Object[] getChildren() {
		return EMPTY_ARRAY;
	}

}
