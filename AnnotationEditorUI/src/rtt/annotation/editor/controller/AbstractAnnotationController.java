package rtt.annotation.editor.controller;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public abstract class AbstractAnnotationController<A extends Annotation, T extends Annotatable<A>> 
	implements IAnnotationController<A, T>{

	@Override
	public final boolean canExecute(Mode mode, Class<? extends Annotation> annotation, T element) {
		if (element == null) {
			throw new IllegalArgumentException("Element was null.");
		}
		
		switch (mode) {
		case SET:
			return canSetAnnotation(annotation, element);				
		case UNSET:
			return canUnsetAnnotation(annotation, element);
		default:
			throw new IllegalArgumentException("Unknown mode '" + mode + "'");
		}
	}	

	protected abstract boolean canSetAnnotation(Class<? extends Annotation> annotation, T element);	
	protected abstract boolean canUnsetAnnotation(Class<? extends Annotation> annotation, T element);

	@Override
	public final boolean execute(Mode mode, A annotation, T element) {
		if (element == null) {
			throw new IllegalArgumentException("Element was null.");
		}
		
		switch (mode) {
		case SET:
			return setAnnotation(annotation, element);				
		case UNSET:
			return unsetAnnotation(annotation, element);
		default:
			throw new IllegalArgumentException("Unknown mode '" + mode + "'");
		}
	}

	protected boolean setAnnotation(A annotation, T element) {
		element.setAnnotation(annotation);
		return true;
	}
	
	protected boolean unsetAnnotation(A annotation, T element) {
		element.setAnnotation(null);
		return true;
	}

}
