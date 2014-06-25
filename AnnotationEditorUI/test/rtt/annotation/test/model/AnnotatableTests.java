package rtt.annotation.test.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ModelElement;

public class AnnotatableTests {
	
	private static final class TestAnnotatable extends Annotatable<ModelElement<?>> {

		protected TestAnnotatable(ModelElement<?> parent) {
			super(parent);
		}		
	}
	
	TestAnnotatable annotatable;

	@Before
	public void setUp() throws Exception {
		annotatable = new TestAnnotatable(null);
	}

	@Test
	public void testEmptyAnnotatable() {
		assertFalse(annotatable.hasAnnotation());
		assertEquals(Annotation.NONE, annotatable.getAnnotation());		
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullAnnotation() throws Exception {
		annotatable.setAnnotation(null);
	}
	
	@Test
	public void testNodeAnnotation() throws Exception {
		annotatable.setAnnotation(Annotation.NODE);
		assertTrue(annotatable.hasAnnotation());
		assertEquals(Annotation.NODE, annotatable.getAnnotation());
	}
	
	@Test
	public void testCompareAnnotation() throws Exception {
		annotatable.setAnnotation(Annotation.COMPARE);
		assertTrue(annotatable.hasAnnotation());
		assertEquals(Annotation.COMPARE, annotatable.getAnnotation());
	}
	
	@Test
	public void testInformationalAnnotation() throws Exception {
		annotatable.setAnnotation(Annotation.INFORMATIONAL);
		assertTrue(annotatable.hasAnnotation());
		assertEquals(Annotation.INFORMATIONAL, annotatable.getAnnotation());
	}
	
	@Test
	public void testUnsetAnnotation() throws Exception {
		annotatable.setAnnotation(Annotation.NODE);
		annotatable.setAnnotation(Annotation.NONE);
		assertFalse(annotatable.hasAnnotation());
		assertEquals(Annotation.NONE, annotatable.getAnnotation());
	}	
}
