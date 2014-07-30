package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAnnotationProcessor {
	
	private static final NewAnnotationProcessor INSTANCE = 
			new NewAnnotationProcessor();
	
	private Map<Class<?>, ClassElement> vistedClasses;
	
	public NewAnnotationProcessor() {
		vistedClasses = new HashMap<>();
	}
	
	public ClassElement getElement(Class<?> objectType) {
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

	public static List<Constructor<?>> getConstructors(Class<?> objectType,
			Class<? extends Annotation> annotation) {
		
		ClassElement element = INSTANCE.getElement(objectType);
		return element.getConstructors(annotation);		
	}

	public static List<Method> getMethods(Class<?> objectType,
			Class<? extends Annotation> annotation) {

		ClassElement element = INSTANCE.getElement(objectType);
		return element.getMethods(annotation);		
	}

	public static List<Field> getFields(Class<?> objectType,
			Class<? extends Annotation> annotation) {
		
		ClassElement element = INSTANCE.getElement(objectType);
		return element.getFields(annotation);		
	}

}
