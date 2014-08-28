package rtt.core.tests.junit.annotations;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Value;
import rtt.annotations.processing2.AnnotationProcessor2;
import rtt.annotations.processing2.ValueMember;
import rtt.core.tests.junit.utils.TestAnnotationUtils;

@SuppressWarnings("unused")
public class MethodProcessingTests {

	@Before
	public void setUp() throws Exception {}
	
	// --------------------------------------------------------------
	// Test: permuted methods
	//	Only non-void and parameter-less methods should be detected
	//	 --> count = 3 + 3
	
	public static class PermutedMethodsClass {
		
		private void privateVoidMethod() {}
		protected void protectedVoidMethod() {}
		public void publicVoidMethod() {}
		
		private String privateStringMethod() { return ""; }
		protected String protectedStringMethod() { return ""; }
		public String publicStringMethod() { return ""; }
		
		private String privateStringMethod(String param) { return ""; }
		protected String protectedStringMethod(String param) { return ""; }
		public String publicStringMethod(String param) { return ""; }
		
		@Value private void privateCompareVoidMethod() {}
		@Value protected void protectedCompareVoidMethod() {}
		@Value public void publicCompareVoidMethod() {}
		
		@Value private void privateCompareVoidMethod(String param) {}
		@Value protected void protectedCompareVoidMethod(String param) {}
		@Value public void publicCompareVoidMethod(String param) {}
		
		@Value private String privateCompareStringMethod() { return ""; }
		@Value protected String protectedCompareStringMethod() { return ""; }
		@Value public String publicCompareStringMethod() { return ""; }
		
		@Value private String privateCompareStringMethod(String param) { return ""; }
		@Value protected String protectedCompareStringMethod(String param) { return ""; }
		@Value public String publicCompareStringMethod(String param) { return ""; }
		
		@Value(informational=true) private void privateInfoVoidMethod() {}
		@Value(informational=true) protected void protectedInfoVoidMethod() {}
		@Value(informational=true) public void publicInfoVoidMethod() {}
		
		@Value(informational=true) private void privateInformationalVoidMethod(String param) {}
		@Value(informational=true) protected void protectedInformationalVoidMethod(String param) {}
		@Value(informational=true) public void publicInformationalVoidMethod(String param) {}
		
		@Value(informational=true) private String privateInfoStringMethod() { return ""; }
		@Value(informational=true) protected String protectedInfoStringMethod() { return ""; }
		@Value(informational=true) public String publicInfoStringMethod() { return ""; }
		
		@Value(informational=true) private String privateInfoStringMethod(String param) { return ""; }
		@Value(informational=true) protected String protectedInfoStringMethod(String param) { return ""; }
		@Value(informational=true) public String publicInfoStringMethod(String param) { return ""; }
	}
	
	@Test
	public void testPermutedMethods() throws Exception {
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				PermutedMethodsClass.class);
		
		TestAnnotationUtils.countMembers(members, 3, 3);
		TestAnnotationUtils.checkMember(members, PermutedMethodsClass.class);
		
		TestAnnotationUtils.executeMembers(members, PermutedMethodsClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Overriding permuted methods
	// Due to annotations within the super class, the extending class
	// should have also multiple methods detected.
	// --> methods count =  3 &  3 (from PermuterdMethodsClass)
	//                AND  +1 & +1 (from own private methods -> not overwritten)
	
	public static class ExtendingPermutedClass extends PermutedMethodsClass {
		private void privateVoidMethod() {}
		protected void protectedVoidMethod() {}
		public void publicVoidMethod() {}
		
		private String privateStringMethod() { return ""; }
		protected String protectedStringMethod() { return ""; }
		public String publicStringMethod() { return ""; }
		
		private String privateStringMethod(String param) { return ""; }
		protected String protectedStringMethod(String param) { return ""; }
		public String publicStringMethod(String param) { return ""; }
		
		@Value private void privateCompareVoidMethod() {}
		protected void protectedCompareVoidMethod() {}
		public void publicCompareVoidMethod() {}
		
		@Value private void privateCompareVoidMethod(String param) {}
		protected void protectedCompareVoidMethod(String param) {}
		public void publicCompareVoidMethod(String param) {}
		
		@Value private String privateCompareStringMethod() { return ""; }
		protected String protectedCompareStringMethod() { return ""; }
		public String publicCompareStringMethod() { return ""; }
		
		@Value private String privateCompareStringMethod(String param) { return ""; }
		protected String protectedCompareStringMethod(String param) { return ""; }
		public String publicCompareStringMethod(String param) { return ""; }
		
		@Value(informational=true) private void privateInfoVoidMethod() {}
		protected void protectedInfoVoidMethod() {}
		public void publicInfoVoidMethod() {}
		
		@Value(informational=true) private void privateInformationalVoidMethod(String param) {}
		protected void protectedInformationalVoidMethod(String param) {}
		public void publicInformationalVoidMethod(String param) {}
		
		@Value(informational=true) private String privateInfoStringMethod() { return ""; }
		protected String protectedInfoStringMethod() { return ""; }
		public String publicInfoStringMethod() { return ""; }
		
		@Value(informational=true) private String privateInfoStringMethod(String param) { return ""; }
		protected String protectedInfoStringMethod(String param) { return ""; }
		public String publicInfoStringMethod(String param) { return ""; }
	}
	
	@Test
	public void testExtendingPermutedMethods() throws Exception {
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				ExtendingPermutedClass.class);
		
		TestAnnotationUtils.countMembers(members, 4, 4);
		TestAnnotationUtils.checkMember(members, PermutedMethodsClass.class, 
				"privateCompareStringMethod", "privateInfoStringMethod",
				"protectedCompareStringMethod", "protectedInfoStringMethod",
				"publicCompareStringMethod", "publicInfoStringMethod");
		
		TestAnnotationUtils.checkMember(members, ExtendingPermutedClass.class,
				"privateCompareStringMethod", "privateInfoStringMethod");
		
		
		TestAnnotationUtils.executeMembers(members, ExtendingPermutedClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Overriding methods
	// Due to annotations within the super class, the extending class
	// should have also multiple methods detected.
	// --> methods count = 1 (private super class method)
	//                   + 2 (protected & public extending class methods) 
	
	public static class SuperMethodClass {		
		private String privateMethod() { return ""; }
		protected String protectedMethod() { return ""; }
		public String publicMethod() { return ""; }
		
		@Value private String privateCompareMethod() { return ""; }
		@Value protected String protectedCompareMethod() { return ""; }
		@Value public String publicCompareMethod() { return ""; }
		
		@Value(informational=true) private String privateInfoMethod() { return ""; }
		@Value(informational=true) protected String protectedInfoMethod() { return ""; }
		@Value(informational=true) public String publicInfoMethod() { return ""; }
	}
	
	public static class ExtendingMethodClass extends SuperMethodClass {
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
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				ExtendingMethodClass.class);
		
		TestAnnotationUtils.countMembers(members, 3, 3);
		TestAnnotationUtils.checkMember(members, SuperMethodClass.class);
		
		TestAnnotationUtils.executeMembers(members, ExtendingMethodClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Overriding methods twice
	//   Due to annotations within the super class, the 'second' extending class
	//   should have also multiple methods detected.
	//   --> methods count = 1 (private super class method)
	//                     + 2 (protected & public extending class methods) 

	public static class SecondExtendingMethodClass extends ExtendingMethodClass {
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
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				SecondExtendingMethodClass.class);
		
		TestAnnotationUtils.countMembers(members, 3, 3);
		TestAnnotationUtils.checkMember(members, SuperMethodClass.class);
		
		TestAnnotationUtils.executeMembers(members, SecondExtendingMethodClass.class);
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
		
		@Value private String privateCompareMethod() { return ""; }
		@Value protected String protectedCompareMethod() { return ""; }
		@Value public String publicCompareMethod() { return ""; }
		
		@Value(informational=true) private String privateInfoMethod() { return ""; }
		@Value(informational=true) protected String protectedInfoMethod() { return ""; }
		@Value(informational=true) public String publicInfoMethod() { return ""; }
		
		protected abstract String protectedAbstractMethod();
		public abstract String publicAbstractMethod();
		
		@Value protected abstract String protectedAbstractCompareMethod();
		@Value public abstract String publicAbstractCompareMethod();
		
		@Value(informational=true) protected abstract String protectedAbstractInfoMethod();
		@Value(informational=true) public abstract String publicAbstractInfoMethod();
	}
	
	public static class ExtendingAbstractClass extends AbstractClass {
		protected String protectedAbstractMethod() { return ""; }
		public String publicAbstractMethod(){ return ""; }

		protected String protectedAbstractCompareMethod() { return ""; }
		public String publicAbstractCompareMethod() { return ""; }

		protected String protectedAbstractInfoMethod() { return ""; }
		public String publicAbstractInfoMethod() { return ""; }		
	}
	
	@Test
	public void testExtendingAbstractMethods() throws Exception {
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				ExtendingAbstractClass.class);
		
		TestAnnotationUtils.countMembers(members, 5, 5);
		TestAnnotationUtils.checkMember(members, AbstractClass.class);
		
		TestAnnotationUtils.executeMembers(members, ExtendingAbstractClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Implementing interface methods
	//    Annotated methods should be detected.
	
	static interface MethodInterface {
		String interfaceMethod();
		public String publicInterfaceMethod();
		
		@Value String compareInterfaceMethod();
		@Value public String publicInterfaceCompareMethod();
		
		@Value(informational=true) String infoInterfaceMethod();
		@Value(informational=true) public String publicInfoInterfaceMethod();
	}
	
	public static class ImplementingClass implements MethodInterface {
		public String interfaceMethod() { return ""; }
		public String publicInterfaceMethod() { return ""; }
		public String compareInterfaceMethod() { return ""; }
		public String publicInterfaceCompareMethod() { return ""; }
		public String infoInterfaceMethod() { return ""; }
		public String publicInfoInterfaceMethod() { return ""; }		
	}
	
	@Test
	public void testImplementingMethods() throws Exception {
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				ImplementingClass.class);
		
		TestAnnotationUtils.countMembers(members, 2, 2);
		TestAnnotationUtils.checkMember(members, MethodInterface.class);
		
		TestAnnotationUtils.executeMembers(members, ImplementingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: Extending implementing abstract methods 
	//    - (annotated) Interface <- Abstract class <- concrete Class
	//    - methods from concrete class should be detected.
	
	static interface TopInterface {
		public String publicInterfaceMethod1();
		public String publicInterfaceMethod2();
		
		@Value public String publicInterfaceCompareMethod1();
		@Value public String publicInterfaceCompareMethod2();
		
		@Value(informational=true) public String publicInfoInterfaceMethod1();
		@Value(informational=true) public String publicInfoInterfaceMethod2();
	}
	
	static abstract class ImplementingAbstractClass implements TopInterface {
		public String publicInterfaceMethod1() { return ""; }
		public String publicInterfaceCompareMethod1() { return ""; }
		public String publicInfoInterfaceMethod1() { return ""; }
	}
	
	public static class ConcreteClass extends ImplementingAbstractClass {
		public String publicInterfaceMethod2() { return ""; }		
		public String publicInterfaceCompareMethod2() { return ""; }		
		public String publicInfoInterfaceMethod2() { return ""; }
	}
	
	@Test
	public void testImplementingAbstractMethods() throws Exception {
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				ConcreteClass.class);
		
		TestAnnotationUtils.countMembers(members, 2, 2);
		TestAnnotationUtils.checkMember(members, TopInterface.class);
		
		TestAnnotationUtils.executeMembers(members, ConcreteClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: one interfaces extends another
	//    - methods from concrete class should be detected.
	
	interface InterfaceA {
		public String publicInterfaceMethodA();		
		@Value public String publicInterfaceCompareMethodA();
		@Value(informational=true) public String publicInfoInterfaceMethodA();
	}
	
	interface InterfaceB extends InterfaceA {
		public String publicInterfaceMethodB();
		@Value public String publicInterfaceCompareMethodB();
		@Value(informational=true) public String publicInfoInterfaceMethodB();
	}
	
	public static class ExtendedImplementingClass implements InterfaceB {
		public String publicInterfaceMethodA() { return ""; }
		public String publicInterfaceCompareMethodA() { return ""; }
		public String publicInfoInterfaceMethodA() { return ""; }
		public String publicInterfaceMethodB() { return ""; }
		public String publicInterfaceCompareMethodB() { return ""; }
		public String publicInfoInterfaceMethodB() { return ""; }
	}
	
	@Test
	public void testExtendedInterfaceMethods() throws Exception {
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				ExtendedImplementingClass.class);
		
		TestAnnotationUtils.countMembers(members, 2, 2);
		TestAnnotationUtils.checkMember(members, InterfaceA.class, 
				"publicInterfaceCompareMethodA", "publicInfoInterfaceMethodA");
		TestAnnotationUtils.checkMember(members, InterfaceB.class, 
				"publicInterfaceCompareMethodB", "publicInfoInterfaceMethodB");
		
		TestAnnotationUtils.executeMembers(members, ExtendedImplementingClass.class);
	}
	
	// --------------------------------------------------------------
	// Test: value annotation at super *and* extending class should lead
	//       to recognizing of two methods (but returning the same value)
	
	public static class AnnotatedSuperClass {
		@Value protected String protectedCompareMethod() { return ""; };
		@Value public String publicCompareMethod() { return ""; };
		
		@Value(informational=true) protected String protectedInfoMethod() { return ""; };
		@Value(informational=true) public String publicInfoMethod() { return ""; };
	}
	
	public static class AnnotatedExtendingClass extends AnnotatedSuperClass {
		@Value protected String protectedCompareMethod() { return ""; };
		@Value public String publicCompareMethod() { return ""; };
		
		@Value(informational=true) protected String protectedInfoMethod() { return ""; };
		@Value(informational=true) public String publicInfoMethod() { return ""; };
	}
	
	@Test
	public void testDoubleAnnotation() throws Exception {
		Set<ValueMember<?>> members = AnnotationProcessor2.getValueMembers(
				AnnotatedExtendingClass.class);
		
		TestAnnotationUtils.countMembers(members, 4, 4);
		TestAnnotationUtils.checkMember(members, AnnotatedSuperClass.class, 
				"protectedCompareMethod", "publicCompareMethod",
				"protectedInfoMethod", "publicInfoMethod");
		TestAnnotationUtils.checkMember(members, AnnotatedExtendingClass.class, 
				"protectedCompareMethod", "publicCompareMethod",
				"protectedInfoMethod", "publicInfoMethod");
		
		TestAnnotationUtils.executeMembers(members, AnnotatedExtendingClass.class);
	}
	
}
