package rtt.core.tests.junit;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.core.archive.output.GeneratorType;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.testing.compare.OutputCompare;
import rtt.core.testing.compare.results.TestFailure;

public class CompareOutputTests {

	private static final String NO_DIFFS_FOUND = "Compare found no differences where there should be some.";
	private static final String DIFFS_FOUND = "Compare found differences where none should be.";

	@Before
	public void setUp() throws Exception {
		
	}
	
	private void checkThrowsException(Output reference, Output actual, boolean checkInfos) {
		try {
			OutputCompare.compareOutput(reference, actual, checkInfos);
			fail();
		} catch (Exception e) {
			// do nothing
		}
	}
	
	private boolean hasFailures(Output refOutput, Output actualOutput, 
			boolean testInfos) {
		
		List<TestFailure> failures = OutputCompare.compareOutput(
				refOutput, actualOutput, testInfos);
		
		return failures.size() > 0;
	}
	
	private Output createOutput(int nodeCount) {
		Output output = new Output();
		
		for (int i = 0; i < nodeCount; i++) {
			Node node = new Node();
			node.setGeneratorName("Item " + i);
			node.setGeneratorType(GeneratorType.METHOD);
			
			output.getNodes().add(node);
		}
		
		return output;
	}
	
	@Test
	public void testNullOutputs() throws Exception {
		checkThrowsException(null, null, true);
		checkThrowsException(null, null, false);
		
		checkThrowsException(new Output(), null, true);
		checkThrowsException(new Output(), null, false);
		
		checkThrowsException(null, new Output(), true);
		checkThrowsException(null, new Output(), false);
	}	

	@Test
	public void testBothEmptyOutputs() throws Exception {
		assertFalse(DIFFS_FOUND, hasFailures(new Output(), new Output(), true));
		assertFalse(DIFFS_FOUND, hasFailures(new Output(), new Output(), false));
	}	
	
	@Test
	public void testOneEmptyOutputs() throws Exception {
		Output emptyOutput = new Output();
		Output nonEmptyOutput = createOutput(2);
		
		assertTrue(NO_DIFFS_FOUND, hasFailures(emptyOutput, nonEmptyOutput, true));
		assertTrue(NO_DIFFS_FOUND, hasFailures(emptyOutput, nonEmptyOutput, false));
		
		assertTrue(NO_DIFFS_FOUND, hasFailures(nonEmptyOutput, emptyOutput, true));
		assertTrue(NO_DIFFS_FOUND, hasFailures(nonEmptyOutput, emptyOutput, false));
	}
	
	@Test
	public void testEqualOutputs() throws Exception {
		Output refOutput = createOutput(2);
		Output actualOutput = createOutput(2);
		
		assertFalse(DIFFS_FOUND, hasFailures(refOutput, actualOutput, true));
		assertFalse(DIFFS_FOUND, hasFailures(refOutput, actualOutput, false));
	}

}
