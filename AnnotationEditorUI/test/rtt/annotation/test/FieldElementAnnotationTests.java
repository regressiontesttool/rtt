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
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.RTTAnnotation;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

public class FieldElementAnnotationTests {

	private ClassModelFactory factory;
	private FieldElement field;

	@Before
	public void setUp() throws Exception {
		factory = ClassModelFactory.getFactory();
		field = factory.createFieldElement(null, "aField");
	}
	
	@Test
	public void testEmptyAnnotation() throws Exception {
		assertFalse("Annotation", field.hasAnnotation());
		assertNull("Annotation", field.getAnnotation());
	}
	
	@Test
	public void testSetNodeAnnotation() throws Exception {
		assertFalse("Adding a Node annotation should not be allowed, but was.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.NODE, field));
		assertFalse("Adding a Node annotation should not be possible, but was.",
				ControllerRegistry.execute(Mode.SET, 
				RTTAnnotation.create(AnnotationType.NODE), field));
		
		assertFalse(field.hasAnnotation());
		assertNull(field.getAnnotation());
	}
	
	@Test
	public void testSetValueAnnotation() throws Exception {
		assertTrue("Adding a Value annotation should be allowed, but was not.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.VALUE, field));
		assertTrue("Adding a Value annotation should be possible, but was not.",
				ControllerRegistry.execute(Mode.SET, 
						RTTAnnotation.create(AnnotationType.VALUE), field));
		
		assertTrue("Annotation was not set", field.hasAnnotation());
		assertEquals("Annotation", AnnotationType.VALUE, field.getAnnotation().getType());
	}
	
	@Test
	public void testSetInitializeAnnotation() throws Exception {
		assertFalse("Adding a Initialize annotation should not be allowed, but was.",
				ControllerRegistry.canExecute(Mode.SET, AnnotationType.INITIALIZE, field));
		assertFalse("Adding a Initialize annotation should not be possible, but was.",
				ControllerRegistry.execute(Mode.SET, 
						RTTAnnotation.create(AnnotationType.INITIALIZE), field));
		
		assertFalse(field.hasAnnotation());
		assertNull(field.getAnnotation());
	}
	
	@Test
	public void testSetValueAnnotationTwice() throws Exception {
		ControllerRegistry.execute(Mode.SET, RTTAnnotation.create(AnnotationType.VALUE), field);
		
		assertFalse(ControllerRegistry.canExecute(Mode.SET, AnnotationType.VALUE, field));
		assertFalse(ControllerRegistry.execute(Mode.SET, 
				RTTAnnotation.create(AnnotationType.VALUE), field));
	}

}
