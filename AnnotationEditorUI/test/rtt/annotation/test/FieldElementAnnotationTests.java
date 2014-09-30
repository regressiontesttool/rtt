package rtt.annotation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotation.editor.model.annotation.InitAnnotation;
import rtt.annotation.editor.model.annotation.NodeAnnotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;

public class FieldElementAnnotationTests {

	private ClassModelFactory factory;
	private FieldElement<ValueAnnotation> field;

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
		TestUtils.assertCanNotExecute(Mode.SET, NodeAnnotation.class, field);
		TestUtils.assertNotExecutes(Mode.SET, NodeAnnotation.class, field);
		
		assertFalse(field.hasAnnotation());
		assertNull(field.getAnnotation());
	}
	
	@Test
	public void testSetValueAnnotation() throws Exception {
		TestUtils.assertCanExecute(Mode.SET, ValueAnnotation.class, field);
		TestUtils.assertExecutes(Mode.SET, ValueAnnotation.class, field);
		
		assertTrue("Annotation was not set", field.hasAnnotation());
		assertEquals("Annotation", AnnotationType.VALUE, field.getAnnotation().getType());
	}
	
	@Test
	public void testSetInitializeAnnotation() throws Exception {
		TestUtils.assertCanNotExecute(Mode.SET, InitAnnotation.class, field);
		TestUtils.assertNotExecutes(Mode.SET, InitAnnotation.class, field);
		
		assertFalse(field.hasAnnotation());
		assertNull(field.getAnnotation());
	}
	
	@Test
	public void testSetValueAnnotationTwice() throws Exception {
		TestUtils.assertExecutes(Mode.SET, ValueAnnotation.class, field);
		
		TestUtils.assertCanNotExecute(Mode.SET, ValueAnnotation.class, field);
		TestUtils.assertNotExecutes(Mode.SET, ValueAnnotation.class, field);
	}

}
