package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;

public interface IAnnotationRule<T extends Annotatable> {	
	public boolean canSet(AnnotationType type, T element);
	public boolean canUnset(AnnotationType type, T element);
}
