package rtt.core.tests.junit.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareNodeInformationalTests {
	
	public enum ChildType {
		ELEMENT, VALUE, NODE;		
	}

	private static final String NAME = "aName";
	private static final Type TYPE = Type.OBJECT;
	private static final String FULL_NAME = "aFullName";
	private static final String SIMPLE_NAME = "aSimpleName";
	
	private OutputCompare comparer;
	
	@Before
	public void setUp() throws Exception {
		comparer = new OutputCompare(true);
	}
	
	private Node createNode(String name, Type type, String fullName, String simpleName, boolean informational) {
		Node node = new Node();
		node.setGeneratorName(name);
		node.setGeneratorType(type);
		node.setInformational(informational);
		node.setFullName(fullName);
		node.setSimpleName(simpleName);
		
		return node;
	}
	
	private Node createSampleNode(boolean informational) {
		return createNode(NAME, TYPE, FULL_NAME, SIMPLE_NAME, informational);
	}
	
	private Node createSampleNode(int childCount, ChildType type, 
			boolean informational) {		
		return createSampleNode(childCount, type, informational, false);
	}
	
	private Node createSampleNode(int childCount, ChildType type, 
			boolean informational, boolean childInfos) {
		
		Node node = createSampleNode(informational);
		
		for(int i = 0; i < childCount; i++) {
			Element childElement = null;
			
			switch (type) {
			case ELEMENT:
				childElement = CompareElementTests.createSampleElement(childInfos);
				break;
			case VALUE:
				childElement = CompareValueTests.createSampleValue(childInfos);
				break;			
			case NODE:
				childElement = createSampleNode(childInfos);
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
		testThrowsException(createSampleNode(false), null, IllegalArgumentException.class);
		testThrowsException(null, createSampleNode(false), IllegalArgumentException.class);
		
		testThrowsException(null, null, IllegalArgumentException.class);		
		testThrowsException(createSampleNode(true), null, IllegalArgumentException.class);
		testThrowsException(null, createSampleNode(true), IllegalArgumentException.class);
	}
	
	// Attribute tests
	
	@Test
	public void testUnequalFullNameAttribute() throws Exception {
		Node changedNode = createSampleNode(false);
		changedNode.setFullName("anOtherFullName");
		
		testDifference(createSampleNode(false), changedNode, Difference.FULLNAME);
		testDifference(changedNode, createSampleNode(false), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(createSampleNode(true), changedNode, Difference.FULLNAME);
		testDifference(changedNode, createSampleNode(true), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalSimpleNameAttribute() throws Exception {
		Node changedNode = createSampleNode(false);
		changedNode.setSimpleName("anOtherSimpleName");
		
		testDifference(createSampleNode(false), changedNode, Difference.SIMPLENAME);
		testDifference(changedNode, createSampleNode(false), Difference.SIMPLENAME);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(createSampleNode(true), changedNode, Difference.SIMPLENAME);
		testDifference(changedNode, createSampleNode(true), Difference.SIMPLENAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	// Children tests
	
	@Test
	public void testChildDiffsForInformationalNode() throws Exception {
		// child diffs because of unequal child count
		testDifference(createSampleNode(3, ChildType.VALUE, true), 
				createSampleNode(2, ChildType.VALUE, true), Difference.CHILD_COUNT);
		
		// child diffs because of unequal child classes
		testDifference(createSampleNode(3, ChildType.ELEMENT, true), 
				createSampleNode(3, ChildType.VALUE, true), Difference.CLASSES);
		
		// child diffs because of unequal child informational types
		testDifference(createSampleNode(3, ChildType.ELEMENT, true, true), 
				createSampleNode(3, ChildType.ELEMENT, true), Difference.INFORMATIONAL);
	}
	
	@Test
	public void testValueChild() throws Exception {
		// unequal informational states of child value(s)
		testDifference(createSampleNode(2, ChildType.VALUE, false),
				createSampleNode(2, ChildType.VALUE, false, true), Difference.INFORMATIONAL);
		testDifference(createSampleNode(2, ChildType.VALUE, false, true),
				createSampleNode(2, ChildType.VALUE, false), Difference.INFORMATIONAL);
	}
	
	@Test
	public void testChildValues() throws Exception {
		testNoDifferences(createSampleNode(1, ChildType.VALUE, false),
				createSampleNode(1, ChildType.VALUE, false));
		
		Node node = createSampleNode(false);
		Value value = CompareValueTests.createSampleValue(false);
		value.setValue("anOtherValue");
		node.getElement().add(value);
		node.getElement().add(CompareValueTests.createSampleValue(false));
		
		testDifference(createSampleNode(2, ChildType.VALUE, false), node, Difference.VALUE);
		testDifference(node, createSampleNode(2, ChildType.VALUE, false), Difference.VALUE);
	}
	
	@Test
	public void testChildValuesInformational() throws Exception {		
		testNoDifferences(createSampleNode(1, ChildType.VALUE, false, true), 
				createSampleNode(1, ChildType.VALUE, false, true));
		
		Node node = createSampleNode(false);
		Value value = CompareValueTests.createSampleValue(true);
		value.setValue("anOtherValue");
		node.getElement().add(value);
		node.getElement().add(CompareValueTests.createSampleValue(true));
		
		testDifference(createSampleNode(2, ChildType.VALUE, false, true), node, Difference.VALUE);
		testDifference(node, createSampleNode(2, ChildType.VALUE, false, true), Difference.VALUE);
	}
	
	
	
	
	
	
	
//	@Test
//	public void testEqualNodes() throws Exception {
//		testNoDifferences(createSampleNode(false), createSampleNode(false));
//		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, false), createSampleNode(3, ChildType.ELEMENT, false));
//		testNoDifferences(createSampleNode(2, ChildType.VALUE, false), createSampleNode(2, ChildType.VALUE, false));
//		testNoDifferences(createSampleNode(4, ChildType.NODE, false), createSampleNode(4, ChildType.NODE, false));
//		
//		testNoDifferences(createSampleNode(true), createSampleNode(true));
//		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, true), createSampleNode(3, ChildType.ELEMENT, true));
//		testNoDifferences(createSampleNode(2, ChildType.VALUE, true), createSampleNode(2, ChildType.VALUE, true));
//		testNoDifferences(createSampleNode(4, ChildType.NODE, true), createSampleNode(4, ChildType.NODE, true));
//		
//		comparer = new OutputCompare(true);
//		
//		testNoDifferences(createSampleNode(false), createSampleNode(false));
//		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, false), createSampleNode(3, ChildType.ELEMENT, false));
//		testNoDifferences(createSampleNode(2, ChildType.VALUE, false), createSampleNode(2, ChildType.VALUE, false));
//		testNoDifferences(createSampleNode(4, ChildType.NODE, false), createSampleNode(4, ChildType.NODE, false));
//		
//		testNoDifferences(createSampleNode(true), createSampleNode(true));
//		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, true), createSampleNode(3, ChildType.ELEMENT, true));
//		testNoDifferences(createSampleNode(2, ChildType.VALUE, true), createSampleNode(2, ChildType.VALUE, true));
//		testNoDifferences(createSampleNode(4, ChildType.NODE, true), createSampleNode(4, ChildType.NODE, true));
//	}
//	
//	@Test
//	public void testUnequalChildrenCount() throws Exception {
//		testDifference(createSampleNode(3, ChildType.ELEMENT, false), 
//				createSampleNode(2, ChildType.ELEMENT, false), Difference.CHILD_COUNT);
//		testDifference(createSampleNode(3, ChildType.VALUE, false), 
//				createSampleNode(2, ChildType.VALUE, false), Difference.CHILD_COUNT);
//		testDifference(createSampleNode(3, ChildType.NODE, false), 
//				createSampleNode(2, ChildType.NODE, false), Difference.CHILD_COUNT);
//		
//		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, true), 
//				createSampleNode(2, ChildType.ELEMENT, true));
//		testNoDifferences(createSampleNode(3, ChildType.VALUE, true), 
//				createSampleNode(2, ChildType.VALUE, true));
//		testNoDifferences(createSampleNode(3, ChildType.NODE, true), 
//				createSampleNode(2, ChildType.NODE, true));
//		
//		comparer = new OutputCompare(true);
//		
//		testDifference(createSampleNode(3, ChildType.ELEMENT, false), 
//				createSampleNode(2, ChildType.ELEMENT, false), Difference.CHILD_COUNT);
//		testDifference(createSampleNode(3, ChildType.VALUE, false), 
//				createSampleNode(2, ChildType.VALUE, false), Difference.CHILD_COUNT);
//		testDifference(createSampleNode(3, ChildType.NODE, false), 
//				createSampleNode(2, ChildType.NODE, false), Difference.CHILD_COUNT);
//		
//		testDifference(createSampleNode(3, ChildType.ELEMENT, true), 
//				createSampleNode(2, ChildType.ELEMENT, true), Difference.CHILD_COUNT);
//		testDifference(createSampleNode(3, ChildType.VALUE, true), 
//				createSampleNode(2, ChildType.VALUE, true), Difference.CHILD_COUNT);
//		testDifference(createSampleNode(3, ChildType.NODE, true), 
//				createSampleNode(2, ChildType.NODE, true), Difference.CHILD_COUNT);
//	}
//	
//	@Test
//	public void testUneqalChildrenTypes() throws Exception {
//		testDifference(createSampleNode(2, ChildType.ELEMENT, false), 
//				createSampleNode(2, ChildType.VALUE, false), Difference.CLASSES);
//		
//		testDifference(createSampleNode(2, ChildType.ELEMENT, false, true), 
//				createSampleNode(2, ChildType.VALUE, false, false), Difference.CLASSES);
//		
//		testNoDifferences(createSampleNode(2, ChildType.ELEMENT, true), 
//				createSampleNode(2, ChildType.VALUE, true));
//		
//		comparer = new OutputCompare(true);
//		
//		testDifference(createSampleNode(2, ChildType.ELEMENT, false), 
//				createSampleNode(2, ChildType.VALUE, false), Difference.CLASSES);
//		
//		testDifference(createSampleNode(2, ChildType.ELEMENT, true), 
//				createSampleNode(2, ChildType.VALUE, true), Difference.CLASSES);
//	}
//	
//	@Test
//	public void testUnequalChildElement_Name() throws Exception {
//		Node changedNode = createSampleNode(2, ChildType.ELEMENT, false);
//		Element changedElement = CompareElementTests.createSampleElement(false);
//		changedElement.setName("anOtherName");		
//		changedNode.getElement().add(changedElement);
//		
//		testDifference(createSampleNode(3, ChildType.ELEMENT, false), changedNode, Difference.NAME);
//		testDifference(changedNode, createSampleNode(3, ChildType.ELEMENT, false), Difference.NAME);
//		testNoDifferences(changedNode, changedNode);
//		
//		changedNode.setInformational(true);
//		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, true), changedNode);
//		testNoDifferences(changedNode, createSampleNode(3, ChildType.ELEMENT, true));
//		testNoDifferences(changedNode, changedNode);
//		
//		comparer = new OutputCompare(true);
//		
//		changedNode = createSampleNode(2, ChildType.ELEMENT, false);
//		changedElement = CompareElementTests.createSampleElement(true);
//		changedElement.setName("anOtherName");		
//		changedNode.getElement().add(changedElement);
//		
//		testDifference(createSampleNode(3, ChildType.ELEMENT, false), changedNode, Difference.NAME);
//		testDifference(changedNode, createSampleNode(3, ChildType.ELEMENT, false), Difference.NAME);
//		testNoDifferences(changedNode, changedNode);
//		
//		changedNode.setInformational(true);
//		testDifference(createSampleNode(3, ChildType.ELEMENT, true), changedNode, Difference.NAME);
//		testDifference(changedNode, createSampleNode(3, ChildType.ELEMENT, true), Difference.NAME);
//		testNoDifferences(changedNode, changedNode);
//	}
//	
//	@Test
//	public void testUnequalChildValue() throws Exception {
//		Node changedNode = createSampleNode(2, ChildType.VALUE, false);
//		Value changedValue = CompareValueTests.createSampleValue(false);
//		changedValue.setValue("anOtherValue");		
//		changedNode.getElement().add(changedValue);
//		
//		testDifference(createSampleNode(3, ChildType.VALUE, false), changedNode, Difference.VALUE);
//		testDifference(changedNode, createSampleNode(3, ChildType.VALUE, false), Difference.VALUE);
//		testNoDifferences(changedNode, changedNode);
//	}
//	
//	@Test
//	public void testUnequalChildNode_Fullname() throws Exception {
//		Node changedNode = createSampleNode(2, ChildType.NODE, false);
//		Node changedValue = createSampleNode(false);
//		changedValue.setFullName("anOtherFullName");
//		changedNode.getElement().add(changedValue);
//		
//		testDifference(createSampleNode(3, ChildType.NODE, false), changedNode, Difference.FULLNAME);
//		testDifference(changedNode, createSampleNode(3, ChildType.NODE, false), Difference.FULLNAME);
//		testNoDifferences(changedNode, changedNode);
//	}
//	
//	@Test
//	public void testUnequalChildNode_ChildCount() throws Exception {
//		Node changedNode = createSampleNode(2, ChildType.NODE, false);
//		Node changedValue = createSampleNode(2, ChildType.NODE, false);
//		changedNode.getElement().add(changedValue);
//		
//		testDifference(createSampleNode(3, ChildType.NODE, false), changedNode, Difference.CHILD_COUNT);
//		testDifference(changedNode, createSampleNode(3, ChildType.NODE, false), Difference.CHILD_COUNT);
//		testNoDifferences(changedNode, changedNode);
//	}
}
