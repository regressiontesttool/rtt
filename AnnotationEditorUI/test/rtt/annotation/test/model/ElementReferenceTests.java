package rtt.annotation.test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElementReference;
import rtt.annotation.editor.model.ClassModelFactory;

public class ElementReferenceTests {

	private static final String CLASSNAME = "TestClassName";
	private static final String PACKAGENAME = "TestPackageName";
	
	private ClassModelFactory factory;
	private ClassElementReference reference;	

	@Before
	public void setUp() throws Exception {
		this.factory = ClassModelFactory.getFactory();
		this.reference = ClassElementReference.create(CLASSNAME);
	}
	
	@Test
	public void testEmptyReference() throws Exception {
		assertEquals(CLASSNAME, reference.getName());
		assertFalse(reference.isResolved());
		assertNull(reference.getReference());
	}
	
	@Test
	public void testSetReference() throws Exception {
		reference.setReference(createModelElement());
		
		assertTrue(reference.isResolved());
		assertNotNull(reference.getReference());
		assertEquals(createModelElement(), reference.getReference());		
	}
	
	private ClassElement createModelElement() {
		return factory.createClassElement(
				null, CLASSNAME, PACKAGENAME);
	}
	
	@Test
	public void testEquals() throws Exception {
		ClassElementReference reference2 = 
				ClassElementReference.create(CLASSNAME);
		
		assertTrue(reference.equals(reference2));
		assertTrue(reference2.equals(reference));
		
		reference.setReference(createModelElement());
		assertFalse(reference.equals(reference2));
		assertFalse(reference2.equals(reference));
		
		reference2.setReference(createModelElement());
		assertTrue(reference.equals(reference2));
		assertTrue(reference2.equals(reference));
	}
	
	

}
