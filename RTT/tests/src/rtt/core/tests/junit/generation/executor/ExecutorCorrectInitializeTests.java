package rtt.core.tests.junit.generation.executor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Parser;
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

	@Parser static class CorrectPublicMethodClass {
		@Parser.Initialize public void initMethod(InputStream in) {}
	}

	@Test
	public void testCorrectPublicMethodClass() throws Throwable {
		initializeExecutor(CorrectPublicMethodClass.class);
	}

	// Test: correct protected annotated initialize method

	@Parser static class CorrectProtectedMethodClass {
		@Parser.Initialize protected void initMethod(InputStream in) {}
	}

	@Test
	public void testCorrectProtectedMethodClass() throws Throwable {
		initializeExecutor(CorrectProtectedMethodClass.class);
	}

	// Test: correct private annotated initialize method

	@Parser static class CorrectPrivateMethodClass {
		@Parser.Initialize private void initMethod(InputStream in) {}
	}

	@Test
	public void testCorrectPrivateMethodClass() throws Throwable {
		initializeExecutor(CorrectPrivateMethodClass.class);
	}

	// Test: correct public annotated initialize method

	@Parser static class CorrectPublicConstructorClass {
		@Parser.Initialize public CorrectPublicConstructorClass(InputStream in) {}
	}

	@Test
	public void testCorrectPublicConstructorClass() throws Throwable {
		initializeExecutor(CorrectPublicConstructorClass.class);
	}

	// Test: correct protected annotated initialize method

	@Parser static class CorrectProtectedConstructorClass {
		@Parser.Initialize protected CorrectProtectedConstructorClass(InputStream in) {}
	}

	@Test
	public void testCorrectProtectedConstructorClass() throws Throwable {
		initializeExecutor(CorrectProtectedConstructorClass.class);
	}

	// Test: correct private annotated initialize method

	@Parser static class CorrectPrivateConstructorClass {
		@Parser.Initialize private CorrectPrivateConstructorClass(InputStream in) {}
	}

	@Test
	public void testCorrectPrivateConstructorClass() throws Throwable {
		initializeExecutor(CorrectPrivateConstructorClass.class);
	}
	
	// Test: withParams constructor

	@Parser(withParams=true) static class WithParamsConstructorClass {
		@Parser.Initialize public WithParamsConstructorClass(InputStream in, String[] params) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWithParamsConstructor() throws Throwable {
		initializeExecutor(WithParamsConstructorClass.class);
	}

	// Test: withParams method

	@Parser(withParams=true) static class WithParamsMethodClass {
		@Parser.Initialize public void init(InputStream in, String[] params) {}
	}

	@Test(expected=RuntimeException.class)
	public void testWithParamsMethod() throws Throwable {
		initializeExecutor(WithParamsMethodClass.class);
	}
	
	// Test: acceptedExceptions constructor
	
	@Parser(acceptedExceptions={IllegalArgumentException.class}) static class AcceptedExceptionConstructorClass {
		@Parser.Initialize public AcceptedExceptionConstructorClass(InputStream in) {
			throw new IllegalArgumentException("Testing exception");
		}
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testAcceptedExceptionConstructor() throws Throwable {
		initializeExecutor(AcceptedExceptionConstructorClass.class);
	}
	
	// Test: acceptedExceptions method

	@Parser(acceptedExceptions={IllegalArgumentException.class}) static class AcceptedExceptionMethodClass {
		@Parser.Initialize public void initMethod(InputStream in) {
			throw new IllegalArgumentException("Testing exception");
		}
	}
	
	@Test(expected=UnsupportedOperationException.class)
	public void testAcceptedExceptionMethodClass() throws Throwable {
		initializeExecutor(AcceptedExceptionMethodClass.class);
	}

}
