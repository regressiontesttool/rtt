package rtt.core.tests.junit.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.ElementType;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareNodeInformationalTests {
	
	private OutputCompare comparer;
	
	@Before
	public void setUp() throws Exception {
		comparer = new OutputCompare(true);
	}
	
	public static Element createSampleNode(boolean informational) {
		return CompareElementTests.createSampleElement(ElementType.NODE, informational);
	}
	
	public enum CreateInfo {
		NONE,
		PARENT,
		CHILDS,
		PARENT_AND_CHILDS;
	}
	
	public static Element createSampleNode(int childCount, ElementType type, CreateInfo status) {
		
		boolean parentIsInfo = ((status == CreateInfo.PARENT) || (status == CreateInfo.PARENT_AND_CHILDS));
		boolean childIsInfo = ((status == CreateInfo.CHILDS) || (status == CreateInfo.PARENT_AND_CHILDS));
		
		Element node = createSampleNode(parentIsInfo);	
		
		for(int i = 0; i < childCount; i++) {
			Element childElement = CompareElementTests.
					createSampleElement(type, childIsInfo);
			
			node.getElements().add(childElement);
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
	
	@Test
	public void testEqualNodes() throws Exception {
		testNoDifferences(createSampleNode(false), createSampleNode(false));
		testNoDifferences(createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE));
		testNoDifferences(createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE));
		testNoDifferences(createSampleNode(4, ElementType.NODE, CreateInfo.NONE), 
				createSampleNode(4, ElementType.NODE, CreateInfo.NONE));
		
		testNoDifferences(createSampleNode(true), createSampleNode(true));
		testNoDifferences(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), 
				createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT));
		testNoDifferences(createSampleNode(2, ElementType.VALUE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.PARENT));
		testNoDifferences(createSampleNode(4, ElementType.NODE, CreateInfo.PARENT), 
				createSampleNode(4, ElementType.NODE, CreateInfo.PARENT));
		
		testNoDifferences(createSampleNode(3, ElementType.REFERENCE, CreateInfo.CHILDS), 
				createSampleNode(3, ElementType.REFERENCE, CreateInfo.CHILDS));
		testNoDifferences(createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS));
		testNoDifferences(createSampleNode(4, ElementType.NODE, CreateInfo.CHILDS), 
				createSampleNode(4, ElementType.NODE, CreateInfo.CHILDS));
	}
	
	// Attribute tests
	
	@Test
	public void testUnequalClassNameAttribute() throws Exception {
		Element changedNode = createSampleNode(false);
		changedNode.setValue("anOtherClassName");
		
		testDifference(createSampleNode(false), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(false), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(createSampleNode(true), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(true), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
	}
	
	// Children tests
	
	@Test
	public void testChildDiffsForInformationalNode() throws Exception {
		// child diffs because of unequal child count
		testDifference(createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.PARENT), Difference.CHILD_COUNT);
		
		// child diffs because of unequal child classes
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), 
				createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT), Difference.ELEMENT_TYPE);
		
		// child diffs because of unequal child informational types
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT_AND_CHILDS), 
				createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), Difference.INFORMATIONAL);
	}
	
	@Test
	public void testValueChild() throws Exception {
		// unequal informational states of child value(s)
		testDifference(createSampleNode(2, ElementType.VALUE, CreateInfo.NONE),
				createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS), Difference.INFORMATIONAL);
		testDifference(createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS),
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.INFORMATIONAL);
	}
	
	@Test
	public void testChildValues() throws Exception {
		testNoDifferences(createSampleNode(1, ElementType.VALUE, CreateInfo.NONE),
				createSampleNode(1, ElementType.VALUE, CreateInfo.NONE));
		
		Element node = createSampleNode(false);
		Element value = CompareElementTests.createSampleElement(ElementType.VALUE, false);
		value.setValue("anOtherValue");
		node.getElements().add(value);
		node.getElements().add(CompareElementTests.createSampleElement(ElementType.VALUE, false));
		
		testDifference(createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), node, Difference.VALUE);
		testDifference(node, createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.VALUE);
	}
	
	@Test
	public void testChildValuesInformational() throws Exception {		
		testNoDifferences(createSampleNode(1, ElementType.VALUE, CreateInfo.CHILDS), 
				createSampleNode(1, ElementType.VALUE, CreateInfo.CHILDS));
		
		Element node = createSampleNode(false);
		Element value = CompareElementTests.createSampleElement(ElementType.VALUE, true);
		value.setValue("anOtherValue");
		node.getElements().add(value);
		node.getElements().add(CompareElementTests.createSampleElement(ElementType.VALUE, true));
		
		testDifference(createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS), node, Difference.VALUE);
		testDifference(node, createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS), Difference.VALUE);
	}	

	
	@Test
	public void testUnequalChildrenCount() throws Exception {
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ElementType.VALUE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ElementType.NODE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.NODE, CreateInfo.NONE), Difference.CHILD_COUNT);
		
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.REFERENCE, CreateInfo.PARENT), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.PARENT), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ElementType.NODE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.NODE, CreateInfo.PARENT), Difference.CHILD_COUNT);
	}
	
	
	@Test
	public void testUneqalChildrenTypes() throws Exception {
		// just different types REFERENCE != VALUE --> Difference.CLASSES
		testDifference(createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.ELEMENT_TYPE);
		
		// different types (REFERENCE != VALUE) && different informational states --> ELEMENT_TYPE
		testDifference(createSampleNode(2, ElementType.REFERENCE, CreateInfo.CHILDS), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.ELEMENT_TYPE);
		
		// different types (REFERENCE != VALUE), but both informational --> ELEMENT_TYPE
		testDifference(createSampleNode(2, ElementType.REFERENCE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.PARENT), Difference.ELEMENT_TYPE);
	}	
	
	@Test
	public void testUnequalChildElement_Name() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE);
		Element changedElement = CompareElementTests.createSampleElement(ElementType.REFERENCE, false);
		changedElement.setName("anOtherName");		
		changedNode.getElements().add(changedElement);
		
		// generator name of child was changed --> Difference.NAME 
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), changedNode, Difference.NAME);
		testDifference(changedNode, createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), Difference.NAME);
		testNoDifferences(changedNode, changedNode);
		
		// generator name of child was changed and informational are compared --> Difference.NAME
		changedNode.setInformational(true);
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), changedNode, Difference.NAME);
		testDifference(changedNode, createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), Difference.NAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildValue() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.VALUE, CreateInfo.NONE);
		Element changedChild = CompareElementTests.createSampleElement(ElementType.VALUE, false);
		changedChild.setValue("anOtherValue");		
		changedNode.getElements().add(changedChild);
		
		// value of child was changed --> Difference.VALUE 
		testDifference(createSampleNode(3, ElementType.VALUE, CreateInfo.NONE), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.VALUE, CreateInfo.NONE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		// value of child was changed and informational are compared --> Difference.VALUE
		changedNode.setInformational(true);
		testDifference(createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildReference() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE);
		Element changedChild = CompareElementTests.createSampleElement(ElementType.REFERENCE, false);
		changedChild.setValue("2.2.2");		
		changedNode.getElements().add(changedChild);
		
		// value of child was changed --> Difference.VALUE 
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		// value of child was changed and informational are compared --> Difference.VALUE
		changedNode.setInformational(true);
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_Classname() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.NODE, CreateInfo.NONE);
		Element changedChild = createSampleNode(false);
		changedChild.setValue("anOtherClassName");
		changedNode.getElements().add(changedChild);
		
		// full name of child was changed -->  Difference.VALUE
		testDifference(createSampleNode(3, ElementType.NODE, CreateInfo.NONE), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.NODE, CreateInfo.NONE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		// full name of child was changed and informational are compared --> Difference.VALUE
		changedNode.setInformational(true);
		testDifference(createSampleNode(3, ElementType.NODE, CreateInfo.PARENT), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.NODE, CreateInfo.PARENT), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_ChildCount() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.NODE, CreateInfo.NONE);
		Element changedChild = createSampleNode(2, ElementType.NODE, CreateInfo.NONE);
		changedNode.getElements().add(changedChild);
		
		// count of child of a child differs --> Difference.CHILD_COUNT
		testDifference(createSampleNode(3, ElementType.NODE, CreateInfo.NONE), changedNode, Difference.CHILD_COUNT);
		testDifference(changedNode, createSampleNode(3, ElementType.NODE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testNoDifferences(changedNode, changedNode);
		
		// count of child of a child differs and informational are compared --> Difference.CHILD_COUNT
		changedNode.setInformational(true);
		testDifference(createSampleNode(3, ElementType.NODE, CreateInfo.PARENT), changedNode, Difference.CHILD_COUNT);
		testDifference(changedNode, createSampleNode(3, ElementType.NODE, CreateInfo.PARENT), Difference.CHILD_COUNT);
		testNoDifferences(changedNode, changedNode);
	}
}
