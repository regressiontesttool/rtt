package rtt.annotation.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.controller.IAnnotationController;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.rules.Annotation;

public class ClassElementAnnotationTests {

	private ClassModelFactory factory;
	private ClassElement element;

	@Before
	public void setUp() throws Exception {
		factory = ClassModelFactory.getFactory();
		element = factory.createClassElement(null);
	}
	
	@Test
	public void testEmptyAnnotation() throws Exception {
		assertFalse("Annotation", element.hasAnnotation());
		assertNull("Annotation", element.getAnnotation());
	}
	
	@Test
	public void testSetAnnotation() throws Exception {
//		IAnnotationController<ClassElement> controller = ControllerRegistry.INSTANCE.findController(ClassElement.class);
//		assertNotNull("Could not find annotation controller.", controller);
//		
//		assertTrue("Could not set annotation.", controller.setAnnotation(Annotation.NODE, element));
//		
//		assertTrue("Annotation was not set", element.hasAnnotation());
//		assertEquals("Annotation", Annotation.NODE, element.getAnnotation());
	}

}
