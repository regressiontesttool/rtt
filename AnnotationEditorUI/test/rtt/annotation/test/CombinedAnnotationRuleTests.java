package rtt.annotation.test;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.controller.rules.CombinedAnnotationRule;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModelFactory;

public class CombinedAnnotationRuleTests {

	private CombinedAnnotationRule<ClassElement> rule;
	private ClassModelFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = ClassModelFactory.getFactory();
		rule = new CombinedAnnotationRule<ClassElement>();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullElement() throws Exception {
		rule.isAllowed(Annotation.NODE, null);
	}

	@Test
	public void testEmptyRule() throws Exception {
		assertFalse("Rule should be empty.", rule.hasElements());
		assertFalse("No rules were added, but has to retrun false.", 
				rule.isAllowed(Annotation.NODE, factory.createClassElement(null)));
	}
	
//	@Test
//	public void testAddRule() throws Exception {
//		rule.addRule(new ClassElementAnnotationController.ClassElementNodeRule());
//		assertTrue("A rule should be available", rule.hasElements());
//	}
//	
//	@Test
//	public void testSimpleRule() throws Exception {
//		rule.addRule(new ClassElementAnnotationController.ClassElementNodeRule());
//		assertTrue("NODE annotation should be allowed to add.", 
//				rule.isAllowed(Annotation.NODE, factory.createClassElement(null)));
//	}

}
