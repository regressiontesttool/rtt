package rtt.annotation.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotation.editor.model.annotation.InitAnnotation;
import rtt.annotation.editor.model.annotation.NodeAnnotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;

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
		TestUtils.assertCanExecute(Mode.SET, NodeAnnotation.class, classElement);
		TestUtils.assertExecutes(Mode.SET, NodeAnnotation.class, classElement);
		
		assertTrue("Annotation was not set", classElement.hasAnnotation());
		assertEquals("Annotation", AnnotationType.NODE, classElement.getAnnotation().getType());
	}
	
	@Test
	public void testSetValueAnnotation() throws Exception {
		TestUtils.assertCanNotExecute(Mode.SET, ValueAnnotation.class, classElement);
		TestUtils.assertNotExecutes(Mode.SET, ValueAnnotation.class, classElement);
		
		assertFalse("Annotation was set", classElement.hasAnnotation());
	}
	
	@Test
	public void testSetInitAnnotation() throws Exception {
		TestUtils.assertCanNotExecute(Mode.SET, InitAnnotation.class, classElement);
		TestUtils.assertNotExecutes(Mode.SET, InitAnnotation.class, classElement);
		
		assertFalse("Annotation was set", classElement.hasAnnotation());
	}
	
	@Test
	public void testSetNodeAnnotationTwice() throws Exception {
		TestUtils.assertExecutes(Mode.SET, NodeAnnotation.class, classElement);
		
		TestUtils.assertCanNotExecute(Mode.SET, NodeAnnotation.class, classElement);
		TestUtils.assertNotExecutes(Mode.SET, NodeAnnotation.class, classElement);
	}

}
