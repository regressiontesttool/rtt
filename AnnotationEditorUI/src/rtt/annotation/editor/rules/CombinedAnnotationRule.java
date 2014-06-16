package rtt.annotation.editor.rules;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.model.Annotatable;

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
	public final boolean checkRule(Annotation annotation, T element) {
		if (rules.isEmpty()) {
			return false;
		}
		
		for (IAnnotationRule<T> rule : rules) {
			if (rule.isAllowed(annotation, element) == false) {
				return false;
			}
		}
		
		return true;
	}
}
