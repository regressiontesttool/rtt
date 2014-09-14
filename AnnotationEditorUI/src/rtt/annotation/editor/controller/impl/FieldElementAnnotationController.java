package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.Annotation;
import rtt.annotation.editor.model.Annotation.AnnotationType;

public final class FieldElementAnnotationController extends RuledAnnotationController<FieldElement> {
	
	private static class FieldElementRule extends AbstractAnnotationRule<FieldElement> {

		@Override
		protected boolean checkSet(AnnotationType type, FieldElement element) {
			return type == AnnotationType.VALUE && !element.hasAnnotation();
		}

		@Override
		protected boolean checkUnset(AnnotationType type, FieldElement element) {
			return element.hasAnnotation();
		}
		
	}
	
	public FieldElementAnnotationController() {
		setRule(new FieldElementRule());
	}
	
	@Override
	public boolean execute(Mode mode, Annotation annotation, FieldElement element) {
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