package rtt.ui.core.internal.treeItem;

import java.util.List;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.INode;

public class NodeTreeItem extends AbstractTreeItem {
	
	private INode node;
	
	public NodeTreeItem(ITreeItem parent, INode node) {
		super(parent);
		setName(node.getClassName());
		this.node = node;
	}

	@Override
	public Object[] getChildren() {	
		List<INode> childs = node.getChildernNodes();
		if (childs != null) {
			Object[] result = new Object[childs.size()];
			for (int i = 0; i < childs.size(); i++) {
				result[i] = new NodeTreeItem(this, childs.get(i));
			}
			
			return result;
		}
		
		return EMPTY_ARRAY;
	}
	
	@Override
	public Object getObject(Class<?> clazz) {
		if (clazz == INode.class) {
			return node;
		}
		
		return super.getObject(clazz);
	}

}
