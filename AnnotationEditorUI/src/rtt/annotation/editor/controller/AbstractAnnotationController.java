package rtt.annotation.editor.controller;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.Annotation;
import rtt.annotation.editor.model.Annotation.AnnotationType;

public abstract class AbstractAnnotationController<T extends Annotatable> 
	implements IAnnotationController<T>{

	@Override
	public final boolean canExecute(Mode mode, AnnotationType type, T element) {
		if (element == null) {
			throw new IllegalArgumentException("Element was null.");
		}
		
		switch (mode) {
		case SET:
			return canSetAnnotation(type, element);				
		case UNSET:
			return canUnsetAnnotation(type, element);
		default:
			throw new IllegalArgumentException("Unknown mode '" + mode + "'");
		}
	}	

	protected abstract boolean canSetAnnotation(AnnotationType type, T element);	
	protected abstract boolean canUnsetAnnotation(AnnotationType type, T element);

	@Override
	public final boolean execute(Mode mode, Annotation annotation, T element) {
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

	protected boolean setAnnotation(Annotation annotation, T element) {
		element.setAnnotation(annotation);
		return true;
	}
	
	protected boolean unsetAnnotation(Annotation annotation, T element) {
		element.setAnnotation(null);
		return true;
	}

}
