package rtt.ui.content.output;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.Node;
import rtt.ui.content.IContent;
import rtt.ui.content.main.AbstractContent;
import rtt.ui.content.main.ContentIcon;
import rtt.ui.content.main.SimpleTypedContent;
import rtt.ui.content.main.SimpleTypedContent.ContentType;

public class NodeContent extends AbstractContent {

	private Node node;
	
	public NodeContent(IContent parent, Node node) {
		super(parent);
		this.node = node;
		
		List<IContent> attributes = new ArrayList<IContent>();
		if (node.getAttributes() != null) {
			for (Attribute attribute : node.getAttributes().getAttribute()) {
				attributes.add(new AttributeContent(this, attribute));
			}
		}		
		
		List<IContent> children = new ArrayList<IContent>();
		if (node.getChildren() != null) {
			for (Node childNode	: node.getChildren().getNode()) {
				children.add(new NodeContent(this, childNode));
			}
		}		
		
		childs.add(new SimpleTypedContent(this, ContentType.ATTRIBUTES, attributes));
		childs.add(new SimpleTypedContent(this, ContentType.CHILDREN, children));		
	}
	
	public Node getNode() {
		return node;
	}

	@Override
	public String getText() {
		return "<Node> " + node.getSimpleName();
	}

	@Override
	protected ContentIcon getIcon() {
		return ContentIcon.NODE;
	}

}
