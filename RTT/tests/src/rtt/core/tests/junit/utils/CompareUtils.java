package rtt.core.tests.junit.utils;

import static org.junit.Assert.fail;
import rtt.core.archive.output.Element;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.OutputCompare.CompareResult;
import rtt.core.testing.compare.OutputCompare.CompareResult.Difference;

public class CompareUtils {
	
	public static void testThrowsException(OutputCompare comparer,
			Element referenceElement, Element actualElement, 
			Class<? extends Throwable> expectedException) {
		
		try {
			comparer.compareElements(referenceElement, actualElement);
			fail("Expected exception '" + expectedException.getSimpleName() + "' was not thrown.");
		} catch (Exception e) {
			if (!expectedException.isInstance(e)) {
				fail("An other exception '" + e.getClass().getSimpleName() 
						+ "' instead of expected '" + expectedException.getSimpleName() + "' was thrown.");
			}
		}
	}
	
	public static void testNoDifferences(OutputCompare comparer,			
			Element referenceElement, Element actualElement) {
		CompareResult result = comparer.compareElements(referenceElement, actualElement);
		if (result != null && result.hasDifferences()) {
			fail("Differences found '" + result.getDifference().name() + "' , but there should not: " + result.getMessage());
		}
	}
	
	public static void testDifference(OutputCompare comparer,
			Element referenceElement, Element actualElement, Difference expected) {
		CompareResult result = comparer.compareElements(referenceElement, actualElement);
		if (result == null || !result.hasDifferences()) {
			fail("Compare found no differences, but expected was '" + expected.name() + "'.");
		}
		
		if (!result.getDifference().equals(expected)) {
			fail("Difference expected '" + expected.name() + "', but was '" + result.getDifference().name() + "'.");
		}
	}

}
