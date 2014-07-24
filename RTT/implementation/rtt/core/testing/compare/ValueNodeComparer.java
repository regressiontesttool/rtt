package rtt.core.testing.compare;

import rtt.core.archive.output.ValueNode;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.Comparer;

public class ValueNodeComparer extends Comparer<ValueNode> {

	private static final String VALUE_DIFFERENCE =
			"The node values are different.";

	@Override
	protected CompareResult compare(ValueNode referenceNode,
			ValueNode actualNode) {
		
		CompareResult result = new CompareResult();
		
		if (referenceNode.getValue().equals(actualNode.getValue())) {
			result.setDifference(VALUE_DIFFERENCE);
		}

		return result;
	}

}
