/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.managing;

import headliner.treedistance.BasicTree;
import headliner.treedistance.ComparisonZhangShasha;
import headliner.treedistance.OpsZhangShasha;
import headliner.treedistance.Transformation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import rtt.archive.Configuration;
import rtt.archive.Input;
import rtt.archive.LexerOutput;
import rtt.archive.Node;
import rtt.archive.NodeAttribute;
import rtt.archive.ParserOutput;
import rtt.archive.Testcase;
import rtt.archive.Testsuit;
import rtt.archive.Token;
import rtt.archive.TokenAttribute;
import rtt.archive.Tree;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class Tester extends TestRunner {

	public Tester() {
	}

	public List<TestResult> test(Archive currentArchive,
			Configuration configuration, String testSuiteName, String baseDir,
			boolean matching, boolean testInformational, Logging log)
			throws Exception {
		List<TestResult> results = new LinkedList<TestResult>();

		LexerExecuter lexer = null;
		if (configuration.getLexerClass() != null)
			lexer = new LexerExecuter(configuration.getLexerClass().getValue(),
					configuration.getClasspath(), baseDir);

		ParserExecuter parser = null;
		if (configuration.getParserClass() != null)
			parser = new ParserExecuter(configuration.getParserClass()
					.getValue(), configuration.getClasspath(), baseDir);

		for (Testsuit t : currentArchive.getTestSuites()) {
			if (testSuiteName != null && !t.getName().equals(testSuiteName))
				continue;

	

			for (Testcase test : t.getTestcase()) {
				boolean testSuccess = true;
				
				if (Manager.verbose)
					System.out.println("TestCase: " + test.getName()
							+ " [Testsuite: " + t.getName() + "]");

				Input in = test.getInput();
				if (in == null)
					in = test.getInputRef().getInput();
				boolean somethingTested = false;
				boolean wasError = false;

				try {
					// if no lexerOutput is set, generate it!
					if (lexer != null
							&& (test.getLexerOutput() != null || test
									.getLexerOutputRef() != null)) {
						if (Manager.verbose)
							System.out.println("Testing Lexic Results");

						LexerOutput lo = this.generateLexerResult(lexer, in);
						LexerTestFailure failure = null;
						if (test.getLexerOutput() != null)
							failure = compare(lo, test.getLexerOutput(), test,
									t, testInformational);
						else
							failure = compare(lo, test.getLexerOutputRef()
									.getLexerOutput(), test, t,
									testInformational);

						if (failure != null) {
							results.add(failure);
							testSuccess = false;
						}

						somethingTested = true;
					}

					if (parser != null
							&& (test.getParserOutput() != null || test
									.getParserOutputRef() != null)) {
						if (Manager.verbose)
							System.out.println("Testing Syntactic Results");

						ParserOutput po = this.generateParserResult(parser, in);
						List<ParserTestFailure> failure = null;

						ParserOutput p = test.getParserOutput();
						if (p == null)
							p = test.getParserOutputRef().getParserOutput();

						if (!matching)
							failure = compareStrict(po, p, test, t,
									testInformational);
						else
							failure = compareMatching(po, p, test, t,
									testInformational);

						if (failure != null && failure.size() > 0) {
							results.addAll(failure);
							testSuccess = false;
						}
						somethingTested = true;
					}

				} catch (Exception e) {
					if (Manager.verbose)
						e.printStackTrace();
					log.addInformational(
							"Error during running of Test Results: "
									+ e.getMessage(), "");
				}
				if (!somethingTested) {
					System.out.println("No testdata for Testcase '"
							+ test.getName() + "' in testsuite '" + t.getName()
							+ "'.");

					results.add(new TestSkipped(test, t));
				}

				if (somethingTested && testSuccess) {
					results.add(new TestSuccess(test, t));
				}

			}

		}

		return results;
	}

	public LexerTestFailure compare(LexerOutput was, LexerOutput expected,
			Testcase test, Testsuit ts, boolean testInformational) {
		Iterator<Token> wasIterator = was.getToken().iterator();
		Iterator<Token> expIterator = expected.getToken().iterator();

		while (wasIterator.hasNext()) {
			Token wasToken = wasIterator.next();
			LexerTestFailure error = null;
			if (!expIterator.hasNext()) {
				return new LexerTestFailure(wasToken, null, test, ts);
			}

			Token expToken = expIterator.next();
			error = compare(wasToken, expToken, test, ts, testInformational);
			if (error != null)
				return error;

		}

		if (expIterator.hasNext()) {
			return new LexerTestFailure(null, expIterator.next(), test, ts);
		}

		return null;

	}

	private LexerTestFailure compare(Token was, Token expected, Testcase test,
			Testsuit ts, boolean testInformational) {
		if (was.isIsEof() != expected.isIsEof())
			return new LexerTestFailure(was, expected, test, ts);

		if (was.getTokenAttribute().size() != expected.getTokenAttribute()
				.size())
			return new LexerTestFailure(was, expected, test, ts);

		for (int i = 0; i < was.getTokenAttribute().size(); ++i) {
			TokenAttribute wasAttrib = was.getTokenAttribute().get(i);
			TokenAttribute expAttrib = expected.getTokenAttribute().get(i);

			if (wasAttrib.isInformational() && !testInformational)
				continue;

			if (!wasAttrib.getName().equals(expAttrib.getName()))
				return new LexerTestFailure(was, expected, test, ts);
			if (!wasAttrib.getValue().equals(expAttrib.getValue()))
				return new LexerTestFailure(was, expected, test, ts);

		}

		return null;
	}

	private List<ParserTestFailure> compareMatching(ParserOutput was,
			ParserOutput expected, Testcase test, Testsuit t,
			boolean testInformational) {
		LinkedList<ParserTestFailure> result = new LinkedList<ParserTestFailure>();

		List<Tree> wasTrees = new LinkedList<Tree>(was.getTree());
		List<Tree> expTrees = new LinkedList<Tree>(expected.getTree());
		Map<Tree, Tree> tests = new HashMap<Tree, Tree>();

		// testen, ob gleichviele bäume zurückgegeben wurden

		if (wasTrees.size() > expTrees.size())
			result.add(new ParserTestFailure(wasTrees.size() - expTrees.size()
					+ " more trees than expected.", test, t));

		if (wasTrees.size() < expTrees.size())
			result.add(new ParserTestFailure(expTrees.size() - wasTrees.size()
					+ " more trees expected.", test, t));

		System.out.println("Searching for matches");

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
			ParserTestFailure failure = compare(entry.getKey(), entry
					.getValue(), test, t, testInformational);
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

	public List<ParserTestFailure> compareStrict(ParserOutput was,
			ParserOutput expected, Testcase test, Testsuit ts,
			boolean testInformational) {
		int max = Math.min(was.getTree().size(), expected.getTree().size());
		List<ParserTestFailure> result = new LinkedList<ParserTestFailure>();

		// nur einen baum mit dem andren vergleichen
		for (int i = 0; i < max; ++i) {
			ParserTestFailure tmp = compare(was.getTree().get(i), expected
					.getTree().get(i), test, ts, testInformational);
			if (tmp != null)
				result.add(tmp);

		}

		if (was.getTree().size() != expected.getTree().size())
			result.add(new ParserTestFailure("Expected Trees: "
					+ expected.getTree().size() + ", but found trees: "
					+ was.getTree().size(), test, ts));

		return result;
	}

	public ParserTestFailure compare(Tree was, Tree expected, Testcase test,
			Testsuit ts, boolean testInformational) {
		Iterator<Node> wasIterator = was.getNode().iterator();
		Iterator<Node> expIterator = expected.getNode().iterator();

		while (wasIterator.hasNext()) {
			Node wasNode = wasIterator.next();
			NodePath curPath = new NodePath(wasNode);
			if (!expIterator.hasNext()) {
				return new ParserTestFailure(wasNode, null, test, ts, curPath,
						null);
			}

			Node expNode = expIterator.next();
			ParserTestFailure failure = compare(wasNode, expNode, test, ts,
					curPath, testInformational);
			if (failure != null)
				return failure;

		}

		if (expIterator.hasNext()) {
			return new ParserTestFailure(null, expIterator.next(), test, ts,
					null, null);
		}

		return null;
	}

	private ParserTestFailure compare(Node was, Node expected, Testcase test,
			Testsuit ts, NodePath curPath, boolean testInformational) {

		if (was.getNodeAttribute().size() != expected.getNodeAttribute().size())
			return new ParserTestFailure(was, expected, test, ts, curPath, null);

		for (int i = 0; i < was.getNodeAttribute().size(); ++i) {
			NodeAttribute wasAttrib = was.getNodeAttribute().get(i);
			NodeAttribute expAttrib = expected.getNodeAttribute().get(i);

			if (wasAttrib.isInformational() && !testInformational)
				continue;

			if (!wasAttrib.getName().equals(expAttrib.getName()))
				return new ParserTestFailure(was, expected, test, ts, curPath,
						i);
			if (!wasAttrib.getValue().equals(expAttrib.getValue()))
				return new ParserTestFailure(was, expected, test, ts, curPath,
						i);

		}
		if ((was.getChildren() == null) != (expected.getChildren() == null))
			return new ParserTestFailure(was, expected, test, ts, curPath, null);

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
				return new ParserTestFailure(wasChild, expChild, test, ts,
						curPath, null);

			ParserTestFailure failure = compare(wasChild, expChild, test, ts,
					curPath.concat(wasChild), testInformational);

			if (failure != null)
				return failure;

		}

		return null;
	}

	public abstract class TestResult {
		Testcase testCase;
		Testsuit testSuite;

		public TestResult(Testcase testCase, Testsuit testSuite) {
			this.testCase = testCase;
			this.testSuite = testSuite;
		}

		public Testcase getTestCase() {
			return testCase;
		}

		public Testsuit getTestSuite() {
			return testSuite;
		}
	}

	public class TestSuccess extends TestResult {
		public TestSuccess(Testcase testCase, Testsuit testSuite) {
			super(testCase, testSuite);
		}

	}

	public class TestSkipped extends TestResult {
		public TestSkipped(Testcase testCase, Testsuit testSuite) {
			super(testCase, testSuite);
		}

	}

	public abstract class TestFailure extends TestResult {

		public TestFailure(Testcase testCase, Testsuit testSuite) {
			super(testCase, testSuite);
		}

		public abstract String getShortMessage();

		public abstract String getMessage();

		public abstract String getPath();

	}

	public class LexerTestFailure extends TestFailure {
		Token was, expected;

		public LexerTestFailure(Token was, Token expected, Testcase testCase,
				Testsuit testSuite) {
			super(testCase, testSuite);
			this.was = was;
			this.expected = expected;
		}

		@Override
		public String getMessage() {
			return "Expected Token:\n" + Printer.PrintToken(expected)
					+ "\nbut Token was:\n" + Printer.PrintToken(was);
		}

		@Override
		public String getShortMessage() {
			return "Token " + Printer.PrintToken(was)
					+ " differs from expected token "
					+ Printer.PrintToken(expected);
		}

		@Override
		public String getPath() {
			return "";
		}
	}

	public class ParserTestFailure extends TestFailure {
		Node was, expected;
		Integer attrPos;
		NodePath path;
		String m = null;

		public ParserTestFailure(Node was, Node expected, Testcase testCase,
				Testsuit testSuite, NodePath path, Integer attribPos) {
			super(testCase, testSuite);
			this.was = was;
			this.expected = expected;
			this.path = path;
			this.attrPos = attribPos;
		}

		public ParserTestFailure(String message, Testcase testCase,
				Testsuit testSuite) {
			super(testCase, testSuite);
			m = message;
		}

		@Override
		public String getMessage() {
			if (m != null)
				return m;

			return "Expected Node:\n" + Printer.PrintNode(expected)
					+ "\nbut Node was:\n" + Printer.PrintNode(was) + "\n"
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

			return "Node " + Printer.PrintNode(was)
					+ " differs from expected node "
					+ Printer.PrintNode(expected);
		}
	}

	public class NodePath {
		List<Node> path;

		public NodePath(Node root) {
			path = new LinkedList<Node>();
			path.add(root);
		}

		public String getXPath(Integer attributePos) {

			String xp = "/archiveLog";
			Integer curPos = null;
			Node curNode = null;
			for (int i = 0; i < path.size(); i++) {
				curNode = path.get(i);

				xp = xp + "/node";
				if (curPos != null) {
					xp += "[" + (curPos + 1) + "]";
				}

				if (i != path.size() - 1)
					xp += "/children";

				curPos = null;

				if (i < path.size() - 1
						&& curNode.getChildren().getNode().size() > 1)
					curPos = curNode.getChildren().getNode().indexOf(
							path.get(i + 1));

			}

			if (attributePos != null) {
				xp += "/nodeAttribute";
				if (curNode.getNodeAttribute().size() > 1)
					xp += "[" + (attributePos + 1) + "]";
			}

			return xp;
		}

		NodePath(List<Node> path, Node newNode) {
			this.path = new LinkedList<Node>();
			this.path.addAll(path);
			this.path.add(newNode);
		}

		public NodePath concat(Node newNode) {
			return new NodePath(path, newNode);
		}

		@Override
		public String toString() {
			String offset = new String();
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < path.size(); i++) {
				builder.append(offset + Printer.PrintNode(path.get(i)) + "\n");
				offset += " ";

			}
			return builder.toString();

		}

	}

}
