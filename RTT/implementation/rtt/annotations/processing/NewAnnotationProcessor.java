package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAnnotationProcessor {
	
	protected static class Element {
		
		private Class<?> type;
		
		private List<Constructor<?>> constructors;
		private List<Field> fields;
		private List<Method> methods;	
		
		public Element(Class<?> objectType) {
			this.type = objectType;
			
			constructors = Arrays.asList(objectType.getDeclaredConstructors());
			fields = Arrays.asList(objectType.getDeclaredFields());
			methods = Arrays.asList(objectType.getDeclaredMethods());
		}
		
		private <T extends AnnotatedElement> List<T> getAnnotatedElements(
				List<T> elements, Class<? extends Annotation> annotation) {
			
			List<T> annotatedElements = new ArrayList<>();
			for (T element : elements) {
				if (element.isAnnotationPresent(annotation)) {
					annotatedElements.add(element);
				}
			}
			
			return annotatedElements;
		}
		
		public Class<?> getType() {
			return type;
		}

		public List<Constructor<?>> getConstructors(
				Class<? extends Annotation> annotation) {
			return getAnnotatedElements(constructors, annotation);
		}

		public List<Method> getMethods(Class<? extends Annotation> annotation) {
			return getAnnotatedElements(methods, annotation);
		}

		public List<Field> getFields(Class<? extends Annotation> annotation) {
			return getAnnotatedElements(fields, annotation);
		}		
	}
	
	private static final NewAnnotationProcessor INSTANCE = 
			new NewAnnotationProcessor();
	
	private Map<Class<?>, Element> vistedClasses;
	
	public NewAnnotationProcessor() {
		vistedClasses = new HashMap<>();
	}
	
	private Element getElement(Class<?> objectType) {
		if (!vistedClasses.containsKey(objectType)) {
			visitClass(objectType);
		}
		
		return vistedClasses.get(objectType);
	}

	private void visitClass(Class<?> objectType) {
		System.out.println("Visit class: " + objectType);
		vistedClasses.put(objectType, new Element(objectType));
	}

	public static List<Constructor<?>> getConstructors(Class<?> objectType,
			Class<? extends Annotation> annotation) {
		
		Element element = INSTANCE.getElement(objectType);
		return element.getConstructors(annotation);		
	}

	public static List<Method> getMethods(Class<?> objectType,
			Class<? extends Annotation> annotation) {

		Element element = INSTANCE.getElement(objectType);
		return element.getMethods(annotation);		
	}

	public static List<Field> getFields(Class<?> objectType,
			Class<? extends Annotation> annotation) {
		
		Element element = INSTANCE.getElement(objectType);
		return element.getFields(annotation);		
	}

}
