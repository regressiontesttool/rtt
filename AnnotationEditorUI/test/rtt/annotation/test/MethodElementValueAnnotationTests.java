package rtt.annotation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotation.editor.model.annotation.InitAnnotation;
import rtt.annotation.editor.model.annotation.NodeAnnotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;

public class MethodElementValueAnnotationTests {
	
	private ClassModelFactory factory;
	private MethodElement<ValueAnnotation> method;

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
		TestUtils.assertCanNotExecute(Mode.SET, NodeAnnotation.class, method);
		TestUtils.assertNotExecutes(Mode.SET, NodeAnnotation.class, method);
		
		assertFalse(method.hasAnnotation());
		assertNull(method.getAnnotation());
	}
	
	@Test
	public void testSetValueAnnotation() throws Exception {
		TestUtils.assertCanExecute(Mode.SET, ValueAnnotation.class, method);
		TestUtils.assertExecutes(Mode.SET, ValueAnnotation.class, method);
		
		assertTrue("Annotation was not set", method.hasAnnotation());
		assertEquals("Annotation", AnnotationType.VALUE, method.getAnnotation().getType());
	}
	
	@Test
	public void testSetInitializeAnnotation() throws Exception {
		TestUtils.assertCanNotExecute(Mode.SET, InitAnnotation.class, method);
		TestUtils.assertNotExecutes(Mode.SET, InitAnnotation.class, method);
		
		assertFalse(method.hasAnnotation());
		assertNull(method.getAnnotation());
	}
	
	@Test
	public void testSetValueAnnotationTwice() throws Exception {
		TestUtils.assertExecutes(Mode.SET, ValueAnnotation.class, method);
		
		TestUtils.assertCanNotExecute(Mode.SET, ValueAnnotation.class, method);
		TestUtils.assertNotExecutes(Mode.SET, ValueAnnotation.class, method);
	}

}
