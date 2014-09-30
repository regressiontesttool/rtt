package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.NodeAnnotation;

public class ClassElementAnnotationController 
	extends RuledAnnotationController<NodeAnnotation, ClassElement> {
	
	public ClassElementAnnotationController() {
		super(new AbstractAnnotationRule<ClassElement>() {});
	}
	
	@Override
	public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
		return NodeAnnotation.class.isAssignableFrom(annotationType);
	}
	
	@Override
	public boolean hasType(Class<? extends Annotatable<?>> annotatableType) {
		return ClassElement.class.isAssignableFrom(annotatableType);
	}
}
