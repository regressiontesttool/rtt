package rtt.core.tests.junit.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Reference;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareNodeTests {
	
	public enum ChildType {
		ELEMENT, VALUE, REFERENCE, NODE;		
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
			case REFERENCE:
				childElement = CompareReferenceTests.createSampleReference(childIsInfo);
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
	
	@Test
	public void testEqualNodes() throws Exception {
		testNoDifferences(createSampleNode(false), createSampleNode(false));
		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, CreateInfo.NONE), 
				createSampleNode(3, ChildType.ELEMENT, CreateInfo.NONE));
		testNoDifferences(createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.NONE));
		testNoDifferences(createSampleNode(4, ChildType.NODE, CreateInfo.NONE), 
				createSampleNode(4, ChildType.NODE, CreateInfo.NONE));
		
		testNoDifferences(createSampleNode(true), createSampleNode(true));
		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), 
				createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT));
		testNoDifferences(createSampleNode(2, ChildType.VALUE, CreateInfo.PARENT), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.PARENT));
		testNoDifferences(createSampleNode(4, ChildType.NODE, CreateInfo.PARENT), 
				createSampleNode(4, ChildType.NODE, CreateInfo.PARENT));
		
		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, CreateInfo.CHILDS), 
				createSampleNode(3, ChildType.ELEMENT, CreateInfo.CHILDS));
		testNoDifferences(createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.CHILDS));
		testNoDifferences(createSampleNode(4, ChildType.NODE, CreateInfo.CHILDS), 
				createSampleNode(4, ChildType.NODE, CreateInfo.CHILDS));
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

	
	@Test
	public void testUnequalChildrenCount() throws Exception {
		testDifference(createSampleNode(3, ChildType.ELEMENT, CreateInfo.NONE), 
				createSampleNode(2, ChildType.ELEMENT, CreateInfo.NONE), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ChildType.VALUE, CreateInfo.NONE), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testDifference(createSampleNode(3, ChildType.NODE, CreateInfo.NONE), 
				createSampleNode(2, ChildType.NODE, CreateInfo.NONE), Difference.CHILD_COUNT);
		
		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), 
				createSampleNode(2, ChildType.ELEMENT, CreateInfo.PARENT));
		testNoDifferences(createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.PARENT));
		testNoDifferences(createSampleNode(3, ChildType.NODE, CreateInfo.PARENT), 
				createSampleNode(2, ChildType.NODE, CreateInfo.PARENT));
	}
	
	
	@Test
	public void testUneqalChildrenTypes() throws Exception {
		// just different types ELEMENT != VALUE --> Difference.CLASSES
		testDifference(createSampleNode(2, ChildType.ELEMENT, CreateInfo.NONE), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.CLASSES);
		
		// different types (ELEMENT != VALUE) && different informational states --> CHILD_COUNT
		testDifference(createSampleNode(2, ChildType.ELEMENT, CreateInfo.CHILDS), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.NONE), Difference.CHILD_COUNT);
		
		// different types (ELEMENT != VALUE), but both informational --> no difference
		testNoDifferences(createSampleNode(2, ChildType.ELEMENT, CreateInfo.PARENT), 
				createSampleNode(2, ChildType.VALUE, CreateInfo.PARENT));
	}	
	
	@Test
	public void testUnequalChildElement_Name() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.ELEMENT, CreateInfo.NONE);
		Element changedElement = CompareElementTests.createSampleElement(false);
		changedElement.setGeneratorName("anOtherName");		
		changedNode.getElement().add(changedElement);
		
		// generator name of child was changed --> Difference.NAME 
		testDifference(createSampleNode(3, ChildType.ELEMENT, CreateInfo.NONE), changedNode, Difference.NAME);
		testDifference(changedNode, createSampleNode(3, ChildType.ELEMENT, CreateInfo.NONE), Difference.NAME);
		testNoDifferences(changedNode, changedNode);
		
		// generator name of child was changed, but node is informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ChildType.ELEMENT, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildValue() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.VALUE, CreateInfo.NONE);
		Value changedChild = CompareValueTests.createSampleValue(false);
		changedChild.setValue("anOtherValue");		
		changedNode.getElement().add(changedChild);
		
		// value of child was changed --> Difference.VALUE 
		testDifference(createSampleNode(3, ChildType.VALUE, CreateInfo.NONE), changedNode, Difference.VALUE);
		testDifference(changedNode, createSampleNode(3, ChildType.VALUE, CreateInfo.NONE), Difference.VALUE);
		testNoDifferences(changedNode, changedNode);
		
		// value of child was changed, but node is informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ChildType.VALUE, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildReference() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.REFERENCE, CreateInfo.NONE);
		Reference changedChild = CompareReferenceTests.createSampleReference(false);
		changedChild.setTo("2.2.2");		
		changedNode.getElement().add(changedChild);
		
		// value of child was changed --> Difference.VALUE 
		testDifference(createSampleNode(3, ChildType.REFERENCE, CreateInfo.NONE), changedNode, Difference.REFERENCE);
		testDifference(changedNode, createSampleNode(3, ChildType.REFERENCE, CreateInfo.NONE), Difference.REFERENCE);
		testNoDifferences(changedNode, changedNode);
		
		// value of child was changed, but node is informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ChildType.REFERENCE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ChildType.REFERENCE, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_Fullname() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.NODE, CreateInfo.NONE);
		Node changedChild = createSampleNode(false);
		changedChild.setFullName("anOtherFullName");
		changedNode.getElement().add(changedChild);
		
		// full name of child was changed --> Difference.FULLNAME
		testDifference(createSampleNode(3, ChildType.NODE, CreateInfo.NONE), changedNode, Difference.FULLNAME);
		testDifference(changedNode, createSampleNode(3, ChildType.NODE, CreateInfo.NONE), Difference.FULLNAME);
		testNoDifferences(changedNode, changedNode);
		
		// full name of child was changed, but node was informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ChildType.NODE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ChildType.NODE, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
	
	@Test
	public void testUnequalChildNode_ChildCount() throws Exception {
		Node changedNode = createSampleNode(2, ChildType.NODE, CreateInfo.NONE);
		Node changedChild = createSampleNode(2, ChildType.NODE, CreateInfo.NONE);
		changedNode.getElement().add(changedChild);
		
		// count of child of a child differs --> Difference.CHILD_COUNT
		testDifference(createSampleNode(3, ChildType.NODE, CreateInfo.NONE), changedNode, Difference.CHILD_COUNT);
		testDifference(changedNode, createSampleNode(3, ChildType.NODE, CreateInfo.NONE), Difference.CHILD_COUNT);
		testNoDifferences(changedNode, changedNode);
		
		// count of child of a child differs, but node was informational --> no difference
		changedNode.setInformational(true);
		testNoDifferences(createSampleNode(3, ChildType.NODE, CreateInfo.PARENT), changedNode);
		testNoDifferences(changedNode, createSampleNode(3, ChildType.NODE, CreateInfo.PARENT));
		testNoDifferences(changedNode, changedNode);
	}
}
