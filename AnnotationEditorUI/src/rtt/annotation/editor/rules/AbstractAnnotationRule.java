package rtt.annotation.editor.rules;

import rtt.annotation.editor.model.Annotatable;

public abstract class AbstractAnnotationRule<T extends Annotatable<?>> implements IAnnotationRule<T> {

	@Override
	public final boolean isAllowed(Annotation annotation, T element) {
		if (element == null) {
			throw new IllegalArgumentException("The given object was null.");
		}
		
		return checkRule(annotation, element);
	}

	protected abstract boolean checkRule(Annotation annotation, T element);
}
