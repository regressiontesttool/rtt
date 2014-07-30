package rtt.core.tests.junit.annotations;

import java.lang.reflect.Method;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.processing.NewAnnotationProcessor;
import rtt.core.tests.junit.utils.AnnotationUtils;

public class MethodProcessingTests {

	@Before
	public void setUp() throws Exception {}
	
	// --------------------------------------------------------------
	// Test: permuted methods
	//	Only non-void and non-parameter methods 
	//	should be detected by annotation processing
	//	 --> compare methods count = 3 
	//	 --> informational methods count = 3
	
	static class PermutedMethodsClass {
		
		private void privateVoidMethod() {}
		protected void protectedVoidMethod() {}
		public void publicVoidMethod() {}
		
		private String privateStringMethod() { return ""; }
		protected String protectedStringMethod() { return ""; }
		public String publicStringMethod() { return ""; }
		
		private String privateStringMethod(String param) { return ""; }
		protected String protectedStringMethod(String param) { return ""; }
		public String publicStringMethod(String param) { return ""; }
		
		@Compare private void privateCompareVoidMethod() {}
		@Compare protected void protectedCompareVoidMethod() {}
		@Compare public void publicCompareVoidMethod() {}
		
		@Compare private void privateCompareVoidMethod(String param) {}
		@Compare protected void protectedCompareVoidMethod(String param) {}
		@Compare public void publicCompareVoidMethod(String param) {}
		
		// correct annotated methods
		@Compare private String privateCompareStringMethod() { return ""; }
		@Compare protected String protectedCompareStringMethod() { return ""; }
		@Compare public String publicCompareStringMethod() { return ""; }
		// correct annotated methods
		
		@Compare private String privateCompareStringMethod(String param) { return ""; }
		@Compare protected String protectedCompareStringMethod(String param) { return ""; }
		@Compare public String publicCompareStringMethod(String param) { return ""; }
		
		@Informational private void privateInfoVoidMethod() {}
		@Informational protected void protectedInfoVoidMethod() {}
		@Informational public void publicInfoVoidMethod() {}
		
		// correct annotated methods
		@Informational private String privateInfoStringMethod() { return ""; }
		@Informational protected String protectedInfoStringMethod() { return ""; }
		@Informational public String publicInfoStringMethod() { return ""; }
		// correct annotated methods
		
		@Informational private String privateInfoStringMethod(String param) { return ""; }
		@Informational protected String protectedInfoStringMethod(String param) { return ""; }
		@Informational public String publicInfoStringMethod(String param) { return ""; }
	}
	
	@Test
	public void testPermutedMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(
				PermutedMethodsClass.class, Compare.class);
		
		AnnotationUtils.checkElements(compareMethods, Compare.class, 3);
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(
				PermutedMethodsClass.class, Informational.class);
		
		AnnotationUtils.checkElements(infoMethods, Informational.class, 3);
	}
	
	// --------------------------------------------------------------
	// Test: Overriding methods
	// Due to annotations within the super class, the extending class
	// should have also multiple methods detected.
	// --> methods count = 1 (private super class method)
	//                   + 2 (protected & public extending class methods) 
	
	static class SuperMethodClass {		
		private String privateMethod() { return ""; }
		protected String protectedMethod() { return ""; }
		public String publicMethod() { return ""; }
		
		@Compare private String privateCompareMethod() { return ""; }
		@Compare protected String protectedCompareMethod() { return ""; }
		@Compare public String publicCompareMethod() { return ""; }
		
		@Informational private String privateInfoMethod() { return ""; }
		@Informational protected String protectedInfoMethod() { return ""; }
		@Informational public String publicInfoMethod() { return ""; }
	}
	
	static class ExtendingMethodClass extends SuperMethodClass {
		private String privateMethod() { return ""; }
		protected String protectedMethod() { return ""; }
		public String publicMethod() { return ""; }
		
		private String privateCompareMethod() { return ""; }
		protected String protectedCompareMethod() { return ""; }
		public String publicCompareMethod() { return ""; }
		
		private String privateInfoMethod() { return ""; }
		protected String protectedInfoMethod() { return ""; }
		public String publicInfoMethod() { return ""; }
	}
	
	@Test
	public void testExtendingMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(ExtendingMethodClass.class, Compare.class);
		AnnotationUtils.checkElements(compareMethods, Compare.class, 3);
		AnnotationUtils.checkMember(compareMethods, ExtendingMethodClass.class, "publicCompareMethod", "protectedCompareMethod");
		AnnotationUtils.checkMember(compareMethods, SuperMethodClass.class, "privateCompareMethod");
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(ExtendingMethodClass.class, Informational.class);		
		AnnotationUtils.checkElements(infoMethods, Informational.class, 3);
		AnnotationUtils.checkMember(infoMethods, ExtendingMethodClass.class, "publicInfoMethod", "protectedInfoMethod");
		AnnotationUtils.checkMember(infoMethods, SuperMethodClass.class, "privateInfoMethod");
	}
	
	// --------------------------------------------------------------
	// Test: Overriding methods twice
	//   Due to annotations within the super class, the 'second' extending class
	//   should have also multiple methods detected.
	//   --> methods count = 1 (private super class method)
	//                     + 2 (protected & public extending class methods) 

	static class SecondExtendingMethodClass extends ExtendingMethodClass {
		private String privateMethod() { return ""; }
		protected String protectedMethod() { return ""; }
		public String publicMethod() { return ""; }
		
		private String privateCompareMethod() { return ""; }
		protected String protectedCompareMethod() { return ""; }
		public String publicCompareMethod() { return ""; }
		
		private String privateInfoMethod() { return ""; }
		protected String protectedInfoMethod() { return ""; }
		public String publicInfoMethod() { return ""; }
	}
	
	@Test
	public void testSecondExtendingMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(SecondExtendingMethodClass.class, Compare.class);
		AnnotationUtils.checkElements(compareMethods, Compare.class, 3);
		AnnotationUtils.checkMember(compareMethods, SecondExtendingMethodClass.class, "publicCompareMethod", "protectedCompareMethod");
		AnnotationUtils.checkMember(compareMethods, SuperMethodClass.class, "privateCompareMethod");
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(SecondExtendingMethodClass.class, Informational.class);		
		AnnotationUtils.checkElements(infoMethods, Informational.class, 3);		
		AnnotationUtils.checkMember(infoMethods, SecondExtendingMethodClass.class, "publicInfoMethod", "protectedInfoMethod");
		AnnotationUtils.checkMember(infoMethods, SuperMethodClass.class, "privateInfoMethod");
	}
	
	// --------------------------------------------------------------
	// Test: Extending abstract methods
	//    Annotated abstract methods should be detected
	//    --> methods count = 3 (from abstract class)
	//                      + 2 (from extending class)
	
	static abstract class AbstractClass {
		private String privateMethod() { return ""; }
		protected String protectedMethod() { return ""; }
		public String publicMethod() { return ""; }
		
		@Compare private String privateCompareMethod() { return ""; }
		@Compare protected String protectedCompareMethod() { return ""; }
		@Compare public String publicCompareMethod() { return ""; }
		
		@Informational private String privateInfoMethod() { return ""; }
		@Informational protected String protectedInfoMethod() { return ""; }
		@Informational public String publicInfoMethod() { return ""; }
		
		protected abstract String protectedAbstractMethod();
		public abstract String publicAbstractMethod();
		
		@Compare protected abstract String protectedAbstractCompareMethod();
		@Compare public abstract String publicAbstractCompareMethod();
		
		@Informational protected abstract String protectedAbstractInfoMethod();
		@Informational public abstract String publicAbstractInfoMethod();
	}
	
	static class ExtendingAbstractClass extends AbstractClass {
		protected String protectedAbstractMethod() { return ""; }
		public String publicAbstractMethod(){ return ""; }

		protected String protectedAbstractCompareMethod() { return ""; }
		public String publicAbstractCompareMethod() { return ""; }

		protected String protectedAbstractInfoMethod() { return ""; }
		public String publicAbstractInfoMethod() { return ""; }		
	}
	
	@Test
	public void testExtendingAbstractMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(ExtendingAbstractClass.class, Compare.class);
		AnnotationUtils.checkElements(compareMethods, Compare.class, 5);
		AnnotationUtils.checkMember(compareMethods, ExtendingAbstractClass.class, "publicAbstractCompareMethod", "protectedAbstractCompareMethod");
		AnnotationUtils.checkMember(compareMethods, AbstractClass.class, "privateCompareMethod", "publicCompareMethod", "protectedCompareMethod");
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(ExtendingAbstractClass.class, Informational.class);		
		AnnotationUtils.checkElements(infoMethods, Informational.class, 5);		
		AnnotationUtils.checkMember(infoMethods, ExtendingAbstractClass.class, "publicAbstractInfoMethod", "protectedAbstractInfoMethod");
		AnnotationUtils.checkMember(infoMethods, AbstractClass.class, "privateInfoMethod", "publicInfoMethod", "protectedInfoMethod");
	}
	
	// --------------------------------------------------------------
	// Test: Implementing interface methods
	//    Annotated methods should be detected.
	
	static interface MethodInterface {
		String interfaceMethod();
		public String publicInterfaceMethod();
		
		@Compare String compareInterfaceMethod();
		@Compare public String publicInterfaceCompareMethod();
		
		@Informational String infoInterfaceMethod();
		@Informational public String publicInfoInterfaceMethod();
	}
	
	static class ImplementingClass implements MethodInterface {
		public String interfaceMethod() { return ""; }
		public String publicInterfaceMethod() { return ""; }
		public String compareInterfaceMethod() { return ""; }
		public String publicInterfaceCompareMethod() { return ""; }
		public String infoInterfaceMethod() { return ""; }
		public String publicInfoInterfaceMethod() { return ""; }		
	}
	
	@Test
	public void testImplementingMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(ImplementingClass.class, Compare.class);
		AnnotationUtils.checkElements(compareMethods, Compare.class, 2);
		AnnotationUtils.checkMember(compareMethods, ImplementingClass.class);
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(ImplementingClass.class, Informational.class);		
		AnnotationUtils.checkElements(infoMethods, Informational.class, 2);		
		AnnotationUtils.checkMember(infoMethods, ImplementingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Extending implementing abstract methods 
	//    - (annotated) Interface <- Abstract class <- concrete Class
	//    - methods from concrete class should be detected.
	
	static interface TopInterface {
		public String publicInterfaceMethod1();
		public String publicInterfaceMethod2();
		
		@Compare public String publicInterfaceCompareMethod1();
		@Compare public String publicInterfaceCompareMethod2();
		
		@Informational public String publicInfoInterfaceMethod1();
		@Informational public String publicInfoInterfaceMethod2();
	}
	
	static abstract class ImplementingAbstractClass implements TopInterface {
		public String publicInterfaceMethod1() { return ""; }
		public String publicInterfaceCompareMethod1() { return ""; }
		public String publicInfoInterfaceMethod1() { return ""; }
	}
	
	static class ConcreteClass extends ImplementingAbstractClass {
		public String publicInterfaceMethod2() { return ""; }		
		public String publicInterfaceCompareMethod2() { return ""; }		
		public String publicInfoInterfaceMethod2() { return ""; }
	}
	
	@Test
	public void testImplementingAbstractMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(ConcreteClass.class, Compare.class);
		AnnotationUtils.checkElements(compareMethods, Compare.class, 2);
		AnnotationUtils.checkMember(compareMethods, ConcreteClass.class, "publicInterfaceMethod2", "publicInterfaceCompareMethod2", "publicInterfaceInfoMethod2");
		AnnotationUtils.checkMember(compareMethods, ImplementingAbstractClass.class, "publicInterfaceMethod1", "publicInterfaceCompareMethod1", "publicInterfaceInfoMethod1");
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(ConcreteClass.class, Informational.class);		
		AnnotationUtils.checkElements(infoMethods, Informational.class, 2);		
		AnnotationUtils.checkMember(infoMethods, ConcreteClass.class, "publicInterfaceMethod2", "publicInterfaceCompareMethod2", "publicInterfaceInfoMethod2");
		AnnotationUtils.checkMember(infoMethods, ImplementingAbstractClass.class, "publicInterfaceMethod1", "publicInterfaceCompareMethod1", "publicInterfaceInfoMethod1");
	}
	
	// --------------------------------------------------------------
	// Test: one interfaces extends another
	//    - methods from concrete class should be detected.
	
	interface InterfaceA {
		public String publicInterfaceMethodA();		
		@Compare public String publicInterfaceCompareMethodA();
		@Informational public String publicInfoInterfaceMethodA();
	}
	
	interface InterfaceB extends InterfaceA {
		public String publicInterfaceMethodB();
		@Compare public String publicInterfaceCompareMethodB();
		@Informational public String publicInfoInterfaceMethodB();
	}
	
	static class ExtendedImplementingClass implements InterfaceB {
		public String publicInterfaceMethodA() { return ""; }
		public String publicInterfaceCompareMethodA() { return ""; }
		public String publicInfoInterfaceMethodA() { return ""; }
		public String publicInterfaceMethodB() { return ""; }
		public String publicInterfaceCompareMethodB() { return ""; }
		public String publicInfoInterfaceMethodB() { return ""; }
	}
	
	@Test
	public void testExtendedInterfaceMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(ExtendedImplementingClass.class, Compare.class);
		AnnotationUtils.checkElements(compareMethods, Compare.class, 2);
		AnnotationUtils.checkMember(compareMethods, ExtendedImplementingClass.class);
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(ExtendedImplementingClass.class, Informational.class);		
		AnnotationUtils.checkElements(infoMethods, Informational.class, 2);		
		AnnotationUtils.checkMember(infoMethods, ExtendedImplementingClass.class);
	}
	
}
