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

public class CompareElementTests {
	
	private static final String NAME = "SampleElement";
	private static final Type TYPE = Type.ELEMENT;
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new OutputCompare(true);
	}
	
	public static Element createSampleElement() {
		return createElement(NAME, TYPE);
	}
	
	public static Element createElement(String name, Type value) {
		Element element = new Element() {};
		
		element.setName(name);
		element.setType(value);
		
		return element;		
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
	public void testExceptions() throws Exception {
		testThrowsException(null, null, IllegalArgumentException.class);
		testThrowsException(null, createSampleElement(), IllegalArgumentException.class);
		testThrowsException(createSampleElement(), null, IllegalArgumentException.class);
	}
	
	@Test
	public void testEqualElements() throws Exception {
		testNoDifferences(createSampleElement(), createSampleElement());
	}
	
	@Test
	public void testUnequalNameAttribute() throws Exception {
		Element changedElement = createElement("otherName", TYPE);
		
		testDifference(changedElement, createSampleElement(), Difference.NAME);
		testDifference(createSampleElement(), changedElement, Difference.NAME);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testUnequalTypeAttribute() throws Exception {
		Element changedElement = createElement(NAME, Type.METHOD);
		
		testDifference(changedElement, createSampleElement(), Difference.TYPE);
		testDifference(createSampleElement(), changedElement, Difference.TYPE);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testUnequalInformationalAttribute() throws Exception {
		Element changedElement = createSampleElement();
		changedElement.setInformational(true);
		
		testDifference(changedElement, createSampleElement(), Difference.INFORMATIONAL);
		testDifference(createSampleElement(), changedElement, Difference.INFORMATIONAL);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testValueElementCompare() throws Exception {
		Value value = new Value();
		value.setName(NAME);
		value.setType(TYPE);
		value.setValue("aValue");
		
		testDifference(createSampleElement(), value, Difference.CLASSES);
		testDifference(value, createSampleElement(), Difference.CLASSES);		
	}
	
	@Test
	public void testNodeElementCompare() throws Exception {
		Node node = new Node();
		node.setName(NAME);
		node.setType(TYPE);
		node.setFullName("aFullName");
		node.setSimpleName("aSimpleName");		
		node.getElement().add(createSampleElement());
		
		testDifference(createSampleElement(), node, Difference.CLASSES);
		testDifference(node, createSampleElement(), Difference.CLASSES);
	}

}
