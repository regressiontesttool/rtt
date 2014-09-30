package rtt.annotation.editor.controller.impl;

import java.io.InputStream;

import rtt.annotation.editor.controller.RuledAnnotationController;
import rtt.annotation.editor.controller.rules.AbstractAnnotationRule;
import rtt.annotation.editor.model.Annotation.AnnotationType;
import rtt.annotation.editor.model.MethodElement;

public class MethodElementAnnotationController extends RuledAnnotationController<MethodElement> {
	
	private static class MethodElementRule extends AbstractAnnotationRule<MethodElement> {

		@Override
		protected boolean checkSet(AnnotationType type, MethodElement element) {
			
			if (!element.hasAnnotation()) {
				int parameterCount = element.getParameters().size();
				if (type == AnnotationType.VALUE) {
					return parameterCount == 0;
				}
				
				if (type == AnnotationType.INITIALIZE) {
					if (parameterCount < 1 || parameterCount > 2) {
						return false;
					}
					
					String firstParameter = InputStream.class.getName();
					if (!element.getParameters().get(0).equals(firstParameter)) {
						return false;
					}
					
					return true;					
				}
			}
			
			return false;
		}

		@Override
		protected boolean checkUnset(AnnotationType type, MethodElement element) {
			return element.hasAnnotation(type);
		}
		
	}
	
	public MethodElementAnnotationController() {
		super(new MethodElementRule());
	}
}