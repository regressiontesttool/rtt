package rtt.ui.core.internal.treeItem;

import java.util.List;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.IAbstractSyntaxTree;
import rtt.ui.core.archive.INode;

public class AstTreeItem extends AbstractTreeItem {
	
	private IAbstractSyntaxTree ast;
	
	public AstTreeItem(ITreeItem parent, IAbstractSyntaxTree ast) {
		super(parent);
		setName(ast.getFullName());
		this.ast = ast;
	}

	@Override
	public Object[] getChildren() {
		List<INode> nodes = ast.getNodes();
		if (nodes != null) {
			Object[] result = new Object[nodes.size()];
			
			int i = 0;
			for (INode node : nodes) {
				result[i] = new NodeTreeItem(this, node);
				i++;
			}
			
			return result;
		}
		
		return EMPTY_ARRAY;
	}

}
