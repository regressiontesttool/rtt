package rtt.ui.editors.input.details;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.Node;
import rtt.ui.content.output.NodeContent;

public class NodeDetail implements IDetail {

	public static final String C_COUNT = "rtt.node.children.count";
	public static final String A_COUNT = "rtt.node.attribute.count";
	public static final String FULL_NAME = "rtt.node.fullName";

	private List<StringDetail> details;
	private List<AttributeDetail> attributes;
	private Node node;

	public NodeDetail(NodeContent nodeContent) {
		attributes = new ArrayList<AttributeDetail>();
		details = new ArrayList<StringDetail>();

		setNode(nodeContent.getNode());
	}

	public void setNode(Node node) {
		if (node != null && !node.equals(this.node)) {
			details.clear();
			attributes.clear();

			int childCount = 0;
			if (node.getChildren() != null) {
				childCount = node.getChildren().getNode().size();
			}

			int attributeCount = 0;
			if (node.getAttributes() != null) {
				for (Attribute attribute : node.getAttributes().getAttribute()) {
					attributes.add(new AttributeDetail(attribute));
					attributeCount++;
				}
			}

			details.add(new StringDetail(FULL_NAME, "Class: "
					+ node.getFullName()));
			
			details.add(new StringDetail(C_COUNT, "Children count: "
					+ childCount));
			
			details.add(new StringDetail(A_COUNT, "Attribute count: "
					+ attributeCount));

			this.node = node;
		}
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
