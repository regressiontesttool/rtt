package rtt.annotation.editor.controller;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.rules.Annotation;

public interface IAnnotationController<T extends Annotatable<?>> {
	
	public boolean setAnnotation(Annotation annotation, T element);
}
