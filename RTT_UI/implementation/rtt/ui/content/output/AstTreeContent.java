package rtt.ui.content.output;

import rtt.core.archive.output.Node;
import rtt.core.archive.output.Tree;
import rtt.ui.content.AbstractContent;
import rtt.ui.content.ContentIcon;
import rtt.ui.content.IContent;

public class AstTreeContent extends AbstractContent implements IContent {

	private Tree tree;
	
	public AstTreeContent(IContent parent, Tree tree) {
		super(parent);
		this.tree = tree;

		if (tree.getNode() != null) {
			for (Node node : tree.getNode()) {
				childs.add(new NodeContent(this, node));
			}
		}
	}

	@Override
	public String getText() {
		return "<Tree> " + tree.getSimpleName();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.AST_TREE;
	}

}
