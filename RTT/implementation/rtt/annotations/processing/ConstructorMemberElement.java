package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

final class ConstructorMemberElement extends
		MemberElement<Constructor<?>> {

	public ConstructorMemberElement(ClassElement classElement) {
		super(classElement);
	}
	
	@Override
	protected Map<String, Constructor<?>> createElements(ClassElement classElement,
			Class<? extends Annotation> annotation) {
		
		Map<String, Constructor<?>> annotatedConstructors = new HashMap<>();
		
		ClassElement parentElement = classElement.getParentElement();
		if (parentElement != null) {
			annotatedConstructors.putAll(parentElement.getConstructorMap(annotation));
		}
				
		Class<?> objectType = classElement.getType();
		for (Constructor<?> constructor : objectType.getDeclaredConstructors()) {
			if (constructor.isAnnotationPresent(annotation)) {
				annotatedConstructors.put(constructor.getName(), constructor);
			}
		}
		
		return annotatedConstructors;
	}
}