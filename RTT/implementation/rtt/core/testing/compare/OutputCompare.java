package rtt.core.testing.compare;

import java.util.ArrayList;
import java.util.List;

import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.testing.compare.results.TestFailure;

public class OutputCompare {
	
	private static final String GEN_TYPE_UNEQUAL = "Generator type is unequal.";
	private static final String GEN_NAME_UNEQUAL = "Generator name was unequal.";
	private static final String ISINFORMATIONAL_UNEQUAL = "One node is informational, other not.";
	private static final String ISNULL_UNEQUAL = "One node was null, but other node not.";
	private static final String NODE_NULL = "One given node or both were null.";
	private static final String SIZE_UNEQUAL = "Size of actual and reference nodes are unequal.";

	public static List<TestFailure> compareOutput(
			Output referenceOutput, Output actualOutput, boolean testInformational) {
		
		if (referenceOutput == null || actualOutput == null) {
			throw new IllegalArgumentException("Reference or actual output was null.");
		}
		
		OutputCompare compare = new OutputCompare();
		
		List<TestFailure> failures = new ArrayList<>();
		
		failures.addAll(compare.compareNodes(
				referenceOutput.getNodes(), actualOutput.getNodes(), testInformational));
		
		return failures;
	}
	
	private List<TestFailure> compareNodes(List<Node> referenceNodes, 
			List<Node> actualNodes, boolean testInformational) {
		
		List<TestFailure> failures = new ArrayList<>();
		
		if (referenceNodes.size() != actualNodes.size()) {
			failures.add(new TestFailure(SIZE_UNEQUAL));
			return failures;
		}
		
		for(int index = 0; index < referenceNodes.size(); index++) {
			Node referenceNode = referenceNodes.get(index);
			Node actualNode = actualNodes.get(index);
			
			failures.addAll(compareNode(referenceNode, actualNode, testInformational));
		}
		
		return failures;
	}

	public List<TestFailure> compareNode(Node referenceNode, Node actualNode,
			boolean testInformational) {
		
		List<TestFailure> failures = new ArrayList<>();
		
		if (referenceNode == null || actualNode == null) {
			throw new IllegalStateException(NODE_NULL);
		}
		
		TestFailure failure = compareSimpleNode(referenceNode, actualNode, testInformational);
		if (failure != null) {
			failures.add(failure);
		}

		return failures;
	}

	public TestFailure compareSimpleNode(Node referenceNode, Node actualNode, boolean testInformational) {
		if (referenceNode.isInformational() != actualNode.isInformational()) {
			return new TestFailure(ISINFORMATIONAL_UNEQUAL);
		}
		
		if (!referenceNode.isInformational() || testInformational) {
			if (!referenceNode.getGeneratorType().equals(actualNode.getGeneratorType())) {
				return new TestFailure(GEN_TYPE_UNEQUAL);
			}
			
			if (!referenceNode.getGeneratorName().equals(actualNode.getGeneratorName())) {
				return new TestFailure(GEN_NAME_UNEQUAL);
			}			
			
			if (referenceNode.isIsNull() != actualNode.isIsNull()) {
				return new TestFailure(ISNULL_UNEQUAL);
			}
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
