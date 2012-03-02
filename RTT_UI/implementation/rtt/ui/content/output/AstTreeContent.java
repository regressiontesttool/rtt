package rtt.ui.content.output;

import rtt.core.archive.output.Node;
import rtt.core.archive.output.Tree;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;

public class AstTreeContent extends AbstractContent {

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
