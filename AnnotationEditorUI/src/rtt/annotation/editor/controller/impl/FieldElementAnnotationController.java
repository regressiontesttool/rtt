package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.NoAnnotationRule;
import rtt.annotation.editor.controller.rules.RTTAnnotation;
import rtt.annotation.editor.model.FieldElement;

public final class FieldElementAnnotationController extends RuledAnnotationController<FieldElement> {
	
	public FieldElementAnnotationController() {
		setRule(new NoAnnotationRule<FieldElement>());
	}
	
	@Override
	public boolean execute(Mode mode, RTTAnnotation annotation, FieldElement element) {
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