package rtt.annotation.editor.rules;

import rtt.annotation.editor.model.Annotatable;

public abstract class NodeAnnotationRule<T extends Annotatable<?>> implements IAnnotationRule<T> {
	
	@Override
	public boolean isAllowed(Annotation annotation, T element) {
		if (annotation == Annotation.NODE) {
			return isNodeAllowed(element);
		}
		
		return false;
	}

	protected abstract boolean isNodeAllowed(T element);

}
