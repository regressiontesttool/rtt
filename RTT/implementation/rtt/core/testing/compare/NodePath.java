package rtt.core.testing.compare;

import java.util.LinkedList;
import java.util.List;

import rtt.core.archive.output.Value;
import rtt.core.archive.output.Node;
import rtt.core.manager.Printer;

public class NodePath {

	List<Value> path;

	public NodePath(Value root) {
		path = new LinkedList<Value>();
		path.add(root);
	}

	public String getXPath(Integer attributePos) {

		String xp = "/archiveLog";
		Integer curPos = null;
		Value curNode = null;
		for (int i = 0; i < path.size(); i++) {
			curNode = path.get(i);

			xp = xp + "/node";
			if (curPos != null) {
				xp += "[" + (curPos + 1) + "]";
			}

			if (i != path.size() - 1)
				xp += "/children";

			curPos = null;

			// TODO revise
//			if (i < path.size() - 1
//					&& curNode.getNodes().size() > 1)
//				curPos = curNode.getNodes().indexOf(
//						path.get(i + 1));

		}

//		if (attributePos != null) {
//			xp += "/nodeAttribute";
//			if (curNode.getAttributes().size() > 1)
//				xp += "[" + (attributePos + 1) + "]";
//		}

		return xp;
	}

	NodePath(List<Value> path, Value newNode) {
		this.path = new LinkedList<Value>();
		this.path.addAll(path);
		this.path.add(newNode);
	}

	public NodePath concat(Value newNode) {
		return new NodePath(path, newNode);
	}

	@Override
	public String toString() {
		String offset = new String();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < path.size(); i++) {
			builder.append(offset + Printer.printValue(path.get(i)) + "\n");
			offset += " ";

		}
		return builder.toString();

	}
	
}
