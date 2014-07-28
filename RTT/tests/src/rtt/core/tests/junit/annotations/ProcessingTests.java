package rtt.core.tests.junit.annotations;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.Parser;
import rtt.annotations.Parser.Initialize;
import rtt.annotations.processing.NewAnnotationProcessor;

public class ProcessingTests {

	@Before
	public void setUp() throws Exception {
	}
	
	private void checkElements(List<? extends AnnotatedElement> elements, Class<? extends Annotation> annotation, int itemCount) {
		assertNotNull("Elements was null.", elements);
		assertFalse("Elements was empty.", elements.isEmpty());
		assertEquals(itemCount, elements.size());
		for (AnnotatedElement element : elements) {
			assertTrue(element.isAnnotationPresent(annotation));
		}
	}
	
	static class SimpleClass {
		@Initialize public SimpleClass(Object o) {}		
		@Initialize protected SimpleClass(String text) {}		
		@Initialize private SimpleClass(int number) {}
		
		@Compare private String privateCompareString;
		@Compare protected String protectedCompareString;
		@Compare public String publicCompareString;
		
		@Informational private String privateInfoString;
		@Informational protected String protectedInfoString;
		@Informational public String publicInfoString;
		
		@Compare private void privateCompareMethod() {}
		@Compare protected void protectedCompareMethod() {}
		@Compare public void publicCompareMethod() {}
		
		@Informational private void privateInfoMethod() {}
		@Informational protected void protectedInfoMethod() {}
		@Informational public void publicInfoMethod() {}
	}	
	
	@Test
	public void testSimpleInitConstructors() throws Exception {
		List<Constructor<?>> constructors = NewAnnotationProcessor.getConstructors(SimpleClass.class, Parser.Initialize.class);
		checkElements(constructors, Initialize.class, 3);
	}
	
	@Test
	public void testSimpleAnnotatedFields() throws Exception {
		List<Field> compareFields = NewAnnotationProcessor.getFields(SimpleClass.class, Compare.class);
		checkElements(compareFields, Compare.class, 3);
		
		List<Field> infoFields = NewAnnotationProcessor.getFields(SimpleClass.class, Informational.class);
		checkElements(infoFields, Informational.class, 3);
	}
	
	@Test
	public void testSimpleAnnotatedMethods() throws Exception {
		List<Method> compareMethods = NewAnnotationProcessor.getMethods(SimpleClass.class, Compare.class);
		checkElements(compareMethods, Compare.class, 3);
		
		List<Method> infoMethods = NewAnnotationProcessor.getMethods(SimpleClass.class, Informational.class);		
		checkElements(infoMethods, Informational.class, 3);
	}
}
