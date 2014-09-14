package rtt.annotation.test.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ModelElement;
import rtt.annotation.editor.model.RTTAnnotation;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

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
		assertNull(annotatable.getAnnotation());		
	}
	
	@Test
	public void testNodeAnnotation() throws Exception {
		annotatable.setAnnotation(RTTAnnotation.create(AnnotationType.NODE));
		assertTrue(annotatable.hasAnnotation());
		assertEquals(AnnotationType.NODE, annotatable.getAnnotation().getType());
	}
	
	@Test
	public void testCompareAnnotation() throws Exception {
		annotatable.setAnnotation(RTTAnnotation.create(AnnotationType.VALUE));
		assertTrue(annotatable.hasAnnotation());
		assertEquals(AnnotationType.VALUE, annotatable.getAnnotation().getType());
	}
	
	@Test
	public void testInformationalAnnotation() throws Exception {
		annotatable.setAnnotation(RTTAnnotation.create(AnnotationType.INITIALIZE));
		assertTrue(annotatable.hasAnnotation());
		assertEquals(AnnotationType.INITIALIZE, annotatable.getAnnotation().getType());
	}
	
	@Test
	public void testUnsetAnnotation() throws Exception {
		annotatable.setAnnotation(RTTAnnotation.create(AnnotationType.NODE));
		annotatable.setAnnotation(null);
		assertFalse(annotatable.hasAnnotation());
		assertNull(annotatable.getAnnotation());
	}	
}
