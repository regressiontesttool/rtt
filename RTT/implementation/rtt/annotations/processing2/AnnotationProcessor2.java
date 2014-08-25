package rtt.annotations.processing2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.tools.ant.types.resources.selectors.Compare;

import rtt.annotations.processing.AnnotationProcessor;
import rtt.annotations.processing.ClassElement;

public class AnnotationProcessor2 {
	
	private static final AnnotationProcessor2 INSTANCE = 
			new AnnotationProcessor2();
	
	private Map<Class<?>, ClassElement2> vistedClasses;
	
	public AnnotationProcessor2() {
		vistedClasses = new HashMap<>();
	}
	
	private ClassElement2 getElement(Class<?> objectType) {
		if (objectType == null) {
			throw new IllegalArgumentException("Object type must not be null.");
		}
		
		if (!vistedClasses.containsKey(objectType)) {
			vistedClasses.put(objectType, createClassElement(objectType));
		}
		
		return vistedClasses.get(objectType);
	}

	private ClassElement2 createClassElement(Class<?> objectType) {
		Class<?> superClass = objectType.getSuperclass();
		if (superClass != null && superClass != Object.class) {
			return new ClassElement2(objectType, getElement(superClass));
		} else {
			return new ClassElement2(objectType);
		}				
	}

	public static Set<AnnotatedElement<?>> getAnnotatedElements(Object object) {
		Class<?> objectType = object.getClass();
		ClassElement2 classElement = INSTANCE.getElement(objectType);
		
		return classElement.getValueMembers();
	}
	
	
}
