package rtt.core.tests.junit.compare;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.Type;
import rtt.core.archive.output.Reference;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;
import rtt.core.tests.junit.utils.CompareUtils;

public class CompareReferenceTests {

	private static final String NAME = "SampleElement";
	private static final Type TYPE = Type.OBJECT;
	private static final String REFERENCE = "1.1.1";
	
	private OutputCompare comparer;

	@Before
	public void setUp() throws Exception{
		comparer = new OutputCompare(false);
	}
	
	public static Reference createSampleReference(boolean informational) {
		return createReference(NAME, TYPE, REFERENCE, informational);
	}
	
	private static Reference createReference(String name, Type type, String toValue, boolean informational) {
		Reference element = new Reference();
		
		element.setName(name);
		element.setElementType(type);
		element.setInformational(informational);
		element.setTo(toValue);
		
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
		testThrowsException(null, createSampleReference(false), IllegalArgumentException.class);
		testThrowsException(createSampleReference(false), null, IllegalArgumentException.class);
		
		testThrowsException(null, null, IllegalArgumentException.class);
		testThrowsException(null, createSampleReference(true), IllegalArgumentException.class);
		testThrowsException(createSampleReference(true), null, IllegalArgumentException.class);
	}
	
	@Test
	public void testEqualReferences() throws Exception {
		testNoDifferences(createSampleReference(false), createSampleReference(false));
		testNoDifferences(createSampleReference(true), createSampleReference(true));
	}
	
	@Test
	public void testUnequalReferenceAttribute() throws Exception {
		Reference changedReference = createReference(NAME, TYPE, "2.2.2", false);
		testDifference(createSampleReference(false), changedReference, Difference.REFERENCE);
		testDifference(changedReference, createSampleReference(false), Difference.REFERENCE);
		testNoDifferences(changedReference, changedReference);
		
		changedReference.setInformational(true);
		testNoDifferences(createSampleReference(true), changedReference);
		testNoDifferences(changedReference, createSampleReference(true));
		testNoDifferences(changedReference, changedReference);
	}
	
	@Test
	public void testNullReferenceAttribute() throws Exception {
		Reference changedReference = createReference(NAME, TYPE, null, false);
		testDifference(createSampleReference(false), changedReference, Difference.REFERENCE);
		testDifference(changedReference, createSampleReference(false), Difference.REFERENCE);
		testNoDifferences(changedReference, changedReference);
		
		changedReference.setInformational(true);
		testNoDifferences(createSampleReference(true), changedReference);
		testNoDifferences(changedReference, createSampleReference(true));
		testNoDifferences(changedReference, changedReference);
	}

}
