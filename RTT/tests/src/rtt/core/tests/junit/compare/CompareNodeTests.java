package rtt.core.tests.junit.compare;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Node;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult;

public class CompareNodeTests {

	private static final String NAME = "Node";
	private static final GeneratorType TYPE = GeneratorType.METHOD;
	
	private OutputCompare comparer;
	
	@Before
	public void setUp() throws Exception {
		comparer = new OutputCompare(true);
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
	
	private void testNoDifferences(Node referenceNode, Node actualNode) {
		CompareResult result = comparer.compareNodes(referenceNode, actualNode);
		if (result != null) {
			fail("Differences found, but there should not: " + result.getDifference());
		}
	}
	
	private void testDifference(Node referenceNode, Node actualNode) {
		CompareResult result = comparer.compareNodes(referenceNode, actualNode);
		if (result == null) {
			fail("Compare found no differences, but there should be some.");
		}
	}
	
	private void checkThrowsException(Node refNode, Node actualNode) {
		
		try {
			comparer.compareNodes(refNode, actualNode);
			fail("No exception was thrown.");
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
		testNoDifferences(createSampleNode(), createSampleNode());
	}	

	@Test
	public void testTypeAttribute() throws Exception {
		Node node = createSampleNode();
		node.setGeneratorType(GeneratorType.FIELD);
		
		testDifference(node, createSampleNode());		
		testDifference(createSampleNode(), node);
	}
	
	@Test
	public void testNameAttribute() throws Exception {
		Node node = createSampleNode();
		node.setGeneratorName("OtherNode");
		
		testDifference(node, createSampleNode());		
		testDifference(createSampleNode(), node);
	}
	
	@Test
	public void testIsNullAttribute() throws Exception {
		Node node = createSampleNode();
		node.setIsNull(true);
		
		testDifference(node, createSampleNode());		
		testDifference(createSampleNode(), node);
	}
	
	@Test
	public void testInformationalNodes() throws Exception {
		comparer = new OutputCompare(false);
		
		Node referenceNode = createSampleNode();
		Node actualNode = createSampleNode();
		
		testNoDifferences(referenceNode, actualNode);
		
		actualNode.setGeneratorName("otherName");
		actualNode.setGeneratorType(GeneratorType.FIELD);
		actualNode.setIsNull(true);
		actualNode.setInformational(true);
		
		testDifference(referenceNode, actualNode);
		
		referenceNode.setInformational(true);
		testNoDifferences(referenceNode, actualNode);
	}
}
