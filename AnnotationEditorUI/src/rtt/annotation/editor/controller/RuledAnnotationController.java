package rtt.annotation.editor.controller;

import rtt.annotation.editor.controller.rules.IAnnotationRule;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;

public abstract class RuledAnnotationController<T extends Annotatable>
	extends AbstractAnnotationController<T> {
	
	IAnnotationRule<T> rule;
	
	public RuledAnnotationController(IAnnotationRule<T> rule) {
		this.rule = rule;
	}
	
	@Override
	protected final boolean canSetAnnotation(AnnotationType type, T element) {
		return rule.canSet(type, element);
	}
	
	@Override
	protected final boolean canUnsetAnnotation(AnnotationType type, T element) {
		return rule.canUnset(type, element);
	}	
}
