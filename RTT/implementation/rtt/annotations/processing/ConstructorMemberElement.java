package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

final class ConstructorMemberElement extends
		MemberElement<Constructor<?>> {

	public ConstructorMemberElement(ClassElement classElement) {
		super(classElement);
	}
	
	@Override
	protected synchronized List<Constructor<?>> createElements(ClassElement classElement,
			Class<? extends Annotation> annotation) {
		
		List<Constructor<?>> annotatedConstructors = new ArrayList<>();
		
		ClassElement parentElement = classElement.getParentElement();
		if (parentElement != null) {
			for (Constructor<?> constructor : parentElement.getConstructors(annotation)) {
				annotatedConstructors.add(constructor);
			}
		}
				
		Class<?> objectType = classElement.getType();
		for (Constructor<?> constructor : objectType.getDeclaredConstructors()) {
			if (constructor.isAnnotationPresent(annotation)) {
				annotatedConstructors.add(constructor);
			}
		}
		
		return annotatedConstructors;
	}
}