package rtt.core.testing.compare;

import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.ElementType;
import rtt.core.archive.output.Output;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.results.TestFailure;


public class CompareOutputTest {

	@Before
	public void setUp() throws Exception {}
	
	private void testThrowsException(Output reference, Output actual, 
			boolean checkInfos, Class<? extends Throwable> expectedException) {
		
		try {
			OutputCompare.compareOutput(reference, actual, checkInfos);
			fail("Expected exception '" + expectedException.getSimpleName() + "' was not thrown.");
		} catch (Exception e) {
			if (!expectedException.isInstance(e)) {
				fail("An other exception '" + e.getClass().getSimpleName() 
						+ "' instead of expected '" + expectedException.getSimpleName() + "' was thrown.");
			}
		}
	}
	
	private void testNoDifferences(Output reference, Output actual, boolean testInfos) {
		List<TestFailure> failures = OutputCompare.compareOutput(reference, actual, testInfos);
		
		if (failures != null && failures.size() > 0) {
			StringBuilder failMessage = new StringBuilder("Differences found, but there should not: ");
			Iterator<TestFailure> iterator = failures.iterator();
			while(iterator.hasNext()) {
				failMessage.append(iterator.next().getMessage());
				failMessage.append(iterator.hasNext() ? "," : ".");
			}
			
			fail(failMessage.toString());
		}
	}
	
	private void testDifference(Output reference, Output actual, boolean testInfos) {
		List<TestFailure> failures = OutputCompare.compareOutput(reference, actual, testInfos);
		if (failures == null || failures.size() <= 0) {
			fail("There should be failures, but was not.");
		}
	}
	
	private Output createOutput(ElementType initType, boolean informational) {		
		Element initialElement = CompareElementTest.
				createSampleElement(initType, informational);
		
		Output output = new Output();		
		output.setInitialElement(initialElement);
		return output;
	}
	
	private Output createOutput(boolean initInfo, int childCount, boolean childInfos) {
		Output output = createOutput(ElementType.NODE, initInfo);
		
		Element initialNode = output.getInitialElement();
		Element element = null;
		for (int i = 0; i < childCount; i++) {
			element = CompareElementTest.createSampleElement(
					ElementType.VALUE, childInfos);
			element.setName("Item " + i);
			
			initialNode.getElements().add(element);
		}
		
		return output;
	}
	
	@Test
	public void testNullOutputs() throws Exception {
		testThrowsException(null, null, true, RuntimeException.class);
		testThrowsException(null, null, false, RuntimeException.class);
		
		testThrowsException(new Output(), null, true, RuntimeException.class);
		testThrowsException(new Output(), null, false, RuntimeException.class);
		
		testThrowsException(null, new Output(), true, RuntimeException.class);
		testThrowsException(null, new Output(), false, RuntimeException.class);
	}	

	@Test
	public void testBothEmptyOutputs() throws Exception {
		testNoDifferences(new Output(), new Output(), true);
		testNoDifferences(new Output(), new Output(), false);
	}	
	
	@Test
	public void testOneEmptyOutputs() throws Exception {
		Output emptyOutput = new Output();
		Output nonEmptyOutput = new Output();
		
		nonEmptyOutput.setInitialElement(
				CompareElementTest.createSampleElement(ElementType.VALUE, false));
		
		testDifference(emptyOutput, nonEmptyOutput, true);
		testDifference(emptyOutput, nonEmptyOutput, false);
		
		testDifference(nonEmptyOutput, emptyOutput, true);
		testDifference(nonEmptyOutput, emptyOutput, false);
		
		nonEmptyOutput.setInitialElement(
				CompareElementTest.createSampleElement(ElementType.VALUE, true));
		
		testDifference(emptyOutput, nonEmptyOutput, true);
		testDifference(emptyOutput, nonEmptyOutput, false);
		
		testDifference(nonEmptyOutput, emptyOutput, true);
		testDifference(nonEmptyOutput, emptyOutput, false);
	}
	
	@Test
	public void testEqualOutputs_InitValue() throws Exception {
		Output refOutput = createOutput(ElementType.VALUE, false);
		Output actualOutput = createOutput(ElementType.VALUE, false);
		
		testNoDifferences(refOutput, actualOutput, true);
		testNoDifferences(refOutput, actualOutput, false);
		
		testNoDifferences(actualOutput, refOutput, true);
		testNoDifferences(actualOutput, refOutput, false);
	}
	
	@Test
	public void testEqualOutputs_InitReference() throws Exception {
		Output refOutput = createOutput(ElementType.REFERENCE, false);
		Output actualOutput = createOutput(ElementType.REFERENCE, false);
		
		testNoDifferences(refOutput, actualOutput, true);
		testNoDifferences(refOutput, actualOutput, false);
		
		testNoDifferences(actualOutput, refOutput, true);
		testNoDifferences(actualOutput, refOutput, false);
	}
	
	@Test
	public void testEqualOutputs_InitNode() throws Exception {
		Output refOutput = createOutput(ElementType.NODE, false);
		Output actualOutput = createOutput(ElementType.NODE, false);
		
		testNoDifferences(refOutput, actualOutput, true);
		testNoDifferences(refOutput, actualOutput, false);
		
		testNoDifferences(actualOutput, refOutput, true);
		testNoDifferences(actualOutput, refOutput, false);
	}
	
	@Test
	public void testEqualChildCount() throws Exception {
		Output refOutput = createOutput(false, 3, false);
		Output actualOutput = createOutput(false, 3, false);
		
		testNoDifferences(refOutput, actualOutput, true);
		testNoDifferences(refOutput, actualOutput, false);
		
		testNoDifferences(actualOutput, refOutput, true);
		testNoDifferences(actualOutput, refOutput, false);
	}
	
	@Test
	public void testUnequalChildCount() throws Exception {
		Output refOutput = createOutput(false, 2, false);
		Output actualOutput = createOutput(false, 3, false);
		
		testDifference(refOutput, actualOutput, true);
		testDifference(refOutput, actualOutput, false);
		
		testDifference(actualOutput, refOutput, true);
		testDifference(actualOutput, refOutput, false);
	}

}
