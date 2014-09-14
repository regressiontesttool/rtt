package rtt.annotation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.RTTAnnotation;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

public class MethodElementValueAnnotationTests {

	private ClassModelFactory factory;
	private MethodElement method;

	@Before
	public void setUp() throws Exception {
		factory = ClassModelFactory.getFactory();
		method = factory.createMethodElement(null, "aMethod");
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
		assertTrue("Adding a Value annotation should be allowed, but was not.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.VALUE, method));
		assertTrue("Adding a Value annotation should be possible, but was not.",
				ControllerRegistry.execute(Mode.SET, 
						RTTAnnotation.create(AnnotationType.VALUE), method));
		
		assertTrue("Annotation was not set", method.hasAnnotation());
		assertEquals("Annotation", AnnotationType.VALUE, method.getAnnotation().getType());
	}
	
	@Test
	public void testSetInitializeAnnotation() throws Exception {
		assertFalse("Adding a Initialize annotation should not be allowed, but was.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.INITIALIZE, method));
		assertFalse("Adding a Initialize annotation should not be possible, but was.",
				ControllerRegistry.execute(Mode.SET, 
						RTTAnnotation.create(AnnotationType.INITIALIZE), method));
		
		assertFalse(method.hasAnnotation());
		assertNull(method.getAnnotation());
	}
	
	@Test
	public void testSetValueAnnotationTwice() throws Exception {
		ControllerRegistry.execute(Mode.SET, RTTAnnotation.create(AnnotationType.VALUE), method);
		
		assertFalse(ControllerRegistry.canExecute(Mode.SET, AnnotationType.VALUE, method));
		assertFalse(ControllerRegistry.execute(Mode.SET, 
				RTTAnnotation.create(AnnotationType.VALUE), method));
	}

}
