package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

final class MethodMemberElement extends	AbstractMemberElement<Method> {
	
	public MethodMemberElement(ClassElement classElement) {
		super(classElement);
	}

	@Override
	protected Map<String, Method> getParentElements(ClassElement parentElement,
			Class<? extends Annotation> annotation) {
		return parentElement.getMethodMap(annotation);
	}

	@Override
	protected void addElements(Class<?> objectType,
			Class<? extends Annotation> annotation,
			Map<String, Method> annotatedElements) {
		
		for (Class<?> interfaceType : objectType.getInterfaces()) {
			addElements(interfaceType, annotation, annotatedElements);
		}
		
		for (Method method : objectType.getDeclaredMethods()) {
			String signature = getSignature(method);
			
			if (method.isAnnotationPresent(annotation)) {
				annotatedElements.put(signature, method);
			}
			
			if (annotatedElements.containsKey(signature) &&
					!Modifier.isPrivate(method.getModifiers())) {
				
				annotatedElements.put(signature, method);
			}
		}
	}
	
	private String getSignature(Method method) {
		StringBuilder builder = new StringBuilder(method.getName());
		builder.append("-");
		builder.append(method.getReturnType().getSimpleName());
		builder.append("-[");
		for (Class<?> parameter : method.getParameterTypes()) {
			builder.append(parameter.getSimpleName());
			builder.append(";");
		}
		builder.append("]");
		
		return builder.toString();
	}
	
}