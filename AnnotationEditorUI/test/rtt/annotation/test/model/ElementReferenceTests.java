package rtt.annotation.test.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.model.ElementReference;
import rtt.annotation.editor.model.ModelElement;

public class ElementReferenceTests {

	private static final String NAME = "TestReferenceName";
	
	private ElementReference<ModelElement<?>> reference;

	@Before
	public void setUp() throws Exception {
		this.reference = new ElementReference<ModelElement<?>>(NAME);
	}
	
	@Test
	public void testEmptyReference() throws Exception {
		assertEquals(NAME, reference.getName());
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
	
	private ModelElement<?> createModelElement() {
		return new ModelElement<ModelElement<?>>(null) {};
	}
	
	@Test
	public void testEquals() throws Exception {
		ElementReference<ModelElement<?>> reference2 = 
				new ElementReference<ModelElement<?>>(NAME);
		
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
