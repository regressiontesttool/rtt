package rtt.core.tests.junit.compare;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.GeneratorType;
import rtt.core.testing.compare.ClassNodeComparer;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.Comparer;

public class CompareClassNodeTests {

	private static final String NAME = "ValueNode";
	private static final GeneratorType TYPE = GeneratorType.METHOD;
	private static final String FULLNAME = "my.example.MyObject";
	
	private Comparer<ClassNode> comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new ClassNodeComparer();
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
	
	private boolean areDifferent(ClassNode referenceNode, ClassNode actualNode) {
		CompareResult result = comparer.compareNodes(referenceNode, actualNode);
		return result != null && result.hasDifferences();
	}

	@Test
	public void testEqualClassNodes() throws Exception {
		assertFalse(areDifferent(createSampleNode(), createSampleNode()));
	}
	
	@Test
	public void testSimpleNameAttribute() throws Exception {
		ClassNode referenceNode = createSampleNode();
		ClassNode actualNode = createSampleNode();
		actualNode.setSimpleName("newSimpleName");		
		
		assertTrue(areDifferent(referenceNode, actualNode));		
		assertTrue(areDifferent(actualNode, referenceNode));		
	}
	
	@Test
	public void testFullNameAttribute() throws Exception {
		ClassNode referenceNode = createSampleNode();
		ClassNode actualNode = createSampleNode();
		actualNode.setFullName("newFullName");
		
		assertTrue(areDifferent(referenceNode, actualNode));		
		assertTrue(areDifferent(actualNode, referenceNode));		
	}

}
