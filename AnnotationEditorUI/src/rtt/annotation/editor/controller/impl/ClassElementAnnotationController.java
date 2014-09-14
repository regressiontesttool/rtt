package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.NoAnnotationRule;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.RTTAnnotation;

public class ClassElementAnnotationController extends RuledAnnotationController<ClassElement> {
	
	public ClassElementAnnotationController() {
		setRule(new NoAnnotationRule<ClassElement>());
	}
	
	@Override
	public boolean execute(Mode mode, RTTAnnotation annotation, ClassElement element) {
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
