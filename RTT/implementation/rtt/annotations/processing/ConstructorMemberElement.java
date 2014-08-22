package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Map;

final class ConstructorMemberElement extends AbstractMemberElement<Constructor<?>> {

	public ConstructorMemberElement(ClassElement classElement) {
		super(classElement);
	}
	
	@Override
	protected Map<String, Constructor<?>> getParentElements(
			ClassElement parentElement, 
			Class<? extends Annotation> annotation) {
		
		return parentElement.getConstructorMap(annotation);
	}

	@Override
	protected void addElements(Class<?> objectType,
			Class<? extends Annotation> annotation,
			Map<String, Constructor<?>> annotatedElements) {
		
		for (Constructor<?> constructor : objectType.getDeclaredConstructors()) {
			if (constructor.isAnnotationPresent(annotation)) {
				annotatedElements.put(constructor.toGenericString(), constructor);
			}
		}
	}
}