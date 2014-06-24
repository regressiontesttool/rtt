package rtt.annotation.editor.controller;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.controller.rules.IAnnotationRule;
import rtt.annotation.editor.model.Annotatable;

public abstract class RuledAnnotationController<T extends Annotatable<?>>
	implements IAnnotationController<T> {
	
	IAnnotationRule<T> rule;
	
	protected void setRule(IAnnotationRule<T> rule) {
		this.rule = rule;
	}
	
	protected IAnnotationRule<T> getRule() {
		return rule;
	};
	
	@Override
	public boolean isAllowed(Annotation annotation, T element) {
		return rule.isAllowed(annotation, element);
	}
}
