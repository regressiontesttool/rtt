package rtt.core.tests.junit.generation.executor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node;
import rtt.core.archive.input.Input;
import rtt.core.testing.generation.Executor;

public class ExecutorCorrectInitializeTests {	

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

	// Test: correct public annotated initialize method

	@Node static class CorrectPublicMethodClass {
		@Node.Initialize public void initMethod(InputStream in) {}
	}

	@Test
	public void testCorrectPublicMethodClass() throws Throwable {
		initializeExecutor(CorrectPublicMethodClass.class);
	}

	// Test: correct protected annotated initialize method

	@Node static class CorrectProtectedMethodClass {
		@Node.Initialize protected void initMethod(InputStream in) {}
	}

	@Test
	public void testCorrectProtectedMethodClass() throws Throwable {
		initializeExecutor(CorrectProtectedMethodClass.class);
	}

	// Test: correct private annotated initialize method

	@Node static class CorrectPrivateMethodClass {
		@Node.Initialize private void initMethod(InputStream in) {}
	}

	@Test
	public void testCorrectPrivateMethodClass() throws Throwable {
		initializeExecutor(CorrectPrivateMethodClass.class);
	}

	// Test: correct public annotated initialize method

	@Node static class CorrectPublicConstructorClass {
		@Node.Initialize public CorrectPublicConstructorClass(InputStream in) {}
	}

	@Test
	public void testCorrectPublicConstructorClass() throws Throwable {
		initializeExecutor(CorrectPublicConstructorClass.class);
	}

	// Test: correct protected annotated initialize method

	@Node static class CorrectProtectedConstructorClass {
		@Node.Initialize protected CorrectProtectedConstructorClass(InputStream in) {}
	}

	@Test
	public void testCorrectProtectedConstructorClass() throws Throwable {
		initializeExecutor(CorrectProtectedConstructorClass.class);
	}

	// Test: correct private annotated initialize method

	@Node static class CorrectPrivateConstructorClass {
		@Node.Initialize private CorrectPrivateConstructorClass(InputStream in) {}
	}

	@Test
	public void testCorrectPrivateConstructorClass() throws Throwable {
		initializeExecutor(CorrectPrivateConstructorClass.class);
	}
	
	// Test: withParams constructor

	@Node static class WithParamsConstructorClass {
		@Node.Initialize(withParams=true) 
		public WithParamsConstructorClass(InputStream in, String[] params) {}
	}

	@Test
	public void testWithParamsConstructor() throws Throwable {
		initializeExecutor(WithParamsConstructorClass.class);
	}

	// Test: withParams method

	@Node static class WithParamsMethodClass {
		@Node.Initialize(withParams=true) 
		public void init(InputStream in, String[] params) {}
	}

	@Test
	public void testWithParamsMethod() throws Throwable {
		initializeExecutor(WithParamsMethodClass.class);
	}
	
	// Test: acceptedExceptions constructor
	
	@Node static class AcceptedExceptionConstructorClass {
		@Node.Initialize(acceptedExceptions={IllegalArgumentException.class})
		public AcceptedExceptionConstructorClass(InputStream in) {
			throw new IllegalArgumentException("Testing exception");
		}
	}

}
