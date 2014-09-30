package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.Annotation.AnnotationType;
import rtt.annotation.editor.model.ClassElement;

public class ClassElementAnnotationController extends RuledAnnotationController<ClassElement> {
	
	private static class ClassElementRule extends AbstractAnnotationRule<ClassElement> {
		
		@Override
		protected boolean checkSet(AnnotationType type, ClassElement element) {
			return type == AnnotationType.NODE && !element.hasAnnotation(AnnotationType.NODE);
		}
		
		@Override
		protected boolean checkUnset(AnnotationType type, ClassElement element) {
			return element.hasAnnotation(type);
		}
	}
	
	public ClassElementAnnotationController() {
		super(new ClassElementRule());
	}
}
