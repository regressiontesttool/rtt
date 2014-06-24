package rtt.annotation.editor.controller.rules;

import rtt.annotation.editor.model.Annotatable;

public class NoAnnotationRule<T extends Annotatable<?>> extends AbstractAnnotationRule<T> {

	@Override
	public boolean checkRule(Annotation annotation, T element) {
		if (annotation == Annotation.NONE) {
			return element.hasAnnotation();
		}
		
		return !element.hasAnnotation();
	}
}
