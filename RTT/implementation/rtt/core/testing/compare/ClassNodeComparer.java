package rtt.core.testing.compare;

import java.util.List;

import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.ExtendedComparator;

public class ClassNodeComparer extends ExtendedComparator<ClassNode> {

	private static final String SIMPLENAME_UNEQUAL =
			"The simple name attributes are different.";
	private static final String FULLNAME_UNEQUAL = 
			"The full name attributes are different.";
	private static final String CHILDREN_UNEQUAL =
			"The children nodes are different.";

	public ClassNodeComparer(OutputCompare outputCompare) {
		super(outputCompare);
	}
	
	@Override
	protected CompareResult compare(ClassNode referenceNode, ClassNode actualNode) {
		if (!referenceNode.getSimpleName().equals(actualNode.getSimpleName())) {
			return CompareResult.create(SIMPLENAME_UNEQUAL);
		}
		
		if (!referenceNode.getFullName().equals(actualNode.getFullName())) {
			return CompareResult.create(FULLNAME_UNEQUAL);
		}
		
		return compareChildren(referenceNode.getNodes(), actualNode.getNodes());
	}

	private CompareResult compareChildren(List<Node> refChildNodes, List<Node> actualChildNodes) {
		if (refChildNodes.size() != actualChildNodes.size()) {
			return CompareResult.create(CHILDREN_UNEQUAL);
		}
		
		int childCount = refChildNodes.size();
		for (int index = 0; index < childCount; index++) {
			CompareResult result = outputCompare.compareNodes(refChildNodes.get(index), actualChildNodes.get(0));
			if (result != null && result.hasDifferences()) {
				return result;
			}			
		}
		
		return null;
	}
	
}
