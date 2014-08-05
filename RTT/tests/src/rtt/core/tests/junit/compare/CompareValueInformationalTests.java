package rtt.core.tests.junit.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareValueInformationalTests {

	private static final String NAME = "SampleElement";
	private static final Type TYPE = Type.ELEMENT;
	private static final String VALUE = "aValue";
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new OutputCompare(true);
	}
	
	public static Value createSampleValue(boolean informational) {
		return createValue(NAME, TYPE, VALUE, informational);
	}
	
	private static Value createValue(String name, Type type, String value, boolean informational) {
		Value element = new Value();
		
		element.setName(name);
		element.setType(type);
		element.setInformational(informational);
		element.setValue(value);		
		
		return element;		
	}
	
	private void testThrowsException(Element referenceElement, Element actualElement, Class<? extends Throwable> expectedException) {
		CompareUtils.testThrowsException(comparer, referenceElement, actualElement, expectedException);
	}
	
	private void testNoDifferences(Element referenceElement, Element actualElement) {
		CompareUtils.testNoDifferences(comparer, referenceElement, actualElement);
	}
	
	private void testDifference(Element referenceElement, Element actualElement, Difference difference) {
		CompareUtils.testDifference(comparer, referenceElement, actualElement, difference);
	}
	
	@Test
	public void testExceptions() throws Exception {
		testThrowsException(null, null, IllegalArgumentException.class);
		testThrowsException(null, createSampleValue(false), IllegalArgumentException.class);
		testThrowsException(createSampleValue(false), null, IllegalArgumentException.class);
		
		testThrowsException(null, null, IllegalArgumentException.class);
		testThrowsException(null, createSampleValue(true), IllegalArgumentException.class);
		testThrowsException(createSampleValue(true), null, IllegalArgumentException.class);
	}
	
	@Test
	public void testEqualValues() throws Exception {
		testNoDifferences(createSampleValue(false), createSampleValue(false));
		testNoDifferences(createSampleValue(true), createSampleValue(true));
	}
	
	@Test
	public void testUnequalValueAttribute() throws Exception {
		Value changedValue = createValue(NAME, TYPE, "anOtherValue", false);
		testDifference(createSampleValue(false), changedValue, Difference.VALUE);
		testDifference(changedValue, createSampleValue(false), Difference.VALUE);
		testNoDifferences(changedValue, changedValue);
		
		changedValue.setInformational(true);
		testDifference(createSampleValue(true), changedValue, Difference.VALUE);
		testDifference(changedValue, createSampleValue(true), Difference.VALUE);
		testNoDifferences(changedValue, changedValue);
	}
	
	@Test
	public void testNullValueAttribute() throws Exception {
		Value changedValue = createValue(NAME, TYPE, null, false);
		testDifference(createSampleValue(false), changedValue, Difference.VALUE);
		testDifference(changedValue, createSampleValue(false), Difference.VALUE);
		testNoDifferences(changedValue, changedValue);
		
		changedValue.setInformational(true);
		testDifference(createSampleValue(true), changedValue, Difference.VALUE);
		testDifference(changedValue, createSampleValue(true), Difference.VALUE);
		testNoDifferences(changedValue, changedValue);
	}

}
