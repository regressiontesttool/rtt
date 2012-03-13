package rtt.core.testing.compare;

import headliner.treedistance.BasicTree;
import headliner.treedistance.ComparisonZhangShasha;
import headliner.treedistance.OpsZhangShasha;
import headliner.treedistance.Transformation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.ParserOutput;
import rtt.core.archive.output.Tree;
import rtt.core.testing.compare.results.ParserTestFailure;
import rtt.core.utils.DebugLog;
import rtt.core.utils.DebugLog.LogType;



public class ParserOutputCompare {
	
	public static List<ParserTestFailure> compareParserOuput(ParserOutput was, ParserOutput expected,
			boolean testInformational, boolean matching) {
		
		ParserOutputCompare c = new ParserOutputCompare();
		if (matching == true) {
			
			return c.compareMatching(was, expected, testInformational);
		} else {
			return c.compareStrict(was, expected, testInformational);
		}		
	}
	
	private List<ParserTestFailure> compareStrict(ParserOutput was,
			ParserOutput expected, boolean testInformational) {
		int max = Math.min(was.getTree().size(), expected.getTree().size());
		List<ParserTestFailure> result = new LinkedList<ParserTestFailure>();

		// nur einen baum mit dem andren vergleichen
		for (int i = 0; i < max; ++i) {
			ParserTestFailure tmp = compareTree(was.getTree().get(i), expected
					.getTree().get(i), testInformational);
			if (tmp != null)
				result.add(tmp);

		}

		if (was.getTree().size() != expected.getTree().size())
			result.add(new ParserTestFailure("Expected Trees: "
					+ expected.getTree().size() + ", but found trees: "
					+ was.getTree().size()));

		return result;
	}

	private List<ParserTestFailure> compareMatching(ParserOutput was,
			ParserOutput expected, boolean testInformational) {
		LinkedList<ParserTestFailure> result = new LinkedList<ParserTestFailure>();

		List<Tree> wasTrees = new LinkedList<Tree>(was.getTree());
		List<Tree> expTrees = new LinkedList<Tree>(expected.getTree());
		Map<Tree, Tree> tests = new HashMap<Tree, Tree>();

		// testen, ob gleichviele b�ume zur�ckgegeben wurden

		if (wasTrees.size() > expTrees.size())
			result.add(new ParserTestFailure(wasTrees.size() - expTrees.size()
					+ " more trees than expected."));

		if (wasTrees.size() < expTrees.size())
			result.add(new ParserTestFailure(expTrees.size() - wasTrees.size()
					+ " more trees expected."));

		DebugLog.log(LogType.ALL, "Searching for matches");

		while (wasTrees.size() > 0) {
			Tree w = wasTrees.get(0);
			double minDistance = Double.MAX_VALUE;

			for (Tree e : expTrees) {
				double curDistance = calculateEditDistance(w, e);
				if (curDistance < minDistance) {
					minDistance = curDistance;
					tests.put(w, e);
				}
				if (curDistance == 0)
					break;
			}

			wasTrees.remove(w);
			expTrees.remove(tests.get(w));

		}

		for (Map.Entry<Tree, Tree> entry : tests.entrySet()) {
			ParserTestFailure failure = compareTree(entry.getKey(), entry
					.getValue(), testInformational);
			if (failure != null)
				result.add(failure);
		}

		return result;
	}

	private double calculateEditDistance(Tree was, Tree exp) {
		ComparisonZhangShasha<Node> comparator = new ComparisonZhangShasha<Node>();
		OpsZhangShasha costs = new OpsZhangShasha();
		BasicTree wasTree = new BasicTree(was, was.getNode().get(0), 0);
		BasicTree expTree = new BasicTree(exp, exp.getNode().get(0), 0);
		Transformation transform = comparator.findDistance(wasTree, expTree,
				costs);
		return transform.getCost();
	}

	private ParserTestFailure compareTree(Tree was, Tree expected,
			boolean testInformational) {
		Iterator<Node> wasIterator = was.getNode().iterator();
		Iterator<Node> expIterator = expected.getNode().iterator();

		while (wasIterator.hasNext()) {
			Node wasNode = wasIterator.next();
			NodePath curPath = new NodePath(wasNode);
			if (!expIterator.hasNext()) {
				return new ParserTestFailure(wasNode, null, curPath,
						null);
			}

			Node expNode = expIterator.next();
			ParserTestFailure failure = compareNode(wasNode, expNode, 
					curPath, testInformational);
			if (failure != null)
				return failure;

		}

		if (expIterator.hasNext()) {
			return new ParserTestFailure(null, expIterator.next(), 
					null, null);
		}

		return null;
	}

	private ParserTestFailure compareNode(Node was, Node expected, 
			NodePath curPath, boolean testInformational) {

		if (was == null || expected == null) {
			return new ParserTestFailure("Generated node, expected node or both nodes were null.");
		}
		
		if (expected.getAttributes() == null && was.getAttributes() == null) {
			return null;
		}
		
		List<Attribute> expectedAttributes = expected.getAttributes().getAttribute();
		List<Attribute> wasAttributes = was.getAttributes().getAttribute();
		
		if (wasAttributes.size() != expectedAttributes.size())
			return new ParserTestFailure(was, expected, curPath, null);

		for (int i = 0; i < wasAttributes.size(); ++i) {
			Attribute wasAttrib = wasAttributes.get(i);
			Attribute expAttrib = expectedAttributes.get(i);

			if (wasAttrib.isInformational() && !testInformational)
				continue;

			if (!wasAttrib.getName().equals(expAttrib.getName()))
				return new ParserTestFailure(was, expected, curPath,
						i);
			if (!wasAttrib.getValue().equals(expAttrib.getValue()))
				return new ParserTestFailure(was, expected, curPath,
						i);

		}
		if ((was.getChildren() == null) != (expected.getChildren() == null))
			return new ParserTestFailure(was, expected, curPath, null);

		int maxChildren = (was.getChildren() == null) ? 0 : Math.max(was
				.getChildren().getNode().size(), expected.getChildren()
				.getNode().size());

		for (int i = 0; i < maxChildren; ++i) {
			Node wasChild = null;
			Node expChild = null;
			try {
				wasChild = was.getChildren().getNode().get(i);
			} catch (IndexOutOfBoundsException e) {
			}

			try {
				expChild = expected.getChildren().getNode().get(i);
			} catch (IndexOutOfBoundsException e) {
			}

			if (wasChild == null || expChild == null)
				return new ParserTestFailure(wasChild, expChild, curPath, null);

			ParserTestFailure failure = compareNode(wasChild, expChild, 
					curPath.concat(wasChild), testInformational);

			if (failure != null)
				return failure;

		}

		return null;
	}

}
