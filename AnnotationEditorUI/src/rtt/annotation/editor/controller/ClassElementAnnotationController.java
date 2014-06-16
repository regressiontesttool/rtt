package rtt.annotation.editor.controller;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.rules.Annotation;
import rtt.annotation.editor.rules.CombinedAnnotationRule;
import rtt.annotation.editor.rules.NodeAnnotationRule;

public class ClassElementAnnotationController implements
		IAnnotationController<ClassElement> {

	public static final class ClassElementNodeRule extends
			NodeAnnotationRule<ClassElement> {
		@Override
		protected boolean isNodeAllowed(ClassElement element) {
			return !element.hasAnnotation();
		}
	}
	
	private CombinedAnnotationRule<ClassElement> rule;

	public ClassElementAnnotationController() {
		rule = new CombinedAnnotationRule<ClassElement>();
		rule.addRule(new ClassElementNodeRule());
	}

	@Override
	public boolean setAnnotation(Annotation annotation, ClassElement element) {
		if (rule.isAllowed(annotation, element)) {
			element.setAnnotation(annotation);
			return true;
		}
		
		return false;
	}
	
}
