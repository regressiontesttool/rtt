package rtt.ui.editors.input;

import java.util.ArrayList;
import java.util.List;

import rtt.ui.core.archive.IAttribute;
import rtt.ui.core.archive.INode;
import rtt.ui.core.internal.treeItem.NodeTreeItem;

public class NodeDetail implements IDetail {
	
	public static final String C_COUNT = "rtt.node.children.count";
	public static final String A_COUNT = "rtt.node.attribute.count";
	public static final String CLASSNAME = "rtt.node.classname";
	
	private List<StringDetail> details;
	private List<AttributeDetail> attributes;
	private INode node;
	
	public NodeDetail(NodeTreeItem o) {
		attributes = new ArrayList<AttributeDetail>();
		details = new ArrayList<StringDetail>();
		
		setNode((INode) o.getObject(INode.class));
	}
	
	public void setNode(INode node) {
		if (node != null && !node.equals(this.node)) {			
			
			details.clear();
			attributes.clear();
			
			if (node != null) {

				details.add(new StringDetail(CLASSNAME, "Class: " + node.getClassName()));
				details.add(new StringDetail(C_COUNT, "Children count: " + node.getChildernNodes().size()));
				details.add(new StringDetail(A_COUNT, "Attribute count: " + node.getAttributes().size()));
				
				for (IAttribute attribute : node.getAttributes()) {
					attributes.add(new AttributeDetail(attribute));
				}
			}
		}
		
		this.node = node;		
	}	

	@Override
	public List<StringDetail> getStringDetails() {		
		return details;
	}

	@Override
	public List<AttributeDetail> getAttributes() {		
		return attributes;
	}
}
