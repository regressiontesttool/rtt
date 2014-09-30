package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public interface IAnnotationRule<T extends Annotatable<?>> {	
	public boolean canSet(Class<? extends Annotation> annotation, T element);
	public boolean canUnset(Class<? extends Annotation> annotation, T element);
}
