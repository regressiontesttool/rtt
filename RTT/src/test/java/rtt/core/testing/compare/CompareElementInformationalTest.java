package rtt.core.tests.junit.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.ElementType;
import rtt.core.archive.output.GeneratorType;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareElementInformationalTest {
	
	private static final String NAME = "SampleElement";
	private static final GeneratorType GENERATOR = GeneratorType.OBJECT;
	private static final ElementType TYPE = ElementType.VALUE;
	private static final String VALUE = "aValue";
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new OutputCompare(true);
	}
	
	public static Element createSampleElement(ElementType type, boolean informational) {
		return createElement(NAME, type, GENERATOR, VALUE, informational);
	}
	
	public static Element createElement(String name, ElementType type,
			GeneratorType generatedBy, String value, boolean informational) {
		
		Element element = new Element() {};
		
		element.setName(name);
		element.setElementType(type);
		element.setGeneratedBy(generatedBy);
		element.setValue(value);
		element.setInformational(informational);
		
		return element;		
	}
	
	private Element createSampleElement(boolean informational) {
		return createSampleElement(TYPE, informational);
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
		Element changedElement = createElement("otherName", TYPE, GENERATOR, VALUE, false);
		
		testDifference(changedElement, createSampleElement(false), Difference.NAME);
		testDifference(createSampleElement(false), changedElement, Difference.NAME);
		testNoDifferences(changedElement, changedElement);
		
		changedElement.setInformational(true);
		testDifference(changedElement, createSampleElement(true), Difference.NAME);
		testDifference(createSampleElement(true), changedElement, Difference.NAME);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testUnequalElementTypeAttribute() throws Exception {
		Element changedElement = createElement(NAME, ElementType.REFERENCE, GENERATOR, VALUE, false);
		
		testDifference(changedElement, createSampleElement(false), Difference.ELEMENT_TYPE);
		testDifference(createSampleElement(false), changedElement, Difference.ELEMENT_TYPE);
		testNoDifferences(changedElement, changedElement);
		
		changedElement.setInformational(true);
		testDifference(changedElement, createSampleElement(true), Difference.ELEMENT_TYPE);
		testDifference(createSampleElement(true), changedElement, Difference.ELEMENT_TYPE);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testUnequalGeneratorTypeAttribute() throws Exception {
		Element changedElement = createElement(NAME, TYPE, GeneratorType.METHOD, VALUE, false);
		
		testDifference(changedElement, createSampleElement(false), Difference.GENERATOR_TYPE);
		testDifference(createSampleElement(false), changedElement, Difference.GENERATOR_TYPE);
		testNoDifferences(changedElement, changedElement);
		
		changedElement.setInformational(true);
		testDifference(changedElement, createSampleElement(true), Difference.GENERATOR_TYPE);
		testDifference(createSampleElement(true), changedElement, Difference.GENERATOR_TYPE);
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
	public void testNullValueAttribute() throws Exception {
		Element changedElement = createElement(NAME, TYPE, GENERATOR, null, false);
		
		testDifference(changedElement, createSampleElement(false), Difference.VALUE);
		testDifference(createSampleElement(false), changedElement, Difference.VALUE);
		testNoDifferences(changedElement, changedElement);
		
		changedElement.setInformational(true);
		testDifference(changedElement, createSampleElement(true), Difference.VALUE);
		testDifference(createSampleElement(true), changedElement, Difference.VALUE);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testUnequalValueAttribute() throws Exception {
		Element changedElement = createElement(NAME, TYPE, GENERATOR, "anOtherValue", false);
		
		testDifference(changedElement, createSampleElement(false), Difference.VALUE);
		testDifference(createSampleElement(false), changedElement, Difference.VALUE);
		testNoDifferences(changedElement, changedElement);
		
		changedElement.setInformational(true);
		testDifference(changedElement, createSampleElement(true), Difference.VALUE);
		testDifference(createSampleElement(true), changedElement, Difference.VALUE);
		testNoDifferences(changedElement, changedElement);
	}
	
	@Test
	public void testUnequalChildCount() throws Exception {
		Element node = createElement(NAME, TYPE, GENERATOR, VALUE, false);
		node.getElements().add(createSampleElement(false));
		
		testDifference(createSampleElement(false), node, Difference.CHILD_COUNT);
		testDifference(node, createSampleElement(false), Difference.CHILD_COUNT);
		
		node.setInformational(true);
		testDifference(createSampleElement(true), node, Difference.CHILD_COUNT);
		testDifference(node, createSampleElement(true), Difference.CHILD_COUNT);
	}
}
