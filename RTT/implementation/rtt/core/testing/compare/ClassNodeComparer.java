package rtt.core.testing.compare;

import rtt.core.archive.output.ClassNode;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.Comparer;

public class ClassNodeComparer extends Comparer<ClassNode>{

	@Override
	protected CompareResult compare(ClassNode referenceNode, ClassNode actualNode) {
		CompareResult result = new CompareResult();
		
		return result;
	}
	
}
