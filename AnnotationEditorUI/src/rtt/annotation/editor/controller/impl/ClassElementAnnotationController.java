package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.Annotation;
import rtt.annotation.editor.model.Annotation.AnnotationType;

public class ClassElementAnnotationController extends RuledAnnotationController<ClassElement> {
	
	private static class ClassElementRule extends AbstractAnnotationRule<ClassElement> {
		
		@Override
		protected boolean checkSet(AnnotationType type, ClassElement element) {
			return type == AnnotationType.NODE && !element.hasAnnotation();
		}
		
		@Override
		protected boolean checkUnset(AnnotationType type, ClassElement element) {
			return element.hasAnnotation();
		}
	}
	
	public ClassElementAnnotationController() {
		setRule(new ClassElementRule());
	}
	
	@Override
	public boolean execute(Mode mode, Annotation annotation, ClassElement element) {
		switch (mode) {
		case SET:
			element.setAnnotation(annotation);
			return true;
		case UNSET:
			element.setAnnotation(null);
			return true;
		default:
			throw new RuntimeException("Unknown mode '" + mode + "'");
		}
	}	
}
