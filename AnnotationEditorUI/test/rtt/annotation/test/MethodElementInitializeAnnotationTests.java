package rtt.annotation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.RTTAnnotation;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

public class MethodElementInitializeAnnotationTests {

	private ClassModelFactory factory;
	private MethodElement method;

	@Before
	public void setUp() throws Exception {
		factory = ClassModelFactory.getFactory();
		method = factory.createMethodElement(null, "aMethod");
		method.getParameters().add("java.io.InputStream");
	}
	
	@Test
	public void testEmptyAnnotation() throws Exception {
		assertFalse("Annotation", method.hasAnnotation());
		assertNull("Annotation", method.getAnnotation());
	}
	
	@Test
	public void testSetNodeAnnotation() throws Exception {
		assertFalse("Adding a Node annotation should not be allowed, but was.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.NODE, method));
		assertFalse("Adding a Node annotation should not be possible, but was.",
				ControllerRegistry.execute(Mode.SET, 
				RTTAnnotation.create(AnnotationType.NODE), method));
		
		assertFalse(method.hasAnnotation());
		assertNull(method.getAnnotation());
	}
	
	@Test
	public void testSetValueAnnotation() throws Exception {
		assertFalse("Adding a Value annotation should not be allowed, but was.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.VALUE, method));
		assertFalse("Adding a Value annotation should not be possible, but was.",
				ControllerRegistry.execute(Mode.SET, 
						RTTAnnotation.create(AnnotationType.VALUE), method));
		
		assertFalse(method.hasAnnotation());
		assertNull("Annotation", method.getAnnotation());
	}
	
	@Test
	public void testSetInitializeAnnotation() throws Exception {
		assertTrue("Adding a Initialize annotation should be allowed, but was not.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.INITIALIZE, method));
		assertTrue("Adding a Initialize annotation should be possible, but was not.",
				ControllerRegistry.execute(Mode.SET, 
						RTTAnnotation.create(AnnotationType.INITIALIZE), method));
		
		assertTrue("Annotation was not set.", method.hasAnnotation());
		assertEquals("Annotation", AnnotationType.INITIALIZE, method.getAnnotation().getType());
	}
	
	@Test
	public void testSetInitAnnotationTwice() throws Exception {
		ControllerRegistry.execute(Mode.SET, RTTAnnotation.create(AnnotationType.INITIALIZE), method);
		
		assertFalse(ControllerRegistry.canExecute(Mode.SET, AnnotationType.INITIALIZE, method));
		assertFalse(ControllerRegistry.execute(Mode.SET, 
				RTTAnnotation.create(AnnotationType.INITIALIZE), method));
	}

}
