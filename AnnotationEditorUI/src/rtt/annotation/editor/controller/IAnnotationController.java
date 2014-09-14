package rtt.annotation.editor.controller;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.Annotation;
import rtt.annotation.editor.model.Annotation.AnnotationType;

public interface IAnnotationController<T extends Annotatable<?>> {
	
	public enum Mode {
		SET, UNSET;
	}
	
	public boolean canExecute(Mode mode, AnnotationType annotation, T element);
	public boolean execute(Mode mode, Annotation annotation, T element);	
}
