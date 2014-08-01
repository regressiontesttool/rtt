package rtt.core.testing.compare.results;

import rtt.core.archive.output.Value;
import rtt.core.archive.output.Node;
import rtt.core.manager.Printer;
import rtt.core.testing.compare.NodePath;

public class TestFailure implements ITestFailure {
	Value was;
	Value expected;
	Integer attrPos;
	NodePath path;
	String m = null;

	public TestFailure(Value was, Value expected, 
			NodePath path, Integer attribPos) {
		super();
		this.was = was;
		this.expected = expected;
		this.path = path;
		this.attrPos = attribPos;
	}

	public TestFailure(String message) {
		super();
		m = message;
	}

	@Override
	public String getMessage() {
		if (m != null)
			return m;

		return "Expected Node:\n" + Printer.printValue(expected)
				+ "\nbut Node was:\n" + Printer.printValue(was) + "\n"
				+ "Path:\n" + path.toString();
	}

	@Override
	public String getPath() {
		if (path == null)
			return "";

		return path.getXPath(attrPos);
	}

	@Override
	public String getShortMessage() {
		if (m != null)
			return m;

		return "Node " + Printer.printValue(was)
				+ " differs from expected node "
				+ Printer.printValue(expected);
	}
}
