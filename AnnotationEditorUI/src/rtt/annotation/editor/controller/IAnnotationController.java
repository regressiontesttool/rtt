package rtt.annotation.editor.controller;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.model.Annotatable;

public interface IAnnotationController<T extends Annotatable<?>> {
	
	public boolean setAnnotation(Annotation annotation, T element);
	public boolean isAllowed(Annotation annotation, T element);
}
