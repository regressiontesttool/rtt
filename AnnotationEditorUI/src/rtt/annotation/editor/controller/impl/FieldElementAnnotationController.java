package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.Annotation.AnnotationType;
import rtt.annotation.editor.model.FieldElement;

public class FieldElementAnnotationController extends RuledAnnotationController<FieldElement> {
	
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
		super(new FieldElementRule());
	}
}