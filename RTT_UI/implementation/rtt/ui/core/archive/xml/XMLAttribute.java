package rtt.ui.core.archive.xml;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.AttributeList;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Token;
import rtt.ui.core.archive.IAttribute;

public class XMLAttribute implements IAttribute {
	
	private String name;
	private String value;
	private boolean isInformational;

	public XMLAttribute(Attribute attribute) {
		this.name = attribute.getName();
		this.value = attribute.getValue();
		this.isInformational = attribute.isInformational();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public boolean isInformational() {
		return isInformational;
	}

	public static List<IAttribute> getList(Node node) {
		return getList(node.getAttributes());		
	}
	
	public static List<IAttribute> getList(Token token) {
		return getList(token.getAttributes());
	}
	
	private static List<IAttribute> getList(AttributeList attributes) {
		List<IAttribute> list = new LinkedList<IAttribute>();
		if (attributes != null) {
			for (Attribute nodeAttribute : attributes.getAttribute()) {
				list.add(new XMLAttribute(nodeAttribute));
			}
		}	
		
		return list;
	}
}
