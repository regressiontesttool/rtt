package rtt.annotation.editor.controller;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public abstract class AbstractAnnotationController<A extends Annotation> 
	implements IAnnotationController<A> {
	
	private Class<A> annotationType;
	
	public AbstractAnnotationController(Class<A> annotationType) {
		this.annotationType = annotationType;
	}
	
	@Override
	public final boolean hasAnnotationType(Class<? extends Annotation> annotationType) {
		return this.annotationType.equals(annotationType);
	}

	@Override
	public final boolean canExecute(Mode mode, Annotatable<?> element) {
		
		if (element == null) {
			throw new IllegalArgumentException("Element was null.");
		}
		
		switch (mode) {
		case SET:
			return canSetAnnotation(element);				
		case UNSET:
			return canUnsetAnnotation(element);
		default:
			throw new IllegalArgumentException("Unknown mode '" + mode + "'");
		}	
	}	

	protected boolean canSetAnnotation(Annotatable<?> element) {
		return !element.hasAnnotation();
	}
	
	protected boolean canUnsetAnnotation(Annotatable<?> element) {
		return element.hasAnnotation();
	}

	@Override
	public final boolean execute(Mode mode, Annotatable<A> element) {
		
		if (element == null) {
			throw new IllegalArgumentException("Element was null.");
		}
		
		switch (mode) {
		case SET: 
			element.setAnnotation(Annotation.create(annotationType));
			return true;
		case UNSET:
			element.setAnnotation(null);
			return true;
		default:
			throw new IllegalArgumentException("Unknown mode '" + mode + "'");
		}
	}
}
