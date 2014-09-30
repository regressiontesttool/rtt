package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public abstract class AbstractAnnotationRule<T extends Annotatable<?>> 
	implements IAnnotationRule<T> {
	
	@Override
	public final boolean canSet(Class<? extends Annotation> annotation, T element) {
		if (element == null) {
			throw new IllegalArgumentException("The given object was null.");
		}
		return checkSet(annotation, element);
	}
	
	protected boolean checkSet(Class<? extends Annotation> annotation, T element) {
		return !element.hasAnnotation();
	}

	@Override
	public final boolean canUnset(Class<? extends Annotation> annotation, T element) {
		if (element == null) {
			throw new IllegalArgumentException("The given object was null.");
		}
		return checkUnset(annotation, element);
	}

	protected boolean checkUnset(Class<? extends Annotation> annotation, T element) {
		return element.hasAnnotation();
	}
}
