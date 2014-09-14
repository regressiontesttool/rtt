package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.NoAnnotationRule;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.RTTAnnotation;

public final class MethodElementAnnotationController extends RuledAnnotationController<MethodElement> {
	
	public MethodElementAnnotationController() {
		setRule(new NoAnnotationRule<MethodElement>());
	}

	@Override
	public boolean execute(Mode mode, RTTAnnotation annotation, MethodElement element) {
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