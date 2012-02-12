/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.core.manager;

import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Token;


/**
 * 
 * @author Peter Mucha
 * 
 */
public class Printer {

	static public String PrintToken(Token t) {
		if (t == null || t.getAttributes() == null)
			return "Token: <null>";

		String result = "Token: [";
		boolean first = true;
		for (Attribute ta : t.getAttributes().getAttribute()) {
			if (ta.isInformational() == true)
				continue;

			if (first)
				first = false;
			else
				result += "|";

			result += ta.getName() + ":" + ta.getValue();
		}

		result += "(";
		first = true;
		for (Attribute ta : t.getAttributes().getAttribute()) {
			if (ta.isInformational() == false)
				continue;

			if (first)
				first = false;
			else
				result += "|";

			result += ta.getName() + ":" + ta.getValue();
		}

		result += ")]";

		return result;
	}

	static public String PrintNode(Node n) {
		if (n == null || n.getAttributes() == null)
			return "Node: <null>";

		String result = "Node: [";
		boolean first = true;
		for (Attribute na : n.getAttributes().getAttribute()) {
			if (na.isInformational() == true)
				continue;

			if (first)
				first = false;
			else
				result += "|";

			result += na.getName() + ":" + na.getValue();
		}
		result += "(";
		first = true;
		for (Attribute na : n.getAttributes().getAttribute()) {
			if (na.isInformational() != true)
				continue;

			if (first)
				first = false;
			else
				result += "|";

			result += na.getName() + ":" + na.getValue();
		}

		int childCount = (n.getChildren() != null) ? n.getChildren().getNode()
				.size() : 0;

		result += ")](Children:" + childCount + ")";

		return result;
	}

}
