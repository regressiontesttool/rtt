package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;

public class ValueMethodElementAnnotationController 
	extends RuledAnnotationController<ValueAnnotation, MethodElement<ValueAnnotation>> {
	
	private static class ValueMethodElementRule 
		extends AbstractAnnotationRule<MethodElement<ValueAnnotation>> {

		@Override
		protected boolean checkSet(Class<? extends Annotation> annotation, 
				MethodElement<ValueAnnotation> element) {
			
			if (!element.hasAnnotation()) {
				return element.getParameters().size() == 0;
			}
			
			return false;
		}		
	}
	
	public ValueMethodElementAnnotationController() {
		super(new ValueMethodElementRule());
	}
	
	@Override
	public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
		return ValueAnnotation.class.isAssignableFrom(annotationType);
	}
	
	@Override
	public boolean hasType(Class<? extends Annotatable<?>> annotatableType) {
		return MethodElement.class.isAssignableFrom(annotatableType);
	}
}