package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;


public interface IAnnotationRule<T extends Annotatable<?>> {	
	public boolean canExecute(Mode mode, AnnotationType type, T element);
}
