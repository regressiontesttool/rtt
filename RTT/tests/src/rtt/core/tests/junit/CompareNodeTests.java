package rtt.core.tests.junit;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Node;
import rtt.core.testing.compare.OutputCompare;

public class CompareNodeTests {

	private static final String NAME = "Node";
	private static final GeneratorType TYPE = GeneratorType.METHOD;
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception {
		comparer = new OutputCompare();
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
	
	private void checkThrowsException(Node refNode, Node actualNode, 
			boolean informational) {
		
		try {
			comparer.compareSimpleNode(refNode, actualNode, informational);
			fail();
		} catch (Exception e) {
			// do nothing
		}
	}
	
	@Test
	public void testNullNodes() throws Exception {
		checkThrowsException(null, null, true);
		checkThrowsException(null, null, false);
		
		checkThrowsException(createSampleNode(), null, true);
		checkThrowsException(createSampleNode(), null, false);
		
		checkThrowsException(null, createSampleNode(), true);
		checkThrowsException(null, createSampleNode(), false);
	}	

	@Test
	public void testEqualNodes() throws Exception {
		assertNull(comparer.compareSimpleNode(createSampleNode(), createSampleNode(), true));
		assertNull(comparer.compareSimpleNode(createSampleNode(), createSampleNode(), false));
	}
	
	@Test
	public void testInformationalAttribute() throws Exception {
		Node refNode = createSampleNode();
		refNode.setInformational(true);
		
		Node actualNode = createSampleNode();
		actualNode.setInformational(false);
		
		assertNotNull(comparer.compareSimpleNode(refNode, actualNode, true));
		assertNotNull(comparer.compareSimpleNode(refNode, actualNode, false));
		
		assertNotNull(comparer.compareSimpleNode(actualNode, refNode, true));
		assertNotNull(comparer.compareSimpleNode(actualNode, refNode, false));
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
		assertNotNull(comparer.compareSimpleNode(nodes[0], nodes[1], true));
		assertNotNull(comparer.compareSimpleNode(nodes[0], nodes[1], false));
		
		assertNotNull(comparer.compareSimpleNode(nodes[1], nodes[0], true));
		assertNotNull(comparer.compareSimpleNode(nodes[1], nodes[0], false));
		
		// after setting both nodes as informational the comparator 
		// shouldn't return any differences
		nodes[0].setInformational(true);
		nodes[1].setInformational(true);
		
		assertNotNull(comparer.compareSimpleNode(nodes[0], nodes[1], true));
		assertNull(comparer.compareSimpleNode(nodes[0], nodes[1], false));
		
		assertNotNull(comparer.compareSimpleNode(nodes[1], nodes[0], true));
		assertNull(comparer.compareSimpleNode(nodes[1], nodes[0], false));
	}
}
