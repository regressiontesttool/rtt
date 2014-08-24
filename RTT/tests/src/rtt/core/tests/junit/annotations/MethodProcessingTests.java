package rtt.core.tests.junit.annotations;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.processing.AnnotationProcessor;
import rtt.core.tests.junit.utils.TestAnnotationUtils;

@SuppressWarnings("unused")
public class MethodProcessingTests {

	@Before
	public void setUp() throws Exception {}
	
	private void invokeMethods(List<Method> methods, Class<?> classType) {
		try {
			Object object = classType.newInstance();
			List<Object> params = new ArrayList<>();
			for (Method method : methods) {
				params.clear();
				for (Class<?> param : Arrays.asList(method.getParameterTypes())) {
					params.add(param.newInstance());
				}
				
				method.setAccessible(true);
				method.invoke(object, params.toArray());
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	// --------------------------------------------------------------
	// Test: permuted methods
	//	Any method annotated should be detected
	//	 --> count = 12
	
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
		
		@Compare private String privateCompareStringMethod() { return ""; }
		@Compare protected String protectedCompareStringMethod() { return ""; }
		@Compare public String publicCompareStringMethod() { return ""; }
		
		@Compare private String privateCompareStringMethod(String param) { return ""; }
		@Compare protected String protectedCompareStringMethod(String param) { return ""; }
		@Compare public String publicCompareStringMethod(String param) { return ""; }
		
		@Informational private void privateInfoVoidMethod() {}
		@Informational protected void protectedInfoVoidMethod() {}
		@Informational public void publicInfoVoidMethod() {}
		
		@Informational private void privateInformationalVoidMethod(String param) {}
		@Informational protected void protectedInformationalVoidMethod(String param) {}
		@Informational public void publicInformationalVoidMethod(String param) {}
		
		@Informational private String privateInfoStringMethod() { return ""; }
		@Informational protected String protectedInfoStringMethod() { return ""; }
		@Informational public String publicInfoStringMethod() { return ""; }
		
		@Informational private String privateInfoStringMethod(String param) { return ""; }
		@Informational protected String protectedInfoStringMethod(String param) { return ""; }
		@Informational public String publicInfoStringMethod(String param) { return ""; }
	}
	
	@Test
	public void testPermutedMethods() throws Exception {
		List<Method> compareMethods = AnnotationProcessor.getMethods(
				PermutedMethodsClass.class, Compare.class);
		
		TestAnnotationUtils.checkElements(compareMethods, Compare.class, 12);
		
		List<Method> infoMethods = AnnotationProcessor.getMethods(
				PermutedMethodsClass.class, Informational.class);
		
		TestAnnotationUtils.checkElements(infoMethods, Informational.class, 12);
		
		invokeMethods(compareMethods, PermutedMethodsClass.class);
		invokeMethods(infoMethods, PermutedMethodsClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Overriding permuted methods
	// Due to annotations within the super class, the extending class
	// should have also multiple methods detected.
	// --> methods count = 12 
	
	static class ExtendingPermutedClass extends PermutedMethodsClass {
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
		protected void protectedCompareVoidMethod() {}
		public void publicCompareVoidMethod() {}
		
		@Compare private void privateCompareVoidMethod(String param) {}
		protected void protectedCompareVoidMethod(String param) {}
		public void publicCompareVoidMethod(String param) {}
		
		@Compare private String privateCompareStringMethod() { return ""; }
		protected String protectedCompareStringMethod() { return ""; }
		public String publicCompareStringMethod() { return ""; }
		
		@Compare private String privateCompareStringMethod(String param) { return ""; }
		protected String protectedCompareStringMethod(String param) { return ""; }
		public String publicCompareStringMethod(String param) { return ""; }
		
		@Informational private void privateInfoVoidMethod() {}
		protected void protectedInfoVoidMethod() {}
		public void publicInfoVoidMethod() {}
		
		@Informational private void privateInformationalVoidMethod(String param) {}
		protected void protectedInformationalVoidMethod(String param) {}
		public void publicInformationalVoidMethod(String param) {}
		
		@Informational private String privateInfoStringMethod() { return ""; }
		protected String protectedInfoStringMethod() { return ""; }
		public String publicInfoStringMethod() { return ""; }
		
		@Informational private String privateInfoStringMethod(String param) { return ""; }
		protected String protectedInfoStringMethod(String param) { return ""; }
		public String publicInfoStringMethod(String param) { return ""; }
	}
	
	@Test
	public void testExtendingPermutedMethods() throws Exception {
		List<Method> compareMethods = AnnotationProcessor.getMethods(
				ExtendingPermutedClass.class, Compare.class);
		
		TestAnnotationUtils.checkElements(compareMethods, Compare.class, 12);
		
		List<Method> infoMethods = AnnotationProcessor.getMethods(
				ExtendingPermutedClass.class, Informational.class);
		
		TestAnnotationUtils.checkElements(infoMethods, Informational.class, 12);
		
		invokeMethods(compareMethods, ExtendingPermutedClass.class);
		invokeMethods(infoMethods, ExtendingPermutedClass.class);
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
		List<Method> compareMethods = AnnotationProcessor.getMethods(ExtendingMethodClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareMethods, Compare.class, 3);
		TestAnnotationUtils.checkMember(compareMethods, ExtendingMethodClass.class, "publicCompareMethod", "protectedCompareMethod");
		TestAnnotationUtils.checkMember(compareMethods, SuperMethodClass.class, "privateCompareMethod");
		
		List<Method> infoMethods = AnnotationProcessor.getMethods(ExtendingMethodClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoMethods, Informational.class, 3);
		TestAnnotationUtils.checkMember(infoMethods, ExtendingMethodClass.class, "publicInfoMethod", "protectedInfoMethod");
		TestAnnotationUtils.checkMember(infoMethods, SuperMethodClass.class, "privateInfoMethod");
		
		invokeMethods(compareMethods, ExtendingMethodClass.class);
		invokeMethods(infoMethods, ExtendingMethodClass.class);
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
		List<Method> compareMethods = AnnotationProcessor.getMethods(SecondExtendingMethodClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareMethods, Compare.class, 3);
		TestAnnotationUtils.checkMember(compareMethods, SecondExtendingMethodClass.class, "publicCompareMethod", "protectedCompareMethod");
		TestAnnotationUtils.checkMember(compareMethods, SuperMethodClass.class, "privateCompareMethod");
		
		List<Method> infoMethods = AnnotationProcessor.getMethods(SecondExtendingMethodClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoMethods, Informational.class, 3);		
		TestAnnotationUtils.checkMember(infoMethods, SecondExtendingMethodClass.class, "publicInfoMethod", "protectedInfoMethod");
		TestAnnotationUtils.checkMember(infoMethods, SuperMethodClass.class, "privateInfoMethod");
		
		invokeMethods(compareMethods, SecondExtendingMethodClass.class);
		invokeMethods(infoMethods, SecondExtendingMethodClass.class);
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
		List<Method> compareMethods = AnnotationProcessor.getMethods(ExtendingAbstractClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareMethods, Compare.class, 5);
		TestAnnotationUtils.checkMember(compareMethods, ExtendingAbstractClass.class, "publicAbstractCompareMethod", "protectedAbstractCompareMethod");
		TestAnnotationUtils.checkMember(compareMethods, AbstractClass.class, "privateCompareMethod", "publicCompareMethod", "protectedCompareMethod");
		
		List<Method> infoMethods = AnnotationProcessor.getMethods(ExtendingAbstractClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoMethods, Informational.class, 5);		
		TestAnnotationUtils.checkMember(infoMethods, ExtendingAbstractClass.class, "publicAbstractInfoMethod", "protectedAbstractInfoMethod");
		TestAnnotationUtils.checkMember(infoMethods, AbstractClass.class, "privateInfoMethod", "publicInfoMethod", "protectedInfoMethod");
		
		invokeMethods(compareMethods, ExtendingAbstractClass.class);
		invokeMethods(infoMethods, ExtendingAbstractClass.class);
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
		List<Method> compareMethods = AnnotationProcessor.getMethods(ImplementingClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareMethods, Compare.class, 2);
		TestAnnotationUtils.checkMember(compareMethods, ImplementingClass.class);
		
		List<Method> infoMethods = AnnotationProcessor.getMethods(ImplementingClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoMethods, Informational.class, 2);		
		TestAnnotationUtils.checkMember(infoMethods, ImplementingClass.class);
		
		invokeMethods(compareMethods, ImplementingClass.class);
		invokeMethods(infoMethods, ImplementingClass.class);
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
		List<Method> compareMethods = AnnotationProcessor.getMethods(ConcreteClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareMethods, Compare.class, 2);
		TestAnnotationUtils.checkMember(compareMethods, ConcreteClass.class, "publicInterfaceMethod2", "publicInterfaceCompareMethod2", "publicInterfaceInfoMethod2");
		TestAnnotationUtils.checkMember(compareMethods, ImplementingAbstractClass.class, "publicInterfaceMethod1", "publicInterfaceCompareMethod1", "publicInterfaceInfoMethod1");
		
		List<Method> infoMethods = AnnotationProcessor.getMethods(ConcreteClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoMethods, Informational.class, 2);		
		TestAnnotationUtils.checkMember(infoMethods, ConcreteClass.class, "publicInterfaceMethod2", "publicInterfaceCompareMethod2", "publicInterfaceInfoMethod2");
		TestAnnotationUtils.checkMember(infoMethods, ImplementingAbstractClass.class, "publicInterfaceMethod1", "publicInterfaceCompareMethod1", "publicInterfaceInfoMethod1");
		
		invokeMethods(compareMethods, ConcreteClass.class);
		invokeMethods(infoMethods, ConcreteClass.class);
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
		List<Method> compareMethods = AnnotationProcessor.getMethods(ExtendedImplementingClass.class, Compare.class);
		TestAnnotationUtils.checkElements(compareMethods, Compare.class, 2);
		TestAnnotationUtils.checkMember(compareMethods, ExtendedImplementingClass.class);
		
		List<Method> infoMethods = AnnotationProcessor.getMethods(ExtendedImplementingClass.class, Informational.class);		
		TestAnnotationUtils.checkElements(infoMethods, Informational.class, 2);		
		TestAnnotationUtils.checkMember(infoMethods, ExtendedImplementingClass.class);
		
		invokeMethods(compareMethods, ExtendedImplementingClass.class);
		invokeMethods(infoMethods, ExtendedImplementingClass.class);
	}
	
}
