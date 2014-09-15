package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.Annotation.AnnotationType;

public interface IAnnotationRule<T extends Annotatable> {	
	public boolean canSet(AnnotationType type, T element);
	public boolean canUnset(AnnotationType type, T element);
}
