package rtt.core.testing.compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.ValueNode;
import rtt.core.testing.compare.results.TestFailure;

public class OutputCompare {
	
	protected static abstract class ExtendedComparator<T extends Node> {
		
		protected OutputCompare outputCompare;
		
		protected ExtendedComparator(OutputCompare outputCompare) {
			this.outputCompare = outputCompare;
		}

		@SuppressWarnings("unchecked")
		public CompareResult compareNodes(Node referenceNode, Node actualNode) {				
			return compare((T) referenceNode, (T) actualNode);
		}
		
		protected abstract CompareResult compare(T referenceNode, T actualNode);
	}
	
	public static class CompareResult {
		private String difference;
		
		public void setDifference(String difference) {
			this.difference = difference;
		}
		
		public String getDifference() {
			return difference;
		}

		public boolean hasDifferences() {
			return difference != null && !difference.equals("");
		}
		
		public static CompareResult create(String difference) {
			CompareResult result = new CompareResult();
			result.setDifference(difference);
			
			return result;			
		}
	}
	
	
	private static final String NODE_NULL = "One or both given nodes were null.";
	
	private static final String INFORMATIONAL_UNEQUAL = "One node is informational, but the other not.";
	private static final String GEN_TYPE_UNEQUAL = "Generator types are not equal.";
	private static final String GEN_NAME_UNEQUAL = "Generator names are not equal.";
	private static final String ISNULL_UNEQUAL = "One node is 'isNull', but the other not.";
	private static final String CLASSES_UNEQUAL = "The node types are not equals";
	
	private static final String SIZE_UNEQUAL = "The sizes of reference and actual output are not equal";
	
	private Map<Class<? extends Node>, ExtendedComparator<?>> comparerMap;
	private boolean testInformational;
	
	public OutputCompare(boolean testInformational) {
		this.testInformational = testInformational;
		
		comparerMap = new HashMap<>();			
		comparerMap.put(ClassNode.class, new ClassNodeComparer(this));
		comparerMap.put(ValueNode.class, new ValueNodeComparer(this));
	}

	public static List<TestFailure> compareOutput(
			Output referenceOutput, Output actualOutput, boolean testInformational) {
		
		if (referenceOutput == null || actualOutput == null) {
			throw new IllegalArgumentException("Reference or actual output was null.");
		}
		
		OutputCompare compare = new OutputCompare(testInformational);
		
		List<TestFailure> failures = new ArrayList<>();
		
		failures.addAll(compare.compareNodeLists(
				referenceOutput.getNodes(), actualOutput.getNodes()));
		
		return failures;
	}
	
	private List<TestFailure> compareNodeLists(List<Node> referenceNodes, 
			List<Node> actualNodes) {
		
		List<TestFailure> failures = new ArrayList<>();
		
		if (referenceNodes.size() != actualNodes.size()) {
			failures.add(new TestFailure(SIZE_UNEQUAL));
			return failures;
		}
		
		for(int index = 0; index < referenceNodes.size(); index++) {
			Node referenceNode = referenceNodes.get(index);
			Node actualNode = actualNodes.get(index);
			
			CompareResult result = compareNodes(referenceNode, actualNode);
			if (result != null && result.hasDifferences()) {
				failures.add(new TestFailure(result.getDifference()));
			}
		}
		
		return failures;
	}
	
	public CompareResult compareNodes(Node referenceNode, Node actualNode) {
		
		if (referenceNode == null || actualNode == null) {
			throw new IllegalStateException(NODE_NULL);
		}
		
		if (referenceNode.isInformational() != actualNode.isInformational()) {
			return CompareResult.create(INFORMATIONAL_UNEQUAL);
		}
		
		if (!referenceNode.isInformational() || testInformational) {
			
			if (referenceNode.getClass() != actualNode.getClass()) {
				return CompareResult.create(CLASSES_UNEQUAL);
			}
			
			if (!referenceNode.getGeneratorType().equals(actualNode.getGeneratorType())) {
				return CompareResult.create(GEN_TYPE_UNEQUAL);
			}

			if (!referenceNode.getGeneratorName().equals(actualNode.getGeneratorName())) {
				return CompareResult.create(GEN_NAME_UNEQUAL);
			}			

			if (referenceNode.isIsNull() != actualNode.isIsNull()) {
				return CompareResult.create(ISNULL_UNEQUAL);
			}
			
			ExtendedComparator<?> comparer = getComparer(referenceNode.getClass());
			if (comparer != null) {
				return comparer.compareNodes(referenceNode, actualNode);
			}			
		}
		
		return null;
	}

	private ExtendedComparator<? extends Node> getComparer(Class<?> nodeClass) {
		if (comparerMap.containsKey(nodeClass)) {
			return comparerMap.get(nodeClass);
		}
		
		return null;
	}
	
	

//	private List<TestFailure> compareMatching(Output was,
//			Output expected, boolean testInformational) {
//		LinkedList<TestFailure> result = new LinkedList<TestFailure>();
//
//		List<Tree> wasTrees = new LinkedList<Tree>(was.getTree());
//		List<Tree> expTrees = new LinkedList<Tree>(expected.getTree());
//		Map<Tree, Tree> tests = new HashMap<Tree, Tree>();
//
//		// testen, ob gleichviele bäume zurückgegeben wurden
//
//		if (wasTrees.size() > expTrees.size())
//			result.add(new TestFailure(wasTrees.size() - expTrees.size()
//					+ " more trees than expected."));
//
//		if (wasTrees.size() < expTrees.size())
//			result.add(new TestFailure(expTrees.size() - wasTrees.size()
//					+ " more trees expected."));
//
//		RTTLogging.info("Searching for matches");
//
//		while (wasTrees.size() > 0) {
//			Tree w = wasTrees.get(0);
//			double minDistance = Double.MAX_VALUE;
//
//			for (Tree e : expTrees) {
//				double curDistance = calculateEditDistance(w, e);
//				if (curDistance < minDistance) {
//					minDistance = curDistance;
//					tests.put(w, e);
//				}
//				if (curDistance == 0)
//					break;
//			}
//
//			wasTrees.remove(w);
//			expTrees.remove(tests.get(w));
//
//		}
//
//		for (Map.Entry<Tree, Tree> entry : tests.entrySet()) {
//			TestFailure failure = compareTree(entry.getKey(), entry
//					.getValue(), testInformational);
//			if (failure != null)
//				result.add(failure);
//		}
//
//		return result;
//	}

//	private TestFailure compareTree(Tree was, Tree expected,
//			boolean testInformational) {
//		Iterator<Node> wasIterator = was.getNode().iterator();
//		Iterator<Node> expIterator = expected.getNode().iterator();
//
//		while (wasIterator.hasNext()) {
//			Node wasNode = wasIterator.next();
//			NodePath curPath = new NodePath(wasNode);
//			if (!expIterator.hasNext()) {
//				return new TestFailure(wasNode, null, curPath,
//						null);
//			}
//
//			Node expNode = expIterator.next();
//			TestFailure failure = compareNode(wasNode, expNode, 
//					curPath, testInformational);
//			if (failure != null)
//				return failure;
//
//		}
//
//		if (expIterator.hasNext()) {
//			return new TestFailure(null, expIterator.next(), 
//					null, null);
//		}
//
//		return null;
//	}

//	private TestFailure compareNode(Node was, Node expected, 
//			NodePath curPath, boolean testInformational) {
//
//		if (was == null || expected == null) {
//			return new TestFailure("Generated node, expected node or both nodes were null.");
//		}
//		
//		if (expected.getAttributes() == null && was.getAttributes() == null) {
//			return null;
//		}
//		
//		List<Attribute> expectedAttributes = expected.getAttributes();
//		List<Attribute> wasAttributes = was.getAttributes();
//		
//		if (wasAttributes.size() != expectedAttributes.size())
//			return new TestFailure(was, expected, curPath, null);
//
//		for (int i = 0; i < wasAttributes.size(); ++i) {
//			Attribute wasAttrib = wasAttributes.get(i);
//			Attribute expAttrib = expectedAttributes.get(i);
//
//			if (wasAttrib.isInformational() && !testInformational)
//				continue;
//
//			if (!wasAttrib.getName().equals(expAttrib.getName()))
//				return new TestFailure(was, expected, curPath,
//						i);
//			if (!wasAttrib.getValue().equals(expAttrib.getValue()))
//				return new TestFailure(was, expected, curPath,
//						i);
//
//		}
//		if ((was.getNodes() == null) != (expected.getNodes() == null))
//			return new TestFailure(was, expected, curPath, null);
//
//		int maxChildren = (was.getNodes() == null) ? 0 : Math.max(was
//				.getNodes().size(), expected.getNodes().size());
//
//		for (int i = 0; i < maxChildren; ++i) {
//			Node wasChild = null;
//			Node expChild = null;
//			try {
//				wasChild = was.getNodes().get(i);
//			} catch (IndexOutOfBoundsException e) {
//			}
//
//			try {
//				expChild = expected.getNodes().get(i);
//			} catch (IndexOutOfBoundsException e) {
//			}
//
//			if (wasChild == null || expChild == null)
//				return new TestFailure(wasChild, expChild, curPath, null);
//
//			TestFailure failure = compareNode(wasChild, expChild, 
//					curPath.concat(wasChild), testInformational);
//
//			if (failure != null)
//				return failure;
//
//		}
//
//		return null;
//	}

}
