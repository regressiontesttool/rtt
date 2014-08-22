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

public class CompareNodeTests {
	
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
		comparer = new OutputCompare(false);
	}
	
	private static Node createNode(String name, Type type, String fullName, String simpleName, boolean informational) {
		Node node = new Node();
		node.setGeneratorName(name);
		node.setGeneratorType(type);
		node.setInformational(informational);
		node.setFullName(fullName);
		node.setSimpleName(simpleName);
		
		return node;
	}
	
	public static Node createSampleNode(boolean informational) {
		return createNode(NAME, TYPE, FULL_NAME, SIMPLE_NAME, informational);
	}
	
	public enum CreateInfo {
		NONE,
		PARENT,
		CHILDS,
		PARENT_AND_CHILDS;
	}
	
	public static Node createSampleNode(int childCount, ChildType type, CreateInfo status) {
		
		boolean parentIsInfo = ((status == CreateInfo.PARENT) || (status == CreateInfo.PARENT_AND_CHILDS));
		boolean childIsInfo = ((status == CreateInfo.CHILDS) || (status == CreateInfo.PARENT_AND_CHILDS));
		
		Node node = createSampleNode(parentIsInfo);	
		
		for(int i = 0; i < childCount; i++) {
			Element childElement = null;
			
			switch (type) {
			case ELEMENT:
				childElement = CompareElementTests.createSampleElement(childIsInfo);
				break;
			case VALUE:
				childElement = CompareValueTests.createSampleValue(childIsInfo);
				break;			
			case NODE:
				childElement = createSampleNode(childIsInfo);
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
		testNoDifferences(createSampleNode(true), changedNode);
		testNoDifferences(changedNode, createSampleNode(true));
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
		testNoDifferences(createSampleNode(true), changedNode);
		testNoDifferences(changedNode, createSampleNode(true));
		testNoDifferences(changedNode, changedNode);
	}
	
	// Children tests
	
	@Test
	public void testNoChildDiffsForInformationalNode() throws Exception {
		// no child diffs despite unequal child count
		testNoDifferences(createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.PARENT));
		
		// no child diffs despite unequal child classes
		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), 
				createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT));
		
		// no child diffs despite unequal child informational types
		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT_AND_CHILDS), 
				createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT));
	}
	
	@Test
	public void testValueChild() throws Exception {
		// unequal informational states of child value(s)
		testDifference(createSampleNode(2, ChildType.VALUE, CreateInfo.NONE),
				createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS), Difference.CHILD_COUNT);
		testDifference(createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS),
				createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.CHILD_COUNT);
	}
	
	@Test
	public void testChildValues() throws Exception {
		testNoDifferences(createSampleNode(1, ChildType.VALUE, CreateInfo.NONE),
				createSampleNode(1, ChildType.VALUE, CreateInfo.NONE));
		
		Node node = createSampleNode(false);
		Value value = CompareValueTests.createSampleValue(false);
		value.setValue("anOtherValue");
		node.getElement().add(value);
		node.getElement().add(CompareValueTests.createSampleValue(false));
		
		testDifference(createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), node, Difference.VALUE);
		testDifference(node, createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.VALUE);
	}
	
	@Test
	public void testChildValuesInformational() throws Exception {		
		testNoDifferences(createSampleNode(1, ChildType.VALUE, CreateInfo.CHILDS), 
				createSampleNode(1, ChildType.VALUE, CreateInfo.CHILDS));
		
		Node node = createSampleNode(false);
		Value value = CompareValueTests.createSampleValue(true);
		value.setValue("anOtherValue");
		node.getElement().add(value);
		node.getElement().add(CompareValueTests.createSampleValue(true));
		
		testNoDifferences(createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS), node);
		testNoDifferences(node, createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS));
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
