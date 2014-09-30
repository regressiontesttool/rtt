package rtt.annotation.editor.controller;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public interface IAnnotationController<A extends Annotation, T extends Annotatable<A>> {
	
	public enum Mode {
		SET, UNSET;
	}
	
	public boolean canExecute(Mode mode, Class<? extends Annotation> annotation, T element);
	public boolean execute(Mode mode, A annotation, T element);
	
	public boolean hasAnnotation(Class<? extends Annotation> annotationType);
	public boolean hasType(Class<? extends Annotatable<?>> annotatableType);
}
