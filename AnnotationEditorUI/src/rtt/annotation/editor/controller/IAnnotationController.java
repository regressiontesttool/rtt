package rtt.annotation.editor.controller;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public interface IAnnotationController<A extends Annotation> {
	
	public enum Mode {
		SET, UNSET;
	}
	
	public boolean hasAnnotationType(Class<? extends Annotation> annotationType);
	
	public boolean canExecute(Mode mode, Annotatable<?> element);	
	public boolean execute(Mode mode, Annotatable<A> element);	
}
