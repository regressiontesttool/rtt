package rtt.core.tests.junit.generation.executor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node;
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

	// Test: annotated constructor, but no parameter

	@Node static class NoParameterConstructor {
		@Node.Initialize
		public NoParameterConstructor() {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoParameterConstructor() throws Throwable {		
		initializeExecutor(NoParameterConstructor.class);
	}

	// Test: annotated method, but no parameter

	@Node static class NoParameterMethod {
		@Node.Initialize
		public void initMethod() {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoParameterMethod() throws Throwable {		
		initializeExecutor(NoParameterMethod.class);
	}

	// Test: annotated constructor, but wrong parameter

	@Node static class WrongParameterConstructor {
		@Node.Initialize
		public WrongParameterConstructor(Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWrongParameterConstructor() throws Throwable {		
		initializeExecutor(WrongParameterConstructor.class);
	}

	// Test: annotated method, but wrong parameter

	@Node static class WrongParameterMethod {
		@Node.Initialize
		public void initMethod(Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWrongParameterMethod() throws Throwable {		
		initializeExecutor(WrongParameterMethod.class);
	}

	// Test: annotated method, but no parameter-less constructor

	@Node static class ParameterlessConstructor {
		public ParameterlessConstructor(Object o) {}

		@Node.Initialize
		public void initMethod(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testNoParameterlessConstructor() throws Throwable {		
		initializeExecutor(ParameterlessConstructor.class);
	}

	// Test: too much parameters

	@Node static class TooMuchParameters {
		@Node.Initialize
		public void initMe(InputStream in, Object o) {}
	}

	@Test(expected=RuntimeException.class)
	public void testTooMuchParameters() throws Throwable {
		initializeExecutor(TooMuchParameters.class);
	}		

	// Test: multiple annotated methods

	@Node static class MultipleInitMethod {
		public MultipleInitMethod() {}		
		@Node.Initialize
		public void initMethod(InputStream in) {}		
		@Node.Initialize
		public void secondMethod() {}
	}

	@Test(expected=RuntimeException.class)
	public void testDoubleInitMethod() throws Throwable {		
		initializeExecutor(MultipleInitMethod.class);
	}

	// Test: multiple annotated constructors

	@Node static class MultipleInitConstruct {
		@Node.Initialize
		public MultipleInitConstruct() {}
		@Node.Initialize
		public MultipleInitConstruct(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testMultipleConstructors() throws Throwable {
		initializeExecutor(MultipleInitConstruct.class);
	}
	
	// Test: withParameter constructor

	@Node static class WithParamsConstructorClass {
		@Node.Initialize(withParams=true)
		public WithParamsConstructorClass(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWithParamsConstructor() throws Throwable {
		initializeExecutor(WithParamsConstructorClass.class);
	}

	// Test: withParameter method

	@Node static class WithParamsMethodClass {
		@Node.Initialize(withParams=true)
		public void init(InputStream in) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWithParamsMethod() throws Throwable {
		initializeExecutor(WithParamsMethodClass.class);
	}
	
	
	
	
}
