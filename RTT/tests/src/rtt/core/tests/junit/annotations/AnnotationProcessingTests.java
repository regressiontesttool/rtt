package rtt.core.tests.junit.annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
		for (AnnotatedElement element : elements) {
			assertTrue(element.isAnnotationPresent(annotation));
		}
	}
	
	private void checkMethods(Collection<Method> methods, Class<?> fromClass) {
//		for (Method method : fromClass.getDeclaredMethods()) {
//			if (!Modifier.isPrivate(method.getModifiers())) {
//				String methodName = method.getName();
//				for (Method listedMethod : methods) {
//					if (listedMethod.)
//				}
//			}			
//		}
		
		// TODO Auto-generated method stub
		
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
		
		private void privateVoidMethod() {}
		protected void protectedVoidMethod() {}
		public void publicVoidMethod() {}
		
		private String privateStringMethod() { return ""; }
		protected String protectedStringMethod() { return ""; }
		public String publicStringMethod() { return ""; }
		
		@Compare private void privateCompareVoidMethod() {}
		@Compare protected void protectedCompareVoidMethod() {}
		@Compare public void publicCompareVoidMethod() {}
		@Compare public void publicCompareParamVoidMethod(String param) {}
		
		@Compare private String privateCompareStringMethod() { return ""; }
		@Compare protected String protectedCompareStringMethod() { return ""; }
		@Compare public String publicCompareStringMethod() { return ""; }
		
		@Compare public String publicCompareParamStringMethod(String param) { return ""; }
		
		@Informational private void privateInfoVoidMethod() {}
		@Informational protected void protectedInfoVoidMethod() {}
		@Informational public void publicInfoVoidMethod() {}
		
		@Informational private String privateInfoStringMethod() { return ""; }
		@Informational protected String protectedInfoStringMethod() { return ""; }
		@Informational public String publicInfoStringMethod() { return ""; }
		
		@Informational public String publicInfoParamStringMethod(String param) { return ""; }
	}	
	
	@Test
	public void testSimpleInitConstructors() throws Exception {
		Collection<Constructor<?>> constructors = NewAnnotationProcessor.getConstructors(SimpleClass.class, Parser.Initialize.class);
		checkElements(constructors, Initialize.class, 3);
	}
	
	@Test
	public void testSimpleAnnotatedFields() throws Exception {
		Collection<Field> compareFields = NewAnnotationProcessor.getFields(SimpleClass.class, Compare.class);
		checkElements(compareFields, Compare.class, 3);
		
		Collection<Field> infoFields = NewAnnotationProcessor.getFields(SimpleClass.class, Informational.class);
		checkElements(infoFields, Informational.class, 3);
	}
	
	@Test
	public void testSimpleAnnotatedMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(SimpleClass.class, Compare.class);
		checkElements(compareMethods, Compare.class, 3);
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(SimpleClass.class, Informational.class);		
		checkElements(infoMethods, Informational.class, 3);
	}
	
	static class ExtendingClass extends SimpleClass {
		private String privateString;
		protected String protectedString;
		public String publicString;
		
		@Compare private String privateCompareString;
		@Compare protected String protectedCompareString;
		@Compare public String publicCompareString;
		
		@Informational private String privateInfoString;
		@Informational protected String protectedInfoString;
		@Informational public String publicInfoString;
		
		private void privateMethod() {}
		protected void protectedMethod() {}
		public void publicMethod() {}
		
		private void privateCompareMethod() {}
		protected void protectedCompareMethod() {}
		public void publicCompareMethod() {}
		
		private void privateInfoMethod() {}
		protected void protectedInfoMethod() {}
		public void publicInfoMethod() {}
	}
	
//	@Test
//	public void testExtendingInitConstructors() throws Exception {
//		List<Constructor<?>> constructors = NewAnnotationProcessor.getConstructors(ExtendingClass.class, Parser.Initialize.class);
//		checkElements(constructors, Initialize.class, 3);
//	}
	
	@Test
	public void testExtendingAnnotatedFields() throws Exception {
		Collection<Field> compareFields = NewAnnotationProcessor.getFields(ExtendingClass.class, Compare.class);
		checkElements(compareFields, Compare.class, 6);
		
		Collection<Field> infoFields = NewAnnotationProcessor.getFields(ExtendingClass.class, Informational.class);
		checkElements(infoFields, Informational.class, 6);
	}
	
	@Test
	public void testExtendingAnnotatedMethods() throws Exception {
		Collection<Method> compareMethods = NewAnnotationProcessor.getMethods(ExtendingClass.class, Compare.class);
		checkElements(compareMethods, Compare.class, 3);
		checkMethods(compareMethods, ExtendingClass.class);
		
		Collection<Method> infoMethods = NewAnnotationProcessor.getMethods(ExtendingClass.class, Informational.class);		
		checkElements(infoMethods, Informational.class, 3);
	}

	
}
