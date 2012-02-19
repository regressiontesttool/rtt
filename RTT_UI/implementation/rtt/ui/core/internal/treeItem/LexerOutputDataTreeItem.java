package rtt.ui.core.internal.treeItem;

import java.util.List;

import rtt.ui.core.archive.ILexerOutput;
import rtt.ui.core.archive.IToken;

public class LexerOutputDataTreeItem extends AbstractTreeItem {

	private ILexerOutput output;
	
	public LexerOutputDataTreeItem(ILexerOutput output) {
		super(null);	
		this.output = output;
	}

	@Override
	public Object[] getChildren() {
		List<IToken> tokens = output.getToken();
		Object[] result = new Object[tokens.size()];
		
		for (int i = 0; i < tokens.size(); i++) {
			result[i] = new TokenTreeItem(this, tokens.get(i));
		}
		
		return result;
	}
	
}
