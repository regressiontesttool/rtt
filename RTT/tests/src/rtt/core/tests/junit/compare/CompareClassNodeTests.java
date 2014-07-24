package rtt.core.tests.junit.compare;

import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.ValueNode;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult;

public class CompareClassNodeTests {
	
	private static final String NAME = "ValueNode";
	private static final GeneratorType TYPE = GeneratorType.METHOD;
	private static final String FULLNAME = "my.example.MyObject";
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new OutputCompare(true);
	}
	
	private ClassNode createSampleNode() {
		return createNode(NAME, TYPE, FULLNAME);
	}
	
	private ClassNode createNode(String name, GeneratorType type, String fullName) {
		ClassNode node = new ClassNode();
		node.setGeneratorName(name);
		node.setGeneratorType(type);
		
		node.setFullName(fullName);
		node.setSimpleName(fullName.substring(fullName.lastIndexOf("."), fullName.length()));
		
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
	public void testEqualClassNodes() throws Exception {
		testNoDifferences(createSampleNode(), createSampleNode());
	}
	
	@Test
	public void testSimpleNameAttribute() throws Exception {
		ClassNode referenceNode = createSampleNode();
		ClassNode actualNode = createSampleNode();
		actualNode.setSimpleName("newSimpleName");		
		
		testDifference(referenceNode, actualNode);		
		testDifference(actualNode, referenceNode);		
	}
	
	@Test
	public void testFullNameAttribute() throws Exception {
		ClassNode referenceNode = createSampleNode();
		ClassNode actualNode = createSampleNode();
		actualNode.setFullName("newFullName");
		
		testDifference(referenceNode, actualNode);		
		testDifference(actualNode, referenceNode);		
	}
	
	@Test
	public void testChildNodeEquals() throws Exception {
		ClassNode referenceNode = createSampleNode();
		referenceNode.getNodes().add(createSampleNode());
		ClassNode actualNode = createSampleNode();
		actualNode.getNodes().add(createSampleNode());
		
		testNoDifferences(referenceNode, actualNode);		
		testNoDifferences(actualNode, referenceNode);		
	}
	
	@Test
	public void testChildNodeCount() throws Exception {
		ClassNode referenceNode = createSampleNode();
		ClassNode actualNode = createSampleNode();
		actualNode.getNodes().add(createSampleNode());
		
		testDifference(referenceNode, actualNode);		
		testDifference(actualNode, referenceNode);		
	}
	
	@Test
	public void testChildNodeType() throws Exception {
		ClassNode referenceNode = createSampleNode();
		referenceNode.getNodes().add(createSampleNode());
		ClassNode actualNode = createSampleNode();
		actualNode.getNodes().add(new ValueNode());
		
		testDifference(referenceNode, actualNode);		
		testDifference(actualNode, referenceNode);		
	}
	
	@Test
	public void testChildNodeCreatorName() throws Exception {
		ClassNode referenceNode = createSampleNode();
		referenceNode.getNodes().add(createSampleNode());
		ClassNode actualNode = createSampleNode();
		actualNode.getNodes().add(createNode("OtherName", TYPE, FULLNAME));
		
		testDifference(referenceNode, actualNode);		
		testDifference(actualNode, referenceNode);		
	}
	
	@Test
	public void testChildNodeCreatorType() throws Exception {
		ClassNode referenceNode = createSampleNode();
		referenceNode.getNodes().add(createSampleNode());
		ClassNode actualNode = createSampleNode();
		actualNode.getNodes().add(createNode(NAME, GeneratorType.FIELD, FULLNAME));
		
		testDifference(referenceNode, actualNode);		
		testDifference(actualNode, referenceNode);		
	}
	
	@Test
	public void testChildNodeClassName() throws Exception {
		ClassNode referenceNode = createSampleNode();
		referenceNode.getNodes().add(createSampleNode());
		ClassNode actualNode = createSampleNode();
		actualNode.getNodes().add(createNode(NAME, TYPE, "my.second.Class"));
		
		testDifference(referenceNode, actualNode);		
		testDifference(actualNode, referenceNode);		
	}

}
