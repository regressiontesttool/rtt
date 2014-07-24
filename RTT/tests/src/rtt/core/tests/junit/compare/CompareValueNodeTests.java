package rtt.core.tests.junit.compare;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.ValueNode;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.Comparer;
import rtt.core.testing.compare.ValueNodeComparer;

public class CompareValueNodeTests {

	private static final String NAME = "ValueNode";
	private static final GeneratorType TYPE = GeneratorType.METHOD;
	private static final String VALUE = "SampleValue";
	
	private Comparer<ValueNode> comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new ValueNodeComparer();
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
	
	private boolean areDifferent(ValueNode referenceNode, ValueNode actualNode) {
		CompareResult result = comparer.compareNodes(referenceNode, actualNode);
		return result != null && result.hasDifferences();
	}

	@Test
	public void testEqualValueNodes() throws Exception {
		assertFalse(areDifferent(createSampleNode(), createSampleNode()));
	}
	
	@Test
	public void testUnEqualValueNodes() throws Exception {
		ValueNode referenceNode = createSampleNode();
		ValueNode actualNode = createSampleNode();
		actualNode.setValue("OtherValue");		
		
		assertTrue(areDifferent(referenceNode, actualNode));		
		assertTrue(areDifferent(actualNode, referenceNode));		
	}	

}
