package rtt.annotation.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import rtt.annotation.editor.controller.ControllerRegistry;
import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public class TestUtils {
	
	public static void assertCanExecute(Mode mode, 
			Class<? extends Annotation> annotationType, 
			Annotatable<?> annotatable) {
		
		String errorMessage = "Adding a '" + annotationType.getSimpleName() 
				+ "' should be allowed, but was not.";
		
		assertTrue(errorMessage, ControllerRegistry.canExecute(
				mode, annotationType, annotatable));
	}
	
	public static void assertCanNotExecute(Mode mode, 
			Class<? extends Annotation> annotationType, 
			Annotatable<?> annotatable) {
		
		String errorMessage = "Adding a '" + annotationType.getSimpleName() 
				+ "' should not be allowed, but was.";
		
		assertFalse(errorMessage, ControllerRegistry.canExecute(
				mode, annotationType, annotatable));
	}
	
	
	public static void assertExecutes(Mode mode,
			Class<? extends Annotation> annotationType,
			Annotatable<?> annotatable) {
		
		String errorMessage = "Adding a '" + annotationType.getSimpleName() 
				+ "' should be possible, but was not.";
		
		assertTrue(errorMessage, ControllerRegistry.execute(
				mode, annotationType, annotatable));
	}
	
	public static void assertNotExecutes(Mode mode,
			Class<? extends Annotation> annotationType,
			Annotatable<?> annotatable) {
		
		String errorMessage = "Adding a '" + annotationType.getSimpleName() 
				+ "' should be not possible, but was.";
		
		assertFalse(errorMessage, ControllerRegistry.execute(
				mode, annotationType, annotatable));
	}

}
