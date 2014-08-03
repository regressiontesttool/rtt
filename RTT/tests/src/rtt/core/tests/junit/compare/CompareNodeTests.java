package rtt.core.tests.junit.compare;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareNodeTests {
	
	public enum ChildType {
		ELEMENT, VALUE, NODE;		
	}

	private static final String NAME = "aName";
	private static final Type TYPE = Type.ELEMENT;
	private static final String FULL_NAME = "aFullName";
	private static final String SIMPLE_NAME = "aSimpleName";
	
	private OutputCompare comparer;
	
	@Before
	public void setUp() throws Exception {
		comparer = new OutputCompare(true);
	}
	
	private Node createNode(String name, Type type, String fullName, String simpleName) {
		Node node = new Node();
		node.setName(name);
		node.setType(type);
		node.setFullName(fullName);
		node.setSimpleName(simpleName);
		
		return node;
	}
	
	private Node createSampleNode() {
		return createNode(NAME, TYPE, FULL_NAME, SIMPLE_NAME);
	}
	
	private Node createSampleNode(int childCount, ChildType type) {
		Node node = createSampleNode();
		
		for(int i = 0; i < childCount; i++) {
			Element childElement = null;
			
			switch (type) {
			case ELEMENT:
				childElement = CompareElementTests.createSampleElement();
				break;
			case VALUE:
				childElement = CompareValueTests.createSampleValue();
				break;			
			case NODE:
				childElement = createSampleNode();
				break;				
			}
			
			node.getElement().add(childElement);
		}
		
		return node;		
	}
	
	private void testThrowsException(Element referenceElement, Element actualElement, Class<? extends Throwable> expectedException) {
		CompareUtils.testThrowsException(comparer, referenceElement, actualElement, expectedException);
	}
	
	private void testNoDifferences(Element referenceElement, Element actualElement) {
		CompareUtils.testNoDifferences(comparer, referenceElement, actualElement);
	}
	
	private void testDifference(Element referenceElement, Element actualElement, Difference expected) {
		CompareUtils.testDifference(comparer, referenceElement, actualElement, expected);
	}
	
	@Test
	public void testNullNodes() throws Exception {
		testThrowsException(null, null, IllegalArgumentException.class);		
		testThrowsException(createSampleNode(), null, IllegalArgumentException.class);
		testThrowsException(null, createSampleNode(), IllegalArgumentException.class);
	}	

	@Test
	public void testEqualNodes() throws Exception {
		testNoDifferences(createSampleNode(), createSampleNode());
		testNoDifferences(createSampleNode(3, ChildType.ELEMENT), createSampleNode(3, ChildType.ELEMENT));
		testNoDifferences(createSampleNode(2, ChildType.VALUE), createSampleNode(2, ChildType.VALUE));
		testNoDifferences(createSampleNode(4, ChildType.NODE), createSampleNode(4, ChildType.NODE));
	}
	
	@Test
	public void testUnequalFullNameAttribute() throws Exception {
		Node changedNode = createSampleNode();
		changedNode.setFullName("anOtherFullName");
		
		testDifference(createSampleNode(), changedNode, Difference.FULLNAME);
		testDifference(changedNode, createSampleNode(), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalSimpleNameAttribute() throws Exception {
		Node changedNode = createSampleNode();
		changedNode.setSimpleName("anOtherSimpleName");
		
		testDifference(createSampleNode(), changedNode, Difference.SIMPLENAME);
		testDifference(changedNode, createSampleNode(), Difference.SIMPLENAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildrenCount() throws Exception {
		testDifference(createSampleNode(3, ChildType.ELEMENT), createSampleNode(2, ChildType.ELEMENT), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ChildType.VALUE), createSampleNode(2, ChildType.VALUE), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ChildType.NODE), createSampleNode(2, ChildType.NODE), Difference.CHILD_COUNT);
	}
	
	@Test
	public void testUneqalChildrenTypes() throws Exception {
		testDifference(createSampleNode(2, ChildType.ELEMENT), createSampleNode(2, ChildType.VALUE), Difference.CLASSES);
	}
	
	@Test
	public void testUnequalChildElement_Name() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.ELEMENT);
		Element changedElement = CompareElementTests.createSampleElement();
		changedElement.setName("anOtherName");		
		changedNode.getElement().add(changedElement);
		
		testDifference(createSampleNode(3, ChildType.ELEMENT), changedNode, Difference.NAME);
		testDifference(changedNode, createSampleNode(3, ChildType.ELEMENT), Difference.NAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildValue() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.VALUE);
		Value changedValue = CompareValueTests.createSampleValue();
		changedValue.setValue("anOtherValue");		
		changedNode.getElement().add(changedValue);
		
		testDifference(createSampleNode(3, ChildType.VALUE), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ChildType.VALUE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_Fullname() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.NODE);
		Node changedValue = createSampleNode();
		changedValue.setFullName("anOtherFullName");
		changedNode.getElement().add(changedValue);
		
		testDifference(createSampleNode(3, ChildType.NODE), changedNode, Difference.FULLNAME);
		testDifference(changedNode, createSampleNode(3, ChildType.NODE), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_ChildCount() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.NODE);
		Node changedValue = createSampleNode(2, ChildType.NODE);
		changedNode.getElement().add(changedValue);
		
		testDifference(createSampleNode(3, ChildType.NODE), changedNode, Difference.CHILD_COUNT);
		testDifference(changedNode, createSampleNode(3, ChildType.NODE), Difference.CHILD_COUNT);
		testNoDifferences(changedNode, changedNode);
	}
}
