package rtt.annotation.editor.controller;

import rtt.annotation.editor.controller.rules.RTTAnnotation;
import rtt.annotation.editor.controller.rules.RTTAnnotation.AnnotationType;
import rtt.annotation.editor.model.Annotatable;

public interface IAnnotationController<T extends Annotatable<?>> {
	
	public enum Mode {
		SET, UNSET;
	}
	
	public boolean canExecute(Mode mode, AnnotationType annotation, T element);
	public boolean execute(Mode mode, RTTAnnotation annotation, T element);	
}
