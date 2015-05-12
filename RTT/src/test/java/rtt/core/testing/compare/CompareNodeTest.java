package rtt.core.testing.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.ElementType;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;

public class CompareNodeTest {
	
	private OutputCompare comparer;
	
	@Before
	public void setUp() throws Exception {
		comparer = new OutputCompare(false);
	}
	
	public static Element createSampleNode(boolean informational) {
		return CompareElementTest.createSampleElement(ElementType.NODE, informational);
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
			Element childElement = CompareElementTest.
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
		testNoDifferences(createSampleNode(true), changedNode);
		testNoDifferences(changedNode, createSampleNode(true));
		testNoDifferences(changedNode, changedNode);
	}
	
	// Children tests
	
	@Test
	public void testNoChildDiffsForInformationalNode() throws Exception {
		// no child diffs despite unequal child count
		testNoDifferences(createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.PARENT));
		
		// no child diffs despite unequal child classes
		testNoDifferences(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), 
				createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT));
		
		// no child diffs despite unequal child informational types
		testNoDifferences(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT_AND_CHILDS), 
				createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT));
	}
	
	@Test
	public void testValueChild() throws Exception {
		// unequal informational states of child value(s)
		testDifference(createSampleNode(2, ElementType.VALUE, CreateInfo.NONE),
				createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS), Difference.CHILD_COUNT);
		testDifference(createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS),
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.CHILD_COUNT);
	}
	
	@Test
	public void testChildValues() throws Exception {
		testNoDifferences(createSampleNode(1, ElementType.VALUE, CreateInfo.NONE),
				createSampleNode(1, ElementType.VALUE, CreateInfo.NONE));
		
		Element node = createSampleNode(false);
		Element value = CompareElementTest.createSampleElement(ElementType.VALUE, false);
		value.setValue("anOtherValue");
		node.getElements().add(value);
		node.getElements().add(CompareElementTest.createSampleElement(ElementType.VALUE, false));
		
		testDifference(createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), node, Difference.VALUE);
		testDifference(node, createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.VALUE);
	}
	
	@Test
	public void testChildValuesInformational() throws Exception {		
		testNoDifferences(createSampleNode(1, ElementType.VALUE, CreateInfo.CHILDS), 
				createSampleNode(1, ElementType.VALUE, CreateInfo.CHILDS));
		
		Element node = createSampleNode(false);
		Element value = CompareElementTest.createSampleElement(ElementType.VALUE, true);
		value.setValue("anOtherValue");
		node.getElements().add(value);
		node.getElements().add(CompareElementTest.createSampleElement(ElementType.VALUE, true));
		
		testNoDifferences(createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS), node);
		testNoDifferences(node, createSampleNode(2, ElementType.VALUE, CreateInfo.CHILDS));
	}	

	
	@Test
	public void testUnequalChildrenCount() throws Exception {
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ElementType.VALUE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ElementType.NODE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.NODE, CreateInfo.NONE), Difference.CHILD_COUNT);
		
		testNoDifferences(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.REFERENCE, CreateInfo.PARENT));
		testNoDifferences(createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.PARENT));
		testNoDifferences(createSampleNode(3, ElementType.NODE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.NODE, CreateInfo.PARENT));
	}
	
	
	@Test
	public void testUneqalChildrenTypes() throws Exception {
		// just different types REFERENCE != VALUE --> Difference.CLASSES
		testDifference(createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.ELEMENT_TYPE);
		
		// different types (REFERENCE != VALUE) && different informational states --> CHILD_COUNT
		testDifference(createSampleNode(2, ElementType.REFERENCE, CreateInfo.CHILDS), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.NONE), Difference.CHILD_COUNT);
		
		// different types (REFERENCE != VALUE), but both informational --> no difference
		testNoDifferences(createSampleNode(2, ElementType.REFERENCE, CreateInfo.PARENT), 
				createSampleNode(2, ElementType.VALUE, CreateInfo.PARENT));
	}	
	
	@Test
	public void testUnequalChildElement_Name() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE);
		Element changedElement = CompareElementTest.createSampleElement(ElementType.REFERENCE, false);
		changedElement.setName("anOtherName");		
		changedNode.getElements().add(changedElement);
		
		// generator name of child was changed --> Difference.NAME 
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), changedNode, Difference.NAME);
		testDifference(changedNode, createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), Difference.NAME);
		testNoDifferences(changedNode, changedNode);
		
		// generator name of child was changed, but node is informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildValue() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.VALUE, CreateInfo.NONE);
		Element changedChild = CompareElementTest.createSampleElement(ElementType.VALUE, false);
		changedChild.setValue("anOtherValue");		
		changedNode.getElements().add(changedChild);
		
		// value of child was changed --> Difference.VALUE 
		testDifference(createSampleNode(3, ElementType.VALUE, CreateInfo.NONE), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.VALUE, CreateInfo.NONE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		// value of child was changed, but node is informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ElementType.VALUE, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildReference() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.REFERENCE, CreateInfo.NONE);
		Element changedChild = CompareElementTest.createSampleElement(ElementType.REFERENCE, false);
		changedChild.setValue("2.2.2");		
		changedNode.getElements().add(changedChild);
		
		// value of child was changed --> Difference.VALUE 
		testDifference(createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.REFERENCE, CreateInfo.NONE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		// value of child was changed, but node is informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ElementType.REFERENCE, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_Classname() throws Exception {
		Element changedNode = createSampleNode(2, ElementType.NODE, CreateInfo.NONE);
		Element changedChild = createSampleNode(false);
		changedChild.setValue("anOtherClassName");
		changedNode.getElements().add(changedChild);
		
		// full name of child was changed --> Difference.FULLNAME
		testDifference(createSampleNode(3, ElementType.NODE, CreateInfo.NONE), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ElementType.NODE, CreateInfo.NONE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		// full name of child was changed, but node was informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ElementType.NODE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ElementType.NODE, CreateInfo.PARENT));
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
		
		// count of child of a child differs, but node was informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ElementType.NODE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ElementType.NODE, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
}
