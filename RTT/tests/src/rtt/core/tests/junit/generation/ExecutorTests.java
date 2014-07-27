package rtt.core.tests.junit.generation;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Parser;
import rtt.core.archive.input.Input;
import rtt.core.testing.generation.Executor;

public class ExecutorTests {	

	private Input input;
	private List<String> params;

	@Before
	public void setUp() {
		input = new Input();
		input.setValue("This is a testing input");

		params = new ArrayList<>();
	}

	private void createAndInitializeExecutor(Class<?> class1) throws Exception {
		Executor executor = new Executor(class1);
		executor.initialize(input, params);		
	}

	// Test: anonymous classes

	@Test(expected=IllegalArgumentException.class)
	public void testAnonymousClass() throws Exception {
		createAndInitializeExecutor(new Object() {}.getClass());
	}

	// Test: local classes

	@Test(expected=IllegalArgumentException.class)
	public void testLocalClass() throws Exception {
		@Parser class LocalClass {
			@Parser.Initialize public LocalClass(InputStream in) {}
		}

		createAndInitializeExecutor(LocalClass.class);
	}

	// Test: Non-static member class

	@Parser class NonStaticMemberClass {
		@Parser.Initialize public NonStaticMemberClass(InputStream in) {}
	}

	@Test(expected=IllegalArgumentException.class)
	public void testMemberClass() throws Exception {
		createAndInitializeExecutor(NonStaticMemberClass.class);
	}

	// Test: no parser annotation

	static class NoParserAnnotation {			
		public NoParserAnnotation() {}			
	}

	@Test(expected=RuntimeException.class)
	public void testNoParserAnnotation() throws Exception {		
		createAndInitializeExecutor(NoParserAnnotation.class);
	}	

	// Test: No initialize annotation

	@Parser static class NoInitAnnotation {
		public NoInitAnnotation() {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoInitAnnotationAnnotation() throws Exception {		
		createAndInitializeExecutor(NoInitAnnotation.class);
	}

	// Test: annotated constructor, but no parameter

	@Parser static class NoParameterConstructor {
		@Parser.Initialize
		public NoParameterConstructor() {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoParameterConstructor() throws Exception {		
		createAndInitializeExecutor(NoParameterConstructor.class);
	}

	// Test: annotated method, but no parameter

	@Parser static class NoParameterMethod {
		@Parser.Initialize
		public void initMethod() {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoParameterMethod() throws Exception {		
		createAndInitializeExecutor(NoParameterMethod.class);
	}

	// Test: annotated constructor, but wrong parameter

	@Parser static class WrongParameterConstructor {
		@Parser.Initialize
		public WrongParameterConstructor(Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWrongParameterConstructor() throws Exception {		
		createAndInitializeExecutor(WrongParameterConstructor.class);
	}

	// Test: annotated method, but wrong parameter

	@Parser static class WrongParameterMethod {
		@Parser.Initialize
		public void initMethod(Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWrongParameterMethod() throws Exception {		
		createAndInitializeExecutor(WrongParameterMethod.class);
	}

	// Test: annotated method, but no parameter-less constructor

	@Parser static class ParameterlessConstructor {
		public ParameterlessConstructor(Object o) {}

		@Parser.Initialize
		public void initMethod(InputStream in) {}
	}

	@Test(expected=InstantiationException.class)
	public void testNoParameterlessConstructor() throws Exception {		
		createAndInitializeExecutor(ParameterlessConstructor.class);
	}

	// Test: too much parameters

	@Parser static class TooMuchParameters {
		@Parser.Initialize
		public void initMe(InputStream in, Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testTooMuchParameters() throws Exception {
		createAndInitializeExecutor(TooMuchParameters.class);
	}		

	// Test: multiple annotated methods

	@Parser static class MultipleInitMethod {
		public MultipleInitMethod() {}		
		@Parser.Initialize
		public void initMethod(InputStream in) {}		
		@Parser.Initialize
		public void secondMethod() {}
	}

	@Test(expected=RuntimeException.class)
	public void testDoubleInitMethod() throws Exception {		
		createAndInitializeExecutor(MultipleInitMethod.class);
	}

	// Test: multiple annotated constructors

	@Parser static class MultipleInitConstruct {
		@Parser.Initialize
		public MultipleInitConstruct() {}
		@Parser.Initialize
		public MultipleInitConstruct(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testMultipleConstructors() throws Exception {
		createAndInitializeExecutor(MultipleInitConstruct.class);
	}

}
