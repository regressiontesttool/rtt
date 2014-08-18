package rtt.core.tests.junit.generation.executor;

import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node;
import rtt.core.archive.input.Input;
import rtt.core.testing.generation.Executor;

public class ExecutorInheritanceInitializeTests {

	private Input input;
	private ArrayList<String> params;

	@Before
	public void setUp() throws Exception {
		input = new Input();
		input.setValue("This is a testing input");

		params = new ArrayList<>();
	}
	
	private void initializeExecutor(Class<?> executorClass) throws Throwable {
		Executor executor = new Executor(executorClass);
		executor.initialize(input, params);		
	}

	// Test: super class has parser annotation

	@Node static class SuperParserClass {
		public SuperParserClass() {}
	}

	static class ImplementingConstructorClass extends SuperParserClass {
		@Node.Initialize public ImplementingConstructorClass(InputStream in) {super();}
	}
	
	static class ImplementingMethodClass extends SuperParserClass {
		@Node.Initialize public void initMethod(InputStream in) {}
	}

	@Test
	public void testInitializeInConcrete() throws Throwable {
		initializeExecutor(ImplementingConstructorClass.class);
		initializeExecutor(ImplementingMethodClass.class);
	}
	
	// Test: super class has parser annotation
	// init constructor & method has super class
	
	@Node static class SuperParserConstructorClass {
		@Node.Initialize public SuperParserConstructorClass(InputStream in) {}
	}
	
	@Node static class SuperParserMethodClass {
		@Node.Initialize public void initMethod(InputStream in) {}
	}
	
	static class ImplementingSuperConstructorClass extends SuperParserConstructorClass {
		public ImplementingSuperConstructorClass(InputStream in) {super(in);}
	}
	
	static class ImplementingSuperMethodClass extends SuperParserMethodClass {}
	
	@Test
	public void testInitializeInSuper() throws Throwable {
		initializeExecutor(ImplementingSuperConstructorClass.class);
		initializeExecutor(ImplementingSuperMethodClass.class);
	}
}
