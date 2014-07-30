package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

final class ConstructorMemberElement extends
		AbstractMemberElement<Constructor<?>> {

	public ConstructorMemberElement(ClassElement classElement) {
		super(classElement);
	}
	
	@Override
	protected synchronized List<Constructor<?>> createElements(ClassElement classElement,
			Class<? extends Annotation> annotation) {
		
		List<Constructor<?>> annotatedConstructors = new ArrayList<>();
		
		ClassElement parentElement = classElement.parentElement;
		if (parentElement != null) {
			for (Constructor<?> constructor : parentElement.getConstructors(annotation)) {
				annotatedConstructors.add(constructor);
			}
		}
				
		Class<?> objectType = classElement.type;
		for (Constructor<?> constructor : objectType.getDeclaredConstructors()) {
			if (constructor.isAnnotationPresent(annotation)) {
				annotatedConstructors.add(constructor);
			}
		}
		
		return annotatedConstructors;
	}
}