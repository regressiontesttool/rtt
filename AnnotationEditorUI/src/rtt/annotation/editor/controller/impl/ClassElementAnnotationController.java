package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.controller.rules.NoAnnotationRule;
import rtt.annotation.editor.model.ClassElement;

public class ClassElementAnnotationController extends RuledAnnotationController<ClassElement> {
	
	public ClassElementAnnotationController() {
		setRule(new NoAnnotationRule<ClassElement>());
	}
	
	@Override
	public boolean setAnnotation(Annotation annotation,
			ClassElement element) {
		
		element.setAnnotation(annotation);
		return true;
	}
	
}
