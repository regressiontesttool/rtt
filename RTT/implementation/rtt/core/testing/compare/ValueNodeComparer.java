package rtt.core.testing.compare;

import rtt.core.archive.output.ValueNode;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.ExtendedComparator;

public class ValueNodeComparer extends ExtendedComparator<ValueNode> {

	private static final String VALUE_UNEQUAL =
			"The node values are different.";
	
	public ValueNodeComparer(OutputCompare outputCompare) {
		super(outputCompare);
	}

	@Override
	protected CompareResult compare(ValueNode referenceNode,
			ValueNode actualNode) {
		
		if (!referenceNode.getValue().equals(actualNode.getValue())) {
			return CompareResult.create(VALUE_UNEQUAL);
		}
		
		return null;
	}

}
