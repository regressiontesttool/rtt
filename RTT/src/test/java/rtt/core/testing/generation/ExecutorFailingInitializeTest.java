package rtt.core.tests.junit.generation.executor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node;
import rtt.annotations.Node.Initialize;
import rtt.core.archive.input.Input;
import rtt.core.testing.generation.Executor;

public class ExecutorFailingInitializeTests {

	private Input input;
	private List<String> params;

	@Before
	public void setUp() {
		input = new Input();
		input.setValue("This is a testing input");

		params = new ArrayList<>();
	}

	private void initializeExecutor(Class<?> executorClass) throws Throwable {
		Executor executor = new Executor(executorClass);
		executor.initialize(input, params);		
	}

	// Test: No initialize annotation

	@Node static class NoInitAnnotation {
		public NoInitAnnotation() {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoInitAnnotationAnnotation() throws Throwable {		
		initializeExecutor(NoInitAnnotation.class);
	}
	
	// Test: Initialize annotation, but no @Node annotation
	static class NoNodeAnnotation {
		@Initialize public NoNodeAnnotation(InputStream in) {}
	}
	
	@Test(expected=RuntimeException.class)
	public void testNoNodeAnnotation() throws Throwable {
		initializeExecutor(NoNodeAnnotation.class);
	}

	// Test: annotated constructor, but no parameter

	@Node static class NoParameterConstructor {
		@Initialize public NoParameterConstructor() {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoParameterConstructor() throws Throwable {		
		initializeExecutor(NoParameterConstructor.class);
	}

	// Test: annotated method, but no parameter

	@Node static class NoParameterMethod {
		@Initialize public void initMethod() {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoParameterMethod() throws Throwable {		
		initializeExecutor(NoParameterMethod.class);
	}

	// Test: annotated constructor, but wrong parameter

	@Node static class WrongParameterConstructor {
		@Initialize	public WrongParameterConstructor(Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWrongParameterConstructor() throws Throwable {		
		initializeExecutor(WrongParameterConstructor.class);
	}

	// Test: annotated method, but wrong parameter

	@Node static class WrongParameterMethod {
		@Initialize	public void initMethod(Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWrongParameterMethod() throws Throwable {		
		initializeExecutor(WrongParameterMethod.class);
	}

	// Test: annotated method, but no parameter-less constructor

	@Node static class ParameterlessConstructor {
		public ParameterlessConstructor(Object o) {}

		@Initialize	public void initMethod(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoParameterlessConstructor() throws Throwable {		
		initializeExecutor(ParameterlessConstructor.class);
	}

	// Test: too much parameters

	@Node static class TooMuchParameters {
		@Initialize	public void initMe(InputStream in, Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testTooMuchParameters() throws Throwable {
		initializeExecutor(TooMuchParameters.class);
	}		

	// Test: multiple annotated methods

	@Node static class MultipleInitMethod {
		public MultipleInitMethod() {}		
		@Initialize	public void initMethod(InputStream in) {}		
		@Initialize	public void secondMethod() {}
	}

	@Test(expected=RuntimeException.class)
	public void testDoubleInitMethod() throws Throwable {		
		initializeExecutor(MultipleInitMethod.class);
	}

	// Test: multiple annotated constructors

	@Node static class MultipleInitConstruct {
		@Initialize	public MultipleInitConstruct() {}
		@Initialize	public MultipleInitConstruct(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testMultipleConstructors() throws Throwable {
		initializeExecutor(MultipleInitConstruct.class);
	}
	
	// Test: withParameter constructor

	@Node static class WithParamsConstructorClass {
		@Initialize(withParams=true) 
		public WithParamsConstructorClass(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWithParamsConstructor() throws Throwable {
		initializeExecutor(WithParamsConstructorClass.class);
	}

	// Test: withParameter method

	@Node static class WithParamsMethodClass {
		@Initialize(withParams=true) 
		public void init(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWithParamsMethod() throws Throwable {
		initializeExecutor(WithParamsMethodClass.class);
	}
	
	// Test: Inheritance of Initialize annotation is not allowed
	// super class has init constructor or method

	@Node static class SuperParserConstructorClass {
		@Initialize public SuperParserConstructorClass(InputStream in) {}
	}

	@Node static class SuperParserMethodClass {
		@Initialize public void initMethod(InputStream in) {}
	}

	static class ImplementingSuperConstructorClass extends SuperParserConstructorClass {
		public ImplementingSuperConstructorClass(InputStream in) {super(in);}
	}

	static class ImplementingSuperMethodClass extends SuperParserMethodClass {}

	@Test(expected=RuntimeException.class)
	public void testInitializeConstructorInSuper() throws Throwable {
		initializeExecutor(ImplementingSuperConstructorClass.class);
	}
	
	@Test(expected=RuntimeException.class)
	public void testInitializeMethodInSuper() throws Throwable {
		initializeExecutor(ImplementingSuperMethodClass.class);
	}
}
