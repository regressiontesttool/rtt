package rtt.core.manager;

import rtt.core.archive.output.Node;

public class Printer {

	static public String PrintNode(Node node) {
		StringBuilder builder = new StringBuilder();
		builder.append("Node: ");
		
		if (node == null) {
			builder.append("<null>");
			return builder.toString();
		}		
		
		builder.append(node.getGeneratorName());
		builder.append(" - ");
		builder.append(node.getGeneratorType().name());
		
		if (node.isIsNull()) {
			builder.append(" returned null.");
		}
		
		if (node.isInformational()) {
			builder.append(" [INFORMATIONAL]");
		}
	
		return builder.toString();
	}

}
