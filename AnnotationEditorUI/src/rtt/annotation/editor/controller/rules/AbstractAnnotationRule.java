package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

public abstract class AbstractAnnotationRule<T extends Annotatable<?>> implements IAnnotationRule<T> {
	
	@Override
	public final boolean canExecute(Mode mode, AnnotationType type, T element) {
		if (element == null) {
			throw new IllegalArgumentException("The given object was null.");
		}
		
		return checkRule(mode, type, element);
	}

	protected abstract boolean checkRule(Mode mode, AnnotationType type, T element);
}
