package rtt.annotation.editor.controller.impl;

import java.io.InputStream;

import rtt.annotation.editor.controller.AbstractAnnotationController;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.InitAnnotation;

public class InitAnnotationController extends
		AbstractAnnotationController<InitAnnotation> {

	public InitAnnotationController() {
		super(InitAnnotation.class);
	}

	@Override
	protected boolean canSetAnnotation(Annotatable<?> element) {
		if (element instanceof MethodElement<?> && !element.hasAnnotation()) {
			MethodElement<?> method = (MethodElement<?>) element;
			int parameterCount = method.getParameters().size();
			if (parameterCount < 1 || parameterCount > 2) {
				return false;
			}
			
			String firstParameter = InputStream.class.getName();
			if (!method.getParameters().get(0).equals(firstParameter)) {
				return false;
			}
			
			return true;
		}
		
		return false;
	}
}
