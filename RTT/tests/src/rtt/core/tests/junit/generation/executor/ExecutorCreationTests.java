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
	
	// Test: super class has parser annotation
	
	@Parser static class ParserSuperClass {}	
	static class ExtendingParserClass extends ParserSuperClass {}
	
	@Test
	public void testExtending() throws Throwable {
		new Executor(ExtendingParserClass.class);
	}
	
	// Test: two level super class has parser annotation
	static class SecondSuperClass extends ParserSuperClass {}
	static class ConcreteClass extends SecondSuperClass {}
	
	@Test
	public void testTwoLevelSuperClass() throws Exception {
		new Executor(ConcreteClass.class);
	}
	
	// Test: interface has parser annotation
	
	@Parser interface ParserInterface {}
	static class ImplementingParserClass implements ParserInterface {}
	
	@Test
	public void testImplementing() throws Exception {
		new Executor(ImplementingParserClass.class);
	}
}
