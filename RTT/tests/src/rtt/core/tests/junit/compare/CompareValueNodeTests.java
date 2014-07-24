package rtt.core.tests.junit.compare;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.ValueNode;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult;

public class CompareValueNodeTests {
	
	private static final String NAME = "ValueNode";
	private static final GeneratorType TYPE = GeneratorType.METHOD;
	private static final String VALUE = "SampleValue";
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new OutputCompare(true);
	}
	
	private ValueNode createSampleNode() {
		return createNode(NAME, TYPE, VALUE);
	}
	
	private ValueNode createNode(String name, GeneratorType type, String value) {
		ValueNode node = new ValueNode();
		node.setGeneratorName(name);
		node.setGeneratorType(type);
		node.setValue(value);
		
		return node;
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
	
	@Test
	public void testEqualValueNodes() throws Exception {
		testNoDifferences(createSampleNode(), createSampleNode());
	}
	
	@Test
	public void testUnEqualValueNodes() throws Exception {
		ValueNode referenceNode = createSampleNode();
		ValueNode actualNode = createSampleNode();
		actualNode.setValue("OtherValue");		
		
		testDifference(referenceNode, actualNode);		
		testDifference(actualNode, referenceNode);		
	}	

}
