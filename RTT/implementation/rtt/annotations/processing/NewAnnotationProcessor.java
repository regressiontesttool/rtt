package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewAnnotationProcessor {
	
	protected static class Element {
		
		private Class<?> type;
		private Element parentElement;
		
		private MemberElement<Constructor<?>> constructors;
		private MemberElement<Field> fields;
		private MemberElement<Method> methods;
		
		public Element(Class<?> objectType, Element parentElement) {
			this.type = objectType;
			this.parentElement = parentElement;
			
			constructors = new MemberElement<Constructor<?>>() {
				@Override
				protected Constructor<?>[] getElements() {
					return type.getDeclaredConstructors();
				}		
			};
			
			fields = new MemberElement<Field>() {				
				@Override
				protected Field[] getElements() {
					return type.getDeclaredFields();
				}
			};
			
			methods = new MemberElement<Method>() {
				@Override
				protected Method[] getElements() {
					return type.getDeclaredMethods();
				}
			};
		}
		
		public Class<?> getType() {
			return type;
		}

		public List<Constructor<?>> getConstructors(
				Class<? extends Annotation> annotation) {
			return constructors.getAnnotatedElements(annotation);
		}

		public List<Method> getMethods(Class<? extends Annotation> annotation) {
			return methods.getAnnotatedElements(annotation);
		}

		public List<Field> getFields(Class<? extends Annotation> annotation) {
			return fields.getAnnotatedElements(annotation);
		}		
	}
	
	protected static abstract class MemberElement<T extends AnnotatedElement> {
		private Map<Class<? extends Annotation>, List<T>> elements;
		
		public MemberElement() {
			elements = new HashMap<>();
		}
		
		public List<T> getAnnotatedElements(Class<? extends Annotation> annotation) {
			if (!elements.containsKey(annotation)) {
				T[] allElements = getElements();
				List<T> annotatedElements = new ArrayList<>();
				for (T element : allElements) {
					if (element.isAnnotationPresent(annotation)) {
						annotatedElements.add(element);
					}
				}
				
				elements.put(annotation, annotatedElements);
			}
			
			return elements.get(annotation);
		}

		protected abstract T[] getElements();
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
		if (objectType != Object.class) {
			System.out.println("Visit class: " + objectType);
			
			Element parentElement = getElement(objectType.getSuperclass());
			vistedClasses.put(objectType, new Element(objectType, parentElement));
		}		
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
