package rtt.annotation.editor.controller.rules;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.controller.IAnnotationController.Mode;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.RTTAnnotation.AnnotationType;

public class CombinedAnnotationRule<T extends Annotatable<?>> extends AbstractAnnotationRule<T> {
	
	List<IAnnotationRule<T>> rules;
	
	public CombinedAnnotationRule() {
		rules = new ArrayList<IAnnotationRule<T>>();
	}
	
	public void addRule(IAnnotationRule<T> newRule) {
		if (!rules.contains(newRule)) {
			rules.add(newRule);
		}
	}
	
	public boolean hasElements() {
		return !rules.isEmpty();		
	}

	@Override
	protected boolean checkRule(Mode mode,
			AnnotationType type, T element) {

		if (rules.isEmpty()) {
			return false;
		}
		
		for (IAnnotationRule<T> rule : rules) {
			if (rule.canExecute(mode, type, element) == false) {
				return false;
			}
		}
		
		return true;
	}
}
