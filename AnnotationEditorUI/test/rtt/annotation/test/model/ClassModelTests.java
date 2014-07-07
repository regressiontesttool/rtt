package rtt.annotation.test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;

public class ClassModelTests {
	
	private static final String PACKAGE1 = "package1";
	private static final String PACKAGE2 = "package2";
	
	private static final String CLASS1 = "class1";
	private static final String CLASS2 = "class2";
	
	private ClassModelFactory factory;
	private ClassModel classModel;

	@Before
	public void setUp() throws Exception {
		factory = ClassModelFactory.getFactory();
		classModel = factory.createClassModel();
	}
	
	@Test
	public void testEmptyClassModel() throws Exception {
		assertNotNull(classModel.getPackages());
		assertEquals(classModel.getPackages(), Collections.EMPTY_SET);
		
		assertNotNull(classModel.getClassElements());
		assertEquals(classModel.getClassElements(), Collections.EMPTY_MAP);
		
		assertNull(classModel.getClasses(PACKAGE1));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testAddNull() throws Exception {
		classModel.addClassElement(null);
	}
	
	@Test
	public void testAddOneClassElement() {
		ClassElement classElement = createClassElement(CLASS1, PACKAGE1);
		classModel.addClassElement(classElement);
		
		assertTrue(checkPackagesSize(1));
		assertTrue(checkClassesSize(PACKAGE1, 1));
		
		assertTrue(containsClasses(PACKAGE1, classElement));
	}
	
	@Test
	public void testAddTwoClassesSamePackage() throws Exception {
		ClassElement classElement1 = createClassElement(CLASS1, PACKAGE1);
		classModel.addClassElement(classElement1);
		ClassElement classElement2 = createClassElement(CLASS2, PACKAGE1);
		classModel.addClassElement(classElement2);
		
		assertTrue(checkPackagesSize(1));
		assertTrue(checkClassesSize(PACKAGE1, 2));
		
		assertTrue(containsClasses(PACKAGE1, classElement1, classElement2));
	}
	
	@Test
	public void testAddTwoClassesDifferentPackage() throws Exception {
		ClassElement classElement1 = createClassElement(CLASS1, PACKAGE1);
		classModel.addClassElement(classElement1);
		ClassElement classElement2 = createClassElement(CLASS1, PACKAGE2);
		classModel.addClassElement(classElement2);
		
		assertTrue(checkPackagesSize(2));
		assertTrue(checkClassesSize(PACKAGE1, 1));
		assertTrue(checkClassesSize(PACKAGE2, 1));
		
		assertTrue(containsClasses(PACKAGE1, classElement1));
		assertTrue(containsClasses(PACKAGE2, classElement2));
	}
	
	private ClassElement createClassElement(String name, String packageName) {
		ClassElement result = factory.createClassElement(classModel);
		result.setName(name);
		result.setPackageName(packageName);
		
		return result;
	}
	
	private boolean checkPackagesSize(int size) {
		return !classModel.getPackages().isEmpty() 
				&& classModel.getPackages().size() == size;		
	}
	
	private boolean checkClassesSize(String packageName, int size) {
		List<ClassElement> classes = classModel.getClasses(PACKAGE1);
		return classes != null && !classes.isEmpty() 
				&& classes.size() == size;
	}
	
	private boolean containsClasses(String packageName, ClassElement... classes) {
		List<ClassElement> classList = classModel.getClasses(packageName);
		for (ClassElement classElement : classes) {
			if (!classList.contains(classElement)) {
				return false;
			}
		}
		
		return true;
	}
}
