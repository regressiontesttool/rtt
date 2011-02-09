/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.managing;

import rtt.archive.Node;
import rtt.archive.NodeAttribute;
import rtt.archive.Token;
import rtt.archive.TokenAttribute;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class Printer {

	static public String PrintToken(Token t) {
		if (t == null)
			return "Token: <null>";

		String result = "Token: [";
		boolean first = true;
		for (TokenAttribute ta : t.getTokenAttribute()) {
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
		for (TokenAttribute ta : t.getTokenAttribute()) {
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
		if (n == null)
			return "Node: <null>";

		String result = "Node: [";
		boolean first = true;
		for (NodeAttribute na : n.getNodeAttribute()) {
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
		for (NodeAttribute na : n.getNodeAttribute()) {
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
