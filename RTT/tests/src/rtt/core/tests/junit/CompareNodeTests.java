package rtt.core.tests.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Node;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.Comparer;

public class CompareNodeTests {

	private static final String NAME = "Node";
	private static final GeneratorType TYPE = GeneratorType.METHOD;
	
	private Comparer<Node> comparer;

	@Before
	public void setUp() throws Exception {
		comparer = new Comparer<Node>();
	}
	
	private Node createNode(String name, GeneratorType type) {
		Node node = new Node();
		node.setGeneratorName(name);
		node.setGeneratorType(type);
		
		return node;
	}
	
	private Node createSampleNode() {
		return createNode(NAME, TYPE);
	}
	
	private Node[] createSampleNodes(int count) {
		Node[] result = new Node[count];
		for (int i = 0; i < count; i++) {
			result[i] = createSampleNode();
		}
		
		return result;
	}
	
	private boolean findDifferences(Node referenceNode, Node actualNode) {
		CompareResult result = comparer.compareNodes(referenceNode, actualNode);
		return result.hasDifferences();		
	}
	
	private void checkThrowsException(Node refNode, Node actualNode) {
		
		try {
			comparer.compareNodes(refNode, actualNode);
			fail();
		} catch (Exception e) {
			// do nothing
		}
	}
	
	@Test
	public void testNullNodes() throws Exception {
		checkThrowsException(null, null);		
		checkThrowsException(createSampleNode(), null);
		checkThrowsException(null, createSampleNode());
	}	

	@Test
	public void testEqualNodes() throws Exception {
		assertFalse(findDifferences(createSampleNode(), createSampleNode()));
	}	

	@Test
	public void testTypeAttribute() throws Exception {
		Node[] nodes = createSampleNodes(2);
		nodes[0].setGeneratorType(GeneratorType.FIELD);
		
		testNodeCombinations(nodes);
	}
	
	@Test
	public void testNameAttribute() throws Exception {
		Node[] nodes = createSampleNodes(2);
		nodes[0].setGeneratorName("OtherNode");
		
		testNodeCombinations(nodes);
	}
	
	@Test
	public void testIsNullAttribute() throws Exception {
		Node[] nodes = createSampleNodes(2);
		nodes[0].setIsNull(true);
		
		testNodeCombinations(nodes);
	}
	
	private void testNodeCombinations(Node[] nodes) {
		assertTrue(findDifferences(nodes[0], nodes[1]));		
		assertTrue(findDifferences(nodes[1], nodes[0]));
	}
}
