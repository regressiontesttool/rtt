package rtt.ui.core.internal.treeItem;

import rtt.ui.core.ITreeItem;
import rtt.ui.core.archive.IToken;

public class TokenTreeItem extends AbstractTreeItem {
	
	private IToken token;

	public TokenTreeItem(ITreeItem parent, IToken token) {
		super(parent);
		setName(token.getClassName());
		this.token = token;
	}
	
	@Override
	public Object getObject(Class<?> clazz) {
		if (clazz == IToken.class) {
			return token;
		}

		return super.getObject(clazz);
	}

	@Override
	public Object[] getChildren() {
		return EMPTY_ARRAY;
	}
}
