package rtt.annotation.editor.controller.impl;

import java.io.InputStream;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.InitAnnotation;

public class InitiMethodElementAnnotationController 
	extends RuledAnnotationController<InitAnnotation, MethodElement<InitAnnotation>> {
	
	private static class InitMethodElementRule 
		extends AbstractAnnotationRule<MethodElement<InitAnnotation>> {

		@Override
		protected boolean checkSet(Class<? extends Annotation> annotation, 
				MethodElement<InitAnnotation> element) {
			
			if (!element.hasAnnotation()) {
				int parameterCount = element.getParameters().size();
				if (parameterCount < 1 || parameterCount > 2) {
					return false;
				}
				
				String firstParameter = InputStream.class.getName();
				if (!element.getParameters().get(0).equals(firstParameter)) {
					return false;
				}
				
				return true;
			}
			
			return false;
		}		
	}
	
	public InitiMethodElementAnnotationController() {
		super(new InitMethodElementRule());
	}
	
	@Override
	public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
		return InitAnnotation.class.isAssignableFrom(annotationType);
	}
	
	@Override
	public boolean hasType(Class<? extends Annotatable<?>> annotatableType) {
		return MethodElement.class.isAssignableFrom(annotatableType);
	}
}