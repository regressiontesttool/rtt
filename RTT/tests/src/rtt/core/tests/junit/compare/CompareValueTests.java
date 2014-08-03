package rtt.core.tests.junit.compare;

import static org.junit.Assert.*;

import javax.xml.bind.util.ValidationEventCollector;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Value;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareValueTests {

	private static final String NAME = "SampleElement";
	private static final Type TYPE = Type.ELEMENT;
	private static final String VALUE = "aValue";
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new OutputCompare(true);
	}
	
	public static Value createSampleValue() {
		return createValue(NAME, TYPE, VALUE);
	}
	
	private static Value createValue(String name, Type type, String value) {
		Value element = new Value();
		
		element.setName(name);
		element.setType(type);
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
		testThrowsException(null, createSampleValue(), IllegalArgumentException.class);
		testThrowsException(createSampleValue(), null, IllegalArgumentException.class);
	}
	
	@Test
	public void testEqualValues() throws Exception {
		testNoDifferences(createSampleValue(), createSampleValue());
	}
	
	@Test
	public void testUnequalValueAttribute() throws Exception {
		Value changedValue = createValue(NAME, TYPE, "anOtherValue");
		testDifference(createSampleValue(), changedValue, Difference.VALUE);
		testDifference(changedValue, createSampleValue(), Difference.VALUE);
		testNoDifferences(changedValue, changedValue);
	}
	
	@Test
	public void testNullValueAttribute() throws Exception {
		Value changedValue = createValue(NAME, TYPE, null);
		testDifference(createSampleValue(), changedValue, Difference.VALUE);
		testDifference(changedValue, createSampleValue(), Difference.VALUE);
		testNoDifferences(changedValue, changedValue);
	}

}
