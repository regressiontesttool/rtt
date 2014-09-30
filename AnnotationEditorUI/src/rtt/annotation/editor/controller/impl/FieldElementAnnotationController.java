package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;

public class FieldElementAnnotationController 
	extends RuledAnnotationController<ValueAnnotation, FieldElement<ValueAnnotation>> {
	
	public FieldElementAnnotationController() {
		super(new AbstractAnnotationRule<FieldElement<ValueAnnotation>>() {});
	}
	
	@Override
	public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
		return ValueAnnotation.class.isAssignableFrom(annotationType);
	}
	
	@Override
	public boolean hasType(Class<? extends Annotatable<?>> annotatableType) {
		return FieldElement.class.isAssignableFrom(annotatableType);
	}
}