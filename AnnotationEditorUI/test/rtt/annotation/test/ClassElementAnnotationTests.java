package rtt.annotation.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.RTTAnnotation;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

public class ClassElementAnnotationTests {

	private ClassModelFactory factory;
	private ClassElement classElement;

	@Before
	public void setUp() throws Exception {
		factory = ClassModelFactory.getFactory();
		classElement = factory.createClassElement(null);
	}
	
	@Test
	public void testEmptyAnnotation() throws Exception {
		assertFalse("Annotation", classElement.hasAnnotation());
		assertNull("Annotation", classElement.getAnnotation());
	}
	
	@Test
	public void testSetNodeAnnotation() throws Exception {
		assertTrue("Adding a Node annotation should be allowed, but was not.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.NODE, classElement));
		assertTrue("Adding a Node annotation should be possible, but was not.",
				ControllerRegistry.execute(Mode.SET, 
				RTTAnnotation.create(AnnotationType.NODE), classElement));
		
		assertTrue("Annotation was not set", classElement.hasAnnotation());
		assertEquals("Annotation", AnnotationType.NODE, classElement.getAnnotation().getType());
	}
	
	@Test
	public void testSetValueAnnotation() throws Exception {
		assertFalse("Adding a Value annotation should not be allowed, but was.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.VALUE, classElement));
		assertFalse("Adding a Value annotation should not be possible, but was.",
				ControllerRegistry.execute(Mode.SET, 
						RTTAnnotation.create(AnnotationType.VALUE), classElement));
		
		assertFalse(classElement.hasAnnotation());
		assertNull(classElement.getAnnotation());
	}
	
	@Test
	public void testSetInitializeAnnotation() throws Exception {
		assertFalse("Adding a Initialize annotation should not be allowed, but was.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.INITIALIZE, classElement));
		assertFalse("Adding a Initialize annotation should not be possible, but was.",
				ControllerRegistry.execute(Mode.SET, 
						RTTAnnotation.create(AnnotationType.INITIALIZE), classElement));
		
		assertFalse(classElement.hasAnnotation());
		assertNull(classElement.getAnnotation());
	}
	
	@Test
	public void testSetNodeAnnotationTwice() throws Exception {
		ControllerRegistry.execute(Mode.SET, RTTAnnotation.create(AnnotationType.NODE), classElement);
		
		assertFalse(ControllerRegistry.canExecute(Mode.SET, AnnotationType.NODE, classElement));
		assertFalse(ControllerRegistry.execute(Mode.SET, RTTAnnotation.create(AnnotationType.NODE), classElement));
	}

}
