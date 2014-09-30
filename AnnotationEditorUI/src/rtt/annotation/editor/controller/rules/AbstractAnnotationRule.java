package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;

public abstract class AbstractAnnotationRule<T extends Annotatable> 
	implements IAnnotationRule<T> {
	
	@Override
	public final boolean canSet(AnnotationType type, T element) {
		if (element == null) {
			throw new IllegalArgumentException("The given object was null.");
		}
		return checkSet(type, element);
	}
	
	protected abstract boolean checkSet(AnnotationType type, T element);

	@Override
	public final boolean canUnset(AnnotationType type, T element) {
		if (element == null) {
			throw new IllegalArgumentException("The given object was null.");
		}
		return checkUnset(type, element);
	}

	protected abstract boolean checkUnset(AnnotationType type, T element);
}
