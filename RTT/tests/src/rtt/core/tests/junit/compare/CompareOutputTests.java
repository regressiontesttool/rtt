package rtt.core.tests.junit.compare;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.Element;
import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.Type;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.results.TestFailure;


public class CompareOutputTests {

	@Before
	public void setUp() throws Exception {
		
	}
	
	private enum InitElement {
		ELEMENT, VALUE, REFERENCE, NODE;
	}
	
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
	
	private Output createOutput(InitElement initType, boolean initInfo) {		
		Element initialElement = null;
		switch (initType) {
		case ELEMENT:
			initialElement = CompareElementTests.createSampleElement(initInfo);
			break;
		case NODE:
			initialElement = CompareNodeTests.createSampleNode(initInfo);
			break;
		case REFERENCE:
			initialElement = CompareReferenceTests.createSampleReference(initInfo);
			break;
		case VALUE:
			initialElement = CompareValueTests.createSampleValue(initInfo);
			break;		
		}
		
		Output output = new Output();		
		output.setInitialElement(initialElement);
		return output;
	}
	
	private Output createOutput(boolean initInfo, int childCount, boolean childInfos) {
		Output output = createOutput(InitElement.NODE, initInfo);
		
		Element initialNode = output.getInitialElement();
		for (int i = 0; i < childCount; i++) {
			Element element = CompareElementTests.createElement(
					"Item " + i, GeneratorType.OBJECT, childInfos);
			
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
				CompareElementTests.createSampleElement(false));
		
		testDifference(emptyOutput, nonEmptyOutput, true);
		testDifference(emptyOutput, nonEmptyOutput, false);
		
		testDifference(nonEmptyOutput, emptyOutput, true);
		testDifference(nonEmptyOutput, emptyOutput, false);
		
		nonEmptyOutput.setInitialElement(
				CompareElementTests.createSampleElement(true));
		
		testDifference(emptyOutput, nonEmptyOutput, true);
		testDifference(emptyOutput, nonEmptyOutput, false);
		
		testDifference(nonEmptyOutput, emptyOutput, true);
		testDifference(nonEmptyOutput, emptyOutput, false);
	}
	
	@Test
	public void testEqualOutputs_InitElement() throws Exception {
		Output refOutput = createOutput(InitElement.ELEMENT, false);
		Output actualOutput = createOutput(InitElement.ELEMENT, false);
		
		testNoDifferences(refOutput, actualOutput, true);
		testNoDifferences(refOutput, actualOutput, false);
		
		testNoDifferences(actualOutput, refOutput, true);
		testNoDifferences(actualOutput, refOutput, false);
	}
	
	@Test
	public void testEqualOutputs_InitValues() throws Exception {
		Output refOutput = createOutput(InitElement.VALUE, false);
		Output actualOutput = createOutput(InitElement.VALUE, false);
		
		testNoDifferences(refOutput, actualOutput, true);
		testNoDifferences(refOutput, actualOutput, false);
		
		testNoDifferences(actualOutput, refOutput, true);
		testNoDifferences(actualOutput, refOutput, false);
	}
	
	@Test
	public void testEqualOutputs_InitReference() throws Exception {
		Output refOutput = createOutput(InitElement.REFERENCE, false);
		Output actualOutput = createOutput(InitElement.REFERENCE, false);
		
		testNoDifferences(refOutput, actualOutput, true);
		testNoDifferences(refOutput, actualOutput, false);
		
		testNoDifferences(actualOutput, refOutput, true);
		testNoDifferences(actualOutput, refOutput, false);
	}
	
	@Test
	public void testEqualOutputs_InitNode() throws Exception {
		Output refOutput = createOutput(InitElement.NODE, false);
		Output actualOutput = createOutput(InitElement.NODE, false);
		
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
