package rtt.core.manager;

import rtt.core.archive.output.Generator;
import rtt.core.archive.output.Node;

public class Printer {

	static public String PrintNode(Node node) {
		StringBuilder builder = new StringBuilder();
		builder.append("Node: ");
		
		if (node == null) {
			builder.append("<null>");
			return builder.toString();
		}		
		
		Generator generator = node.getGeneratedBy();
		builder.append(generator.getName());
		builder.append(" - ");
		builder.append(generator.getType().name());
		
		if (node.isIsNull()) {
			builder.append(" returned null.");
		}
		
		if (node.isInformational()) {
			builder.append(" [INFORMATIONAL]");
		}
	
		return builder.toString();
	}

}
