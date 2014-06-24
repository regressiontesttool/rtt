package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.controller.rules.NoAnnotationRule;
import rtt.annotation.editor.model.FieldElement;

public final class FieldElementAnnotationController extends RuledAnnotationController<FieldElement> {
	
	public FieldElementAnnotationController() {
		setRule(new NoAnnotationRule<FieldElement>());
	}

	@Override
	public boolean setAnnotation(Annotation annotation, FieldElement element) {
		element.setAnnotation(annotation);
		return true;
	}
}