package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.output.Node;
import rtt.core.archive.output.Tree;
import rtt.ui.core.archive.IAttribute;
import rtt.ui.core.archive.INode;

public class XMLNode implements INode {
	
	private List<IAttribute> attributes;
	private List<INode> children;
	private boolean isNull;
	private String className;

	public XMLNode(Node node) {
		attributes = XMLAttribute.getList(node);
		children = XMLNode.getList(node);
		isNull = node.isIsNull();
		className = node.getFullName();
	}
	
	@Override
	public String getClassName() {
		return className;
	}

	@Override
	public List<IAttribute> getAttributes() {
		return attributes;
	}

	@Override
	public List<INode> getChildernNodes() {
		return children;
	}

	@Override
	public boolean isNull() {
		return isNull;
	}

	public static List<INode> getList(Tree tree) {
		List<INode> list = new LinkedList<INode>();
		for (Node node : tree.getNode()) {
			list.add(new XMLNode(node));
		}
		
		return list;
	}
	
	private static List<INode> getList(Node node) {
		List<INode> list = new LinkedList<INode>();
		
		if (node.getChildren() != null) {
			for (Node child : node.getChildren().getNode()) {
				list.add(new XMLNode(child));
			}
		}
		
		return list;
	}

}
