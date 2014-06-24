package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.model.Annotatable;


public interface IAnnotationRule<T extends Annotatable<?>> {
	
	public boolean isAllowed(Annotation annotation, T element);	
}
