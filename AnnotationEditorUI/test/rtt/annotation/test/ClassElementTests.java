package rtt.annotation.test;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class ClassElementTests {

	private ClassElement element;
	private ClassModelFactory factory;
	
	@Before
	public void setUp() throws Exception { 
		factory = ClassModelFactory.getFactory();
		element = factory.createClassElement();		
	}
	
	@Test
	public void testEmptyNames() throws Exception {
		assertEquals("Class name", null, element.getName());
		assertEquals("Package name", null, element.getPackageName());
	}
	
	@Test
	public void testEmptyFields() throws Exception {
		List<? extends Annotatable<?>> fields = element.getFields();
		assertNotNull("Field list", fields);
		assertEquals("Field size", 0, fields.size());
	}
	
	@Test
	public void testEmtpyMethods() throws Exception {
		List<? extends Annotatable<?>> methods = element.getMethods();
		assertNotNull("Method list", methods);
		assertEquals("Method size", 0, methods.size());
	}
	
	@Test
	public void testSetName() throws Exception {
		String className = "TestClassName";
		element.setName(className);
		
		assertEquals("Class name", className, element.getName());
	}
	
	@Test
	public void testSetPackageName() throws Exception {
		String packageName = "abc.def.ghi";
		element.setPackageName(packageName);
		
		assertEquals("Package name", packageName, element.getPackageName());
	}
	
	@Test
	public void testAddField() throws Exception {
		String fieldName = "TestFieldName";
		
		FieldElement field = factory.createFieldElement();
		field.setName(fieldName);
		
		int oldSize = element.getFields().size();		
		element.addField(field);
		int newSize = element.getFields().size();
		assertEquals("Field list size", oldSize + 1, newSize);
		
		assertTrue("Field list contains", element.getFields().contains(field));
		
		FieldElement field2 = factory.createFieldElement();
		field2.setName(fieldName);
		
		oldSize = newSize;
		element.addField(field2);
		newSize = element.getFields().size();
		assertEquals("Field list size", oldSize, newSize);
	}
	
	@Test
	public void testAddMethod() throws Exception {
		String methodName = "TestMethodName";
		
		MethodElement method = factory.createMethodElement();
		method.setName(methodName);
		
		int oldSize = element.getMethods().size();
		element.addMethod(method);
		int newSize = element.getMethods().size();
		assertEquals("Method list size", oldSize + 1, newSize);
		
		assertTrue("Method List contains", element.getMethods().contains(method));
		
		MethodElement method2 = factory.createMethodElement();
		method2.setName(methodName);
		
		oldSize = newSize;
		element.addMethod(method2);
		newSize = element.getMethods().size();
		assertEquals("Method list size", oldSize, newSize);
	}
}
