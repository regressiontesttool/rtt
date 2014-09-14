package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

public class NoAnnotationRule<T extends Annotatable<?>> extends AbstractAnnotationRule<T> {

	@Override
	protected boolean checkRule(Mode mode, AnnotationType type, T element) {
		switch(mode) {
		case SET:
			return !element.hasAnnotation();
		case UNSET:
			return element.hasAnnotation();
		default:
			throw new RuntimeException("Unknown mode '" + mode + "'");
		
		}
	}
}
