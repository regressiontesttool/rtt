package rtt.annotation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotation.editor.model.annotation.NodeAnnotation;

public class ClassElementAnnotationTests {

	private static final String CLASSNAME = "aClassName";
	private static final String PACKAGENAME = "aPackageName";
	
	private ClassModelFactory factory;
	private ClassElement classElement;

	@Before
	public void setUp() throws Exception {
		factory = ClassModelFactory.getFactory();
		classElement = factory.createClassElement(null, CLASSNAME, PACKAGENAME);
	}
	
	@Test
	public void testEmptyAnnotation() throws Exception {
		assertFalse("Annotation", classElement.hasAnnotation());
		assertNull("Annotation", classElement.getAnnotation());
	}
	
	@Test
	public void testSetNodeAnnotation() throws Exception {
		assertTrue("Adding a Node annotation should be allowed, but was not.",
				ControllerRegistry.canExecute(Mode.SET, NodeAnnotation.class, classElement));
		assertTrue("Adding a Node annotation should be possible, but was not.",
				ControllerRegistry.execute(Mode.SET, Annotation.create(NodeAnnotation.class), classElement));
		
		assertTrue("Annotation was not set", classElement.hasAnnotation());
		assertEquals("Annotation", AnnotationType.NODE, classElement.getAnnotation().getType());
	}
	
	@Test
	public void testSetNodeAnnotationTwice() throws Exception {
		ControllerRegistry.execute(Mode.SET, Annotation.create(NodeAnnotation.class), classElement);
		
		assertFalse(ControllerRegistry.canExecute(Mode.SET, NodeAnnotation.class, classElement));
		assertFalse(ControllerRegistry.execute(Mode.SET, Annotation.create(NodeAnnotation.class), classElement));
	}

}
