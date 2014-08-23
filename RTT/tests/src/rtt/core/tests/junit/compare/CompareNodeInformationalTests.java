package rtt.core.tests.junit.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Reference;
import rtt.core.archive.output.Value;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.compare.CompareNodeTests.ChildType;
import rtt.core.tests.junit.compare.CompareNodeTests.CreateInfo;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareNodeInformationalTests {
	
	private OutputCompare comparer;
	
	@Before
	public void setUp() throws Exception {
		comparer = new OutputCompare(true);
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
		testThrowsException(CompareNodeTests.createSampleNode(false), null, IllegalArgumentException.class);
		testThrowsException(null, CompareNodeTests.createSampleNode(false), IllegalArgumentException.class);
		
		testThrowsException(null, null, IllegalArgumentException.class);		
		testThrowsException(CompareNodeTests.createSampleNode(true), null, IllegalArgumentException.class);
		testThrowsException(null, CompareNodeTests.createSampleNode(true), IllegalArgumentException.class);
	}
	
	// Attribute tests
	
	@Test
	public void testUnequalFullNameAttribute() throws Exception {
		Node changedNode = CompareNodeTests.createSampleNode(false);
		changedNode.setFullName("anOtherFullName");
		
		testDifference(CompareNodeTests.createSampleNode(false), changedNode, Difference.FULLNAME);
		testDifference(changedNode, CompareNodeTests.createSampleNode(false), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(CompareNodeTests.createSampleNode(true), changedNode, Difference.FULLNAME);
		testDifference(changedNode, CompareNodeTests.createSampleNode(true), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalSimpleNameAttribute() throws Exception {
		Node changedNode = CompareNodeTests.createSampleNode(false);
		changedNode.setSimpleName("anOtherSimpleName");
		
		testDifference(CompareNodeTests.createSampleNode(false), changedNode, Difference.SIMPLENAME);
		testDifference(changedNode, CompareNodeTests.createSampleNode(false), Difference.SIMPLENAME);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(CompareNodeTests.createSampleNode(true), changedNode, Difference.SIMPLENAME);
		testDifference(changedNode, CompareNodeTests.createSampleNode(true), Difference.SIMPLENAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	// Children tests
	
	@Test
	public void testChildDiffsForInformationalNode() throws Exception {
		// child diffs because of unequal child count
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT), 
				CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.PARENT), Difference.CHILD_COUNT);
		
		// child diffs because of unequal child classes
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), 
				CompareNodeTests.createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT), Difference.CLASSES);
		
		// child diffs because of unequal child informational types
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT_AND_CHILDS), 
				CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), Difference.INFORMATIONAL);
	}
	
	@Test
	public void testValueChild() throws Exception {
		// unequal informational states of child value(s)
		testDifference(CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.NONE),
				CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS), Difference.INFORMATIONAL);
		testDifference(CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS),
				CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.INFORMATIONAL);
	}
	
	@Test
	public void testChildValues() throws Exception {
		testNoDifferences(CompareNodeTests.createSampleNode(1, ChildType.VALUE, CreateInfo.NONE),
				CompareNodeTests.createSampleNode(1, ChildType.VALUE, CreateInfo.NONE));
		
		Node node = CompareNodeTests.createSampleNode(false);
		Value value = CompareValueTests.createSampleValue(false);
		value.setValue("anOtherValue");
		node.getElement().add(value);
		node.getElement().add(CompareValueTests.createSampleValue(false));
		
		testDifference(CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), node, Difference.VALUE);
		testDifference(node, CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.VALUE);
	}
	
	@Test
	public void testChildValuesInformational() throws Exception {		
		testNoDifferences(CompareNodeTests.createSampleNode(1, ChildType.VALUE, CreateInfo.CHILDS), 
				CompareNodeTests.createSampleNode(1, ChildType.VALUE, CreateInfo.CHILDS));
		
		Node node = CompareNodeTests.createSampleNode(false);
		Value value = CompareValueTests.createSampleValue(true);
		value.setValue("anOtherValue");
		node.getElement().add(value);
		node.getElement().add(CompareValueTests.createSampleValue(true));
		
		testDifference(CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS), node, Difference.VALUE);
		testDifference(node, CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS), Difference.VALUE);
	}
	@Test
	public void testUnequalChildrenCount() throws Exception {
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.NONE), 
				CompareNodeTests.createSampleNode(2, ChildType.ELEMENT, CreateInfo.NONE), Difference.CHILD_COUNT);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.VALUE, CreateInfo.NONE), 
				CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.NONE), 
				CompareNodeTests.createSampleNode(2, ChildType.NODE, CreateInfo.NONE), Difference.CHILD_COUNT);
		
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), 
				CompareNodeTests.createSampleNode(2, ChildType.ELEMENT, CreateInfo.PARENT), Difference.CHILD_COUNT);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT), 
				CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.PARENT), Difference.CHILD_COUNT);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.PARENT), 
				CompareNodeTests.createSampleNode(2, ChildType.NODE, CreateInfo.PARENT), Difference.CHILD_COUNT);
	}
	
	
	@Test
	public void testUneqalChildrenTypes() throws Exception {
		// just different types ELEMENT != VALUE --> Difference.CLASSES
		testDifference(CompareNodeTests.createSampleNode(2, ChildType.ELEMENT, CreateInfo.NONE), 
				CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.CLASSES);
		
		// different types (ELEMENT != VALUE) --> CLASSES, due to ignored informational state
		testDifference(CompareNodeTests.createSampleNode(2, ChildType.ELEMENT, CreateInfo.CHILDS), 
				CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.CLASSES);
		
		// different types (ELEMENT != VALUE) --> CLASSES, due to ignored informational state
		testDifference(CompareNodeTests.createSampleNode(2, ChildType.ELEMENT, CreateInfo.PARENT), 
				CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.PARENT), Difference.CLASSES);
	}	
	
	@Test
	public void testUnequalChildElement_Name() throws Exception {
		Node changedNode = CompareNodeTests.createSampleNode(2, ChildType.ELEMENT, CreateInfo.NONE);
		Element changedElement = CompareElementTests.createSampleElement(false);
		changedElement.setGeneratorName("anOtherName");		
		changedNode.getElement().add(changedElement);
		
		// generator name of child was changed --> Difference.NAME 
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.NONE), changedNode, Difference.NAME);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.NONE), Difference.NAME);
		testNoDifferences(changedNode, changedNode);
		
		// generator name of child was changed, but node is informational --> no difference
		changedNode.setInformational(true);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), changedNode, Difference.NAME);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), Difference.NAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildValue() throws Exception {
		Node changedNode = CompareNodeTests.createSampleNode(2, ChildType.VALUE, CreateInfo.NONE);
		Value changedChild = CompareValueTests.createSampleValue(false);
		changedChild.setValue("anOtherValue");		
		changedNode.getElement().add(changedChild);
		
		// value of child was changed --> Difference.VALUE 
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.VALUE, CreateInfo.NONE), changedNode, Difference.VALUE);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.VALUE, CreateInfo.NONE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT), changedNode, Difference.VALUE);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildReference() throws Exception {
		Node changedNode = CompareNodeTests.createSampleNode(2, ChildType.REFERENCE, CreateInfo.NONE);
		Reference changedChild = CompareReferenceTests.createSampleReference(false);
		changedChild.setTo("2.2.2");		
		changedNode.getElement().add(changedChild);
		
		// value of child was changed --> Difference.VALUE 
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.REFERENCE, CreateInfo.NONE), changedNode, Difference.REFERENCE);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.REFERENCE, CreateInfo.NONE), Difference.REFERENCE);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.REFERENCE, CreateInfo.PARENT), changedNode, Difference.REFERENCE);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.REFERENCE, CreateInfo.PARENT), Difference.REFERENCE);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_Fullname() throws Exception {
		Node changedNode = CompareNodeTests.createSampleNode(2, ChildType.NODE, CreateInfo.NONE);
		Node changedChild = CompareNodeTests.createSampleNode(false);
		changedChild.setFullName("anOtherFullName");
		changedNode.getElement().add(changedChild);
		
		// full name of child was changed --> Difference.FULLNAME
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.NONE), changedNode, Difference.FULLNAME);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.NONE), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.PARENT), changedNode, Difference.FULLNAME);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.PARENT), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_ChildCount() throws Exception {
		Node changedNode = CompareNodeTests.createSampleNode(2, ChildType.NODE, CreateInfo.NONE);
		Node changedChild = CompareNodeTests.createSampleNode(2, ChildType.NODE, CreateInfo.NONE);
		changedNode.getElement().add(changedChild);
		
		// count of child of a child differs --> Difference.CHILD_COUNT
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.NONE), changedNode, Difference.CHILD_COUNT);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testNoDifferences(changedNode, changedNode);
		
		changedNode.setInformational(true);
		testDifference(CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.PARENT), changedNode, Difference.CHILD_COUNT);
		testDifference(changedNode, CompareNodeTests.createSampleNode(3, ChildType.NODE, CreateInfo.PARENT), Difference.CHILD_COUNT);
		testNoDifferences(changedNode, changedNode);
	}
}
