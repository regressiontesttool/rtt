package rtt.annotations.processing2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import rtt.annotations.Node.Compare;
import rtt.annotations.Node.Informational;
import rtt.annotations.processing.AnnotationProcessor;

public class AnnotationProcessor2 {

	public static Set<AnnotatedElement<?>> getAnnotatedElements(Object object) {
		Class<?> objectType = object.getClass();		
		SortedSet<AnnotatedElement<?>> elements = new TreeSet<>();
		elements.addAll(getAnnotatedFields(objectType));
		elements.addAll(getAnnotatedMethods(objectType));
		
		return elements;
	}
	
	private static Set<AnnotatedElement<Field>> getAnnotatedFields(Class<?> objectType) {
		
		Set<AnnotatedElement<Field>> annotatedFields = new TreeSet<>();		
		Map<Field, Compare> compareFields = getFields(objectType, Compare.class);
		
		for (Entry<Field, Compare> fieldEntry : compareFields.entrySet()) {
			annotatedFields.add(new AnnotatedField(
					fieldEntry.getKey(), fieldEntry.getValue()));
		}
		
		Map<Field, Informational> infoFields = getFields(objectType, Informational.class);;
		for (Entry<Field, Informational> fieldEntry : infoFields.entrySet()) {
			annotatedFields.add(new AnnotatedField(
					fieldEntry.getKey(), fieldEntry.getValue()));
		}		
		
		return annotatedFields;
	}	

	private static <T extends Annotation> Map<Field, T> getFields(
			Class<?> objectType, Class<T> annotation) {
		
		AnnotationProcessor.getFields(objectType, annotation);
		
		return new TreeMap<>();
	}

	private static Set<AnnotatedElement<Method>> getAnnotatedMethods(Class<?> objectType) {
		Set<AnnotatedElement<Method>> annotatedMethods = new TreeSet<>();		
		Map<Method, Compare> compareMethods = getMethods(objectType, Compare.class);
		
		for (Entry<Method, Compare> methodEntry : compareMethods.entrySet()) {
			annotatedMethods.add(new AnnotatedMethod(
					methodEntry.getKey(), methodEntry.getValue()));
		}
		
		Map<Method, Informational> infoMethods = getMethods(objectType, Informational.class);
		for (Entry<Method, Informational> methodEntry : infoMethods.entrySet()) {
			annotatedMethods.add(new AnnotatedMethod(
					methodEntry.getKey(), methodEntry.getValue()));
		}		
		
		return annotatedMethods;
	}

	private static <T extends Annotation> Map<Method, T> getMethods(
			Class<?> objectType, Class<T> annotation) {
		
		return new TreeMap<>();
	}
}
