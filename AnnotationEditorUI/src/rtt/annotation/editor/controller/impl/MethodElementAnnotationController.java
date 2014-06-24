package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.controller.rules.NoAnnotationRule;
import rtt.annotation.editor.model.MethodElement;

public final class MethodElementAnnotationController extends RuledAnnotationController<MethodElement> {
	
	public MethodElementAnnotationController() {
		setRule(new NoAnnotationRule<MethodElement>());
	}

	@Override
	public boolean setAnnotation(Annotation annotation,
			MethodElement element) {
		element.setAnnotation(annotation);
		return true;
	}
}