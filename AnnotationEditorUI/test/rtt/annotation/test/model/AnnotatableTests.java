package rtt.annotation.test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotation.editor.model.annotation.InitAnnotation;
import rtt.annotation.editor.model.annotation.NodeAnnotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;

public class AnnotatableTests {
	
	Annotatable<Annotation> annotatable;

	@Before
	public void setUp() throws Exception {
		annotatable = new Annotatable<Annotation>(null) {};
	}

	@Test
	public void testEmptyAnnotatable() {
		assertFalse(annotatable.hasAnnotation());
		assertNull(annotatable.getAnnotation());		
	}
	
	@Test
	public void testNodeAnnotation() throws Exception {
		annotatable.setAnnotation(Annotation.create(NodeAnnotation.class));
		assertTrue(annotatable.hasAnnotation());
		assertEquals(AnnotationType.NODE, annotatable.getAnnotation().getType());
	}
	
	@Test
	public void testCompareAnnotation() throws Exception {
		annotatable.setAnnotation(Annotation.create(ValueAnnotation.class));
		assertTrue(annotatable.hasAnnotation());
		assertEquals(AnnotationType.VALUE, annotatable.getAnnotation().getType());
	}
	
	@Test
	public void testInformationalAnnotation() throws Exception {
		annotatable.setAnnotation(Annotation.create(InitAnnotation.class));
		assertTrue(annotatable.hasAnnotation());
		assertEquals(AnnotationType.INITIALIZE, annotatable.getAnnotation().getType());
	}
	
	@Test
	public void testUnsetAnnotation() throws Exception {
		annotatable.setAnnotation(Annotation.create(NodeAnnotation.class));
		annotatable.setAnnotation(null);
		assertFalse(annotatable.hasAnnotation());
		assertNull(annotatable.getAnnotation());
	}	
}
