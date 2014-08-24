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
	private static final Type TYPE = Type.OBJECT;
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new OutputCompare(false);
	}
	
	public static Element createSampleElement(boolean informational) {
		return createElement(NAME, TYPE, informational);
	}
	
	public static Element createElement(String name, Type value, boolean informational) {
		Element element = new Element() {};
		
		element.setGeneratorName(name);
		element.setGeneratorType(value);
		element.setInformational(informational);
		
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
		testThrowsException(null, createSampleElement(false), IllegalArgumentException.class);
		testThrowsException(createSampleElement(false), null, IllegalArgumentException.class);
		
		testThrowsException(null, null, IllegalArgumentException.class);
		testThrowsException(null, createSampleElement(true), IllegalArgumentException.class);
		testThrowsException(createSampleElement(true), null, IllegalArgumentException.class);
	}
	
	@Test
	public void testEqualElements() throws Exception {
		testNoDifferences(createSampleElement(false), createSampleElement(false));
		testNoDifferences(createSampleElement(true), createSampleElement(true));
	}
	
	@Test
	public void testUnequalNameAttribute() throws Exception {
		Element changedElement = createElement("otherName", TYPE, false);
		
		testDifference(changedElement, createSampleElement(false), Difference.NAME);
		testDifference(createSampleElement(false), changedElement, Difference.NAME);
		testNoDifferences(changedElement, changedElement);
		
		changedElement.setInformational(true);
		testDifference(changedElement, createSampleElement(true), Difference.NAME);
		testDifference(createSampleElement(true), changedElement, Difference.NAME);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testUnequalTypeAttribute() throws Exception {
		Element changedElement = createElement(NAME, Type.METHOD, false);
		
		testDifference(changedElement, createSampleElement(false), Difference.TYPE);
		testDifference(createSampleElement(false), changedElement, Difference.TYPE);
		testNoDifferences(changedElement, changedElement);
		
		changedElement.setInformational(true);
		testDifference(changedElement, createSampleElement(true), Difference.TYPE);
		testDifference(createSampleElement(true), changedElement, Difference.TYPE);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testUnequalInformationalAttribute() throws Exception {
		Element changedElement = createSampleElement(true);
		
		testDifference(changedElement, createSampleElement(false), Difference.INFORMATIONAL);
		testDifference(createSampleElement(false), changedElement, Difference.INFORMATIONAL);
		testNoDifferences(changedElement, changedElement);
		
		changedElement.setInformational(false);
		testDifference(changedElement, createSampleElement(true), Difference.INFORMATIONAL);
		testDifference(createSampleElement(true), changedElement, Difference.INFORMATIONAL);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testValueElementCompare() throws Exception {
		Value value = new Value();
		value.setGeneratorName(NAME);
		value.setGeneratorType(TYPE);
		value.setValue("aValue");
		
		testDifference(createSampleElement(false), value, Difference.CLASSES);
		testDifference(value, createSampleElement(false), Difference.CLASSES);		
		
		value.setInformational(true);
		testDifference(createSampleElement(true), value, Difference.CLASSES);
		testDifference(value, createSampleElement(true), Difference.CLASSES);
	}
	
	@Test
	public void testNodeElementCompare() throws Exception {
		Node node = new Node();
		node.setGeneratorName(NAME);
		node.setGeneratorType(TYPE);
		node.setFullName("aFullName");
		node.setSimpleName("aSimpleName");		
		node.getElement().add(createSampleElement(false));
		
		testDifference(createSampleElement(false), node, Difference.CLASSES);
		testDifference(node, createSampleElement(false), Difference.CLASSES);
		
		node.setInformational(true);
		testDifference(createSampleElement(true), node, Difference.CLASSES);
		testDifference(node, createSampleElement(true), Difference.CLASSES);
	}

}
