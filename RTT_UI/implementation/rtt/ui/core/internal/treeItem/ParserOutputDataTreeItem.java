package rtt.ui.core.internal.treeItem;

import java.util.List;

import rtt.ui.core.archive.IAbstractSyntaxTree;
import rtt.ui.core.archive.IParserOutput;

public class ParserOutputDataTreeItem extends AbstractTreeItem  {
	
	private IParserOutput output;
	
	public ParserOutputDataTreeItem(IParserOutput output) {
		super(null);
		this.output = output;
	}

	@Override
	public Object[] getChildren() {
		List<IAbstractSyntaxTree> trees = output.getAstTrees();
		Object[] result = new Object[trees.size()];
		
		for (int i = 0; i < trees.size(); i++) {
			result[i] = new AstTreeItem(this, trees.get(i));
		}
		
		return result;
	}
}
