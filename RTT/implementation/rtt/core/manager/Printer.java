package rtt.core.manager;

import rtt.core.archive.output.Value;

public class Printer {

	static public String printValue(Value value) {
		StringBuilder builder = new StringBuilder();
		builder.append("Node: ");
		
		if (value == null) {
			builder.append("<null>");
			return builder.toString();
		}		
		
		builder.append(value.getName());
		builder.append(" - ");
		builder.append(value.getType());
		
		if (value.isIsNull()) {
			builder.append(" returned null.");
		}
		
		if (value.isInformational()) {
			builder.append(" [INFORMATIONAL]");
		}
	
		return builder.toString();
	}

}
