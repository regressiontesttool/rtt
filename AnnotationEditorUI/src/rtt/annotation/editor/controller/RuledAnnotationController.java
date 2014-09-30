package rtt.annotation.editor.controller;

import rtt.annotation.editor.controller.rules.IAnnotationRule;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

public abstract class RuledAnnotationController<A extends Annotation, T extends Annotatable<A>>
	extends AbstractAnnotationController<A, T> {
	
	IAnnotationRule<T> rule;
	
	public RuledAnnotationController(IAnnotationRule<T> rule) {
		this.rule = rule;
	}
	
	@Override
	protected final boolean canSetAnnotation(
			Class<? extends Annotation> annotation, T element) {
		return rule.canSet(annotation, element);
	}
	
	@Override
	protected final boolean canUnsetAnnotation(
			Class<? extends Annotation> annotation, T element) {
		return rule.canUnset(annotation, element);
	}
}
