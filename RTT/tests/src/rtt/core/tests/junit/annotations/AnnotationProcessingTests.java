package rtt.core.tests.junit.annotations;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.Parser;
import rtt.annotations.Parser.Initialize;
import rtt.annotations.processing.NewAnnotationProcessor;

public class AnnotationProcessingTests {

	@Before
	public void setUp() throws Exception {
	}
	
	private void checkElements(
			Collection<? extends AnnotatedElement> elements, 
			Class<? extends Annotation> annotation, int itemCount) {
		
		assertNotNull("Elements was null.", elements);
		assertFalse("Elements was empty.", elements.isEmpty());
		assertEquals(itemCount, elements.size());
	}
	
	private void checkMember(Iterable<? extends Member> members, Class<?> fromClass, String... memberNames) {
		Arrays.sort(memberNames);		
		for (Member member : members) {
			if (Arrays.binarySearch(memberNames, member.getName()) >= 0) {
				if (!member.getDeclaringClass().equals(fromClass)) {
					String fromClassName = fromClass.getSimpleName();
					String declaringClassName = member.getDeclaringClass().getSimpleName();
					String memberName = member.getClass().getSimpleName() + " " + member.getName();
					
					fail("The member '" + memberName + "' was declared by " + declaringClassName + " and not by " + fromClassName);
				}
			}
		}
	}
	
	static class SimpleClass {
		public SimpleClass() {}		
		protected SimpleClass(boolean hasText) {}		
		private SimpleClass(float number) {}		
		
		@Initialize public SimpleClass(Object o) {}		
		@Initialize protected SimpleClass(String text) {}		
		@Initialize private SimpleClass(int number) {}
		
		private String privateString;
		protected String protectedString;
		public String publicString;
		
		@Compare private String privateCompareString;
		@Compare protected String protectedCompareString;
		@Compare public String publicCompareString;
		
		@Informational private String privateInfoString;
		@Informational protected String protectedInfoString;
		@Informational public String publicInfoString;
	}	
	
//	@Test
//	public void testSimpleInitConstructors() throws Exception {
//		Collection<Constructor<?>> constructors = NewAnnotationProcessor.getConstructors(SimpleClass.class, Parser.Initialize.class);
//		checkElements(constructors, Initialize.class, 3);
//	}
	
	// --------------------------------------------------------------
	// Test: permuted fields
	//	 Only annotated fields should be 
	//	 detected by annotation processing
	//	 --> compare fields count = 3 
	//	 --> informational fields count = 3
	
	static class PermutedFieldsClass {
		private String privateString;
		protected String protectedString;
		public String publicString;
		
		@Compare private String privateCompareString;
		@Compare protected String protectedCompareString;
		@Compare public String publicCompareString;
		
		@Informational private String privateInfoString;
		@Informational protected String protectedInfoString;
		@Informational public String publicInfoString;
	}
	
	@Test
	public void testPermutedFields() throws Exception {
		Collection<Field> compareFields = NewAnnotationProcessor.getFields(
				PermutedFieldsClass.class, Compare.class);
		
		checkElements(compareFields, Compare.class, 3);
		
		Collection<Field> infoFields = NewAnnotationProcessor.getFields(
				PermutedFieldsClass.class, Informational.class);
		
		checkElements(infoFields, Informational.class, 3);
	}
	
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
		
		checkElements(compareMethods, Compare.class, 3);
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(
				PermutedMethodsClass.class, Informational.class);
		
		checkElements(infoMethods, Informational.class, 3);
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
		checkElements(compareMethods, Compare.class, 3);
		checkMember(compareMethods, ExtendingMethodClass.class, "publicCompareMethod", "protectedCompareMethod");
		checkMember(compareMethods, SuperMethodClass.class, "privateCompareMethod");
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(ExtendingMethodClass.class, Informational.class);		
		checkElements(infoMethods, Informational.class, 3);
		checkMember(infoMethods, ExtendingMethodClass.class, "publicInfoMethod", "protectedInfoMethod");
		checkMember(infoMethods, SuperMethodClass.class, "privateInfoMethod");
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
		checkElements(compareMethods, Compare.class, 3);
		checkMember(compareMethods, SecondExtendingMethodClass.class, "publicCompareMethod", "protectedCompareMethod");
		checkMember(compareMethods, SuperMethodClass.class, "privateCompareMethod");
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(SecondExtendingMethodClass.class, Informational.class);		
		checkElements(infoMethods, Informational.class, 3);		
		checkMember(infoMethods, SecondExtendingMethodClass.class, "publicInfoMethod", "protectedInfoMethod");
		checkMember(infoMethods, SuperMethodClass.class, "privateInfoMethod");
	}
	
}
