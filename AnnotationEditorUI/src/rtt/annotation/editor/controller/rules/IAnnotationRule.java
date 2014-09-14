package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.controller.rules.RTTAnnotation.AnnotationType;
import rtt.annotation.editor.model.Annotatable;


public interface IAnnotationRule<T extends Annotatable<?>> {	
	public boolean canExecute(Mode mode, AnnotationType type, T element);
}
