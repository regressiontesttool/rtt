package rtt.core.tests.junit.generation.executor;

import java.io.InputStream;

import org.junit.Test;

import rtt.annotations.Parser;
import rtt.core.testing.generation.Executor;

public class ExecutorCreationTests {
	
	// Test: anonymous classes
	
	@Test(expected=IllegalArgumentException.class)
	public void testAnonymousClass() throws Exception {
		new Executor(new Object() {}.getClass());
	}

	// Test: local classes

	@Test(expected=IllegalArgumentException.class)
	public void testLocalClass() throws Exception {
		@Parser class LocalClass {
			@Parser.Initialize public LocalClass(InputStream in) {}
		}

		new Executor(LocalClass.class);
	}

	// Test: Non-static member class

	@Parser class NonStaticMemberClass {
		@Parser.Initialize public NonStaticMemberClass(InputStream in) {}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMemberClass() throws Exception {
		new Executor(NonStaticMemberClass.class);
	}

	// Test: no parser annotation

	static class NoParserAnnotation {			
		public NoParserAnnotation() {}			
	}

	@Test(expected=RuntimeException.class)
	public void testNoParserAnnotation() throws Exception {		
		new Executor(NoParserAnnotation.class);
	}
	
	// Test: correct parser annotation
	
	@Parser static class CorrectParserAnnotation {
		public CorrectParserAnnotation() {}
	}
	
	@Test
	public void testCorrectParserAnnotation() throws Exception {
		new Executor(CorrectParserAnnotation.class);
	}
}
