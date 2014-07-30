package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class MethodMemberElement extends	AbstractMemberElement<Method> {
	
	public MethodMemberElement(ClassElement classElement) {
		super(classElement);
	}

	@Override
	protected synchronized List<Method> createElements(
			ClassElement classElement,
			Class<? extends Annotation> annotation) {
		
		Map<String, Method> annotatedMethods = new HashMap<>();
		
		ClassElement parentElement = classElement.parentElement;
		if (parentElement != null) {
			for (Method method : parentElement.getMethods(annotation)) {
				annotatedMethods.put(getSignature(method), method);
			}
		}
		
		addMethods(classElement.type, annotation, annotatedMethods);
		
		return new ArrayList<>(annotatedMethods.values());
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

	private void addMethods(Class<?> objectType, 
			Class<? extends Annotation> annotation, 
			Map<String, Method> annotatedMethods) {
		
		for (Class<?> interfaceType : objectType.getInterfaces()) {
			addMethods(interfaceType, annotation, annotatedMethods);
		}
		
		for (Method method : objectType.getDeclaredMethods()) {
			String signature = getSignature(method);
			
			if (method.isAnnotationPresent(annotation)) {
				annotatedMethods.put(signature, method);
			}
			
			if (annotatedMethods.containsKey(signature) &&
					!Modifier.isPrivate(method.getModifiers())) {
				
				annotatedMethods.put(signature, method);
			}
		}
	}
}