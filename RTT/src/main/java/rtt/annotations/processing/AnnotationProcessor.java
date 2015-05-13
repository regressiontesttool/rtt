package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import rtt.annotations.Node;

public class AnnotationProcessor {
	
	private static final AnnotationProcessor INSTANCE = 
			new AnnotationProcessor();
	
	private static final Class<? extends Annotation> NODE_ANNOTATION = Node.class;
	private Map<Class<?>, ClassElement> vistedClasses;
	
	public AnnotationProcessor() {
		vistedClasses = new HashMap<>();
	}
	
	public static boolean isNode(Object object) {
		return object != null && isNode(object.getClass());
	}
	
	public static boolean isNode(Class<?> objectType) {
		return getAnnotation(objectType, NODE_ANNOTATION) != null;
	}	
	
	private static Object getAnnotation(Class<?> objectType,
			Class<? extends Annotation> annotationType) {
		
		if (objectType.isAnnotationPresent(annotationType)) {
			return objectType.getAnnotation(annotationType);
		}

		for (Class<?> interfaceObject : objectType.getInterfaces()) {
			if (interfaceObject.isAnnotationPresent(annotationType)) {
				return interfaceObject.getAnnotation(annotationType);
			}
		}

		return null;
	}

	public static SortedSet<ValueMember<?>> getValueMembers(Class<?> objectType) {
		ClassElement classElement = INSTANCE.getElement(objectType);		
		return classElement.getValueMembers();
	}
	
	public static SortedSet<InitMember<?>> getInitMembers(Class<?> objectType) {
		ClassElement classElement = INSTANCE.getElement(objectType);		
		return classElement.getInitMembers();
	}
	
	private ClassElement getElement(Class<?> objectType) {
		if (objectType == null) {
			throw new IllegalArgumentException("Object type must not be null.");
		}
		
		if (!vistedClasses.containsKey(objectType)) {
			vistedClasses.put(objectType, createClassElement(objectType));
		}
		
		return vistedClasses.get(objectType);
	}

	private ClassElement createClassElement(Class<?> objectType) {
		Class<?> superClass = objectType.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			return new ClassElement(objectType, getElement(superClass));
		} else {
			return new ClassElement(objectType);
		}				
	}	
}
