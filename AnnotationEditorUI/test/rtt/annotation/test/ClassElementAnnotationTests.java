package rtt.annotation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElementReference;
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
		assertTrue("Annotation was not type NODE.", classElement.hasAnnotation(AnnotationType.NODE));
	}
	
	@Test
	public void testSetValueAnnotation() throws Exception {
		TestUtils.assertCanNotExecute(Mode.SET, ValueAnnotation.class, classElement);
		TestUtils.assertNotExecutes(Mode.SET, ValueAnnotation.class, classElement);
		
		assertFalse("Annotation was set", classElement.hasAnnotation());
		assertFalse("Annotation was set", classElement.hasAnnotation(AnnotationType.VALUE));
	}
	
	@Test
	public void testSetInitAnnotation() throws Exception {
		TestUtils.assertCanNotExecute(Mode.SET, InitAnnotation.class, classElement);
		TestUtils.assertNotExecutes(Mode.SET, InitAnnotation.class, classElement);
		
		assertFalse("Annotation was set", classElement.hasAnnotation());
		assertFalse("Annotation was set", classElement.hasAnnotation(AnnotationType.INITIALIZE));
	}
	
	@Test
	public void testSetNodeAnnotationTwice() throws Exception {
		TestUtils.assertExecutes(Mode.SET, NodeAnnotation.class, classElement);
		
		TestUtils.assertCanNotExecute(Mode.SET, NodeAnnotation.class, classElement);
		TestUtils.assertNotExecutes(Mode.SET, NodeAnnotation.class, classElement);
		
		assertTrue("Annotation was not type NODE.", classElement.hasAnnotation(AnnotationType.NODE));
	}
	
	@Test
	public void testSuperClassAnnotation() throws Exception {
		TestUtils.assertExecutes(Mode.SET, NodeAnnotation.class, classElement);
		
		ClassElement extendingClass = factory.createClassElement(null, "anOtherClassname", "anOtherPackageName");
		ClassElementReference ref = ClassElementReference.create(CLASSNAME);
		ref.setReference(classElement);
		
		extendingClass.setSuperClass(ref);
		
		assertFalse("Extending class should not have own annotation", 
				extendingClass.hasAnnotation());
		assertTrue("Extending class does not extend annotation.", 
				extendingClass.hasExtendedAnnotation());
	}
	
	@Test
	public void testInterfaceAnnotation() throws Exception {
		TestUtils.assertExecutes(Mode.SET, NodeAnnotation.class, classElement);
		
		ClassElement implementingClass = factory.createClassElement(null, "anOtherClassname", "anOtherPackageName");
		List<ClassElementReference> interfaceRefs = ClassElementReference.create(
				"interfaceA", "interfaceB");
		
		interfaceRefs.get(0).setReference(classElement);
		
		implementingClass.setInterfaces(interfaceRefs);
		
		assertFalse("Implementing class should not have own annotation", 
				implementingClass.hasAnnotation());
		assertTrue("Implementing class does not extend annotation.", 
				implementingClass.hasExtendedAnnotation());
	}

}
