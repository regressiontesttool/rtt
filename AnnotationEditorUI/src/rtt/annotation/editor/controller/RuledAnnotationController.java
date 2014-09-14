package rtt.annotation.editor.controller;

import rtt.annotation.editor.controller.rules.IAnnotationRule;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

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
	public final boolean canExecute(Mode mode, AnnotationType type, T element) {
		switch (mode) {
		case SET:
			return rule.canSet(type, element);				
		case UNSET:
			return rule.canUnset(type, element);
		default:
			throw new RuntimeException("Unknown mode '" + mode + "'");
		}
	}	
}
