package rtt.annotation.editor.controller.impl;

import rtt.annotation.editor.controller.AbstractAnnotationController;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.ValueAnnotation;

public class ValueAnnotationController extends AbstractAnnotationController<ValueAnnotation> {

	public ValueAnnotationController() {
		super(ValueAnnotation.class);
	}
	
	@Override
	protected boolean canSetAnnotation(Annotatable<?> element) {
		if (element instanceof FieldElement && !element.hasAnnotation()) {
			return true;
		}
		
		if (element instanceof MethodElement && !element.hasAnnotation()) {
			return ((MethodElement<?>) element).getParameters().size() == 0;
		}
		
		return false;
	}
}
