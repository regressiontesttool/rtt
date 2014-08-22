package rtt.core.tests.junit.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
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
