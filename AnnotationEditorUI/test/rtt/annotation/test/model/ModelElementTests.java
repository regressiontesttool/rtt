package rtt.annotation.test.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.model.ModelElement;

public class ModelElementTests {
	
	private static final class TestModelElement extends ModelElement<ModelElement<?>> {

		protected TestModelElement(ModelElement<?> parent) {
			super(parent);
		}		
	}
	
	private String elementName = "element";
	private String parentName = "parent";
	
	private ModelElement<?> element;
	private ModelElement<?> parent;	
	
	@Before
	public void setUp() throws Exception { 
		parent = new TestModelElement(null);
		parent.setName(parentName);
		
		element = new TestModelElement(parent);
		element.setName(elementName);
	}

	@Test
	public void testParent() throws Exception {
		assertNull("Parent of parent element should be null.", parent.getParent());
		assertEquals("Parent of element", parent, element.getParent());		
	}
	
	@Test
	public void testNames() throws Exception {
		assertEquals("Element name", elementName, element.getName());
		assertEquals("Parent name", parentName, element.getParent().getName());
	}
	
	@Test
	public void testEquals() throws Exception {
		ModelElement<?> secondElement = new TestModelElement(parent);
		assertEquals("Parent element", element.getParent(), secondElement.getParent());
		
		secondElement.setName(elementName);
		assertEquals("Element name", element.getName(), secondElement.getName());
		
		assertTrue("Equals don't return true.", element.equals(secondElement));
		assertTrue("Equals don't return true.", secondElement.equals(element));
	}
}
