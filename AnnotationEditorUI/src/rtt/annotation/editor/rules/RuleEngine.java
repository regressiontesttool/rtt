package rtt.annotation.editor.rules;

import java.util.HashMap;
import java.util.Map;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class RuleEngine {
	
	public static final class ClassElementNodeRule implements IAnnotationRule<ClassElement> {
		
		@Override
		public boolean isAllowed(Annotation annotation, ClassElement element) {
			if (annotation == Annotation.NONE) {
				return element.hasAnnotation();
			}

			return !element.hasAnnotation();
		}
	}
	
	public static final class FieldElementCompareRule implements IAnnotationRule<FieldElement> {

		@Override
		public boolean isAllowed(Annotation annotation, FieldElement element) {
			if (annotation == Annotation.NONE) {
				return element.hasAnnotation();
			}

			return !element.hasAnnotation();
		}		
	}
	
	public static final class MethodElementCompareRule implements IAnnotationRule<MethodElement> {

		@Override
		public boolean isAllowed(Annotation annotation, MethodElement element) {
			if (annotation == Annotation.NONE) {
				return element.hasAnnotation();
			}

			return !element.hasAnnotation();
		}		
	}

	private static final RuleEngine INSTANCE = new RuleEngine();
	
	private Map<Class<? extends Annotatable<?>>, IAnnotationRule<?>> rules;
	
	public RuleEngine() {
		rules = new HashMap<Class<? extends Annotatable<?>>, IAnnotationRule<?>>();
		
		rules.put(ClassElement.class, new ClassElementNodeRule());
		rules.put(FieldElement.class, new FieldElementCompareRule());
		rules.put(MethodElement.class, new MethodElementCompareRule());
	}

	public static <T extends Annotatable<?>> boolean canApply(Annotation annotation, T element) {
		
		IAnnotationRule<T> rule = RuleEngine.INSTANCE.findRule(element);
		
		if (rule != null) {
			return rule.isAllowed(annotation, element);
		}
		
		return false;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends Annotatable<?>> IAnnotationRule<T> findRule(T element) {
		if (rules.containsKey(element.getClass())) {
			return (IAnnotationRule<T>) rules.get(element.getClass());
		}
		
		return null;
	}

}
		