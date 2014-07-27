/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for accessing annotations of an object.
 */
public class AnnotationProcessor {
	
	public static <A extends Annotation> A getAnnotation(Class<?> objectType, Class<A> annotationType) {
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
	
	public static boolean hasAnnotation(Class<?> objectType, Class<? extends Annotation> annotationType) {
		return getAnnotation(objectType, annotationType) != null;
	}
	
	/**
	 * <p>Returns *all* {@link AnnotatedElement}s of a given object type, 
	 * which contain the given type of {@link Annotation}.</p>
	 * @param objectType the type of object 
	 * @param annotationType the type of annotation
	 * @return a {@link List} of {@link AnnotatedElement}s
	 */
	public static <A extends AnnotatedElement> List<A> getElements(A[] elements, Class<? extends Annotation> annotation) {
		List<A> annotatedElements = new ArrayList<>();
		for (A element : elements) {
			if (element.isAnnotationPresent(annotation)) {
				annotatedElements.add(element);
			}
		}
		
		return annotatedElements;
	}
	
	/**
	 * <p>Returns *all* {@link Constructor}s of a given object type, 
	 * which contain the given type of {@link Annotation}.</p>
	 * @param objectType the type of object 
	 * @param annotationType the type of annotation
	 * @return a {@link List} of {@link Constructor}s
	 */
	public static List<Constructor<?>> getConstructors(Class<?> objectType, Class<? extends Annotation> annotationType) {
		return getElements(objectType.getDeclaredConstructors(), annotationType);
	}
	
	/**
	 * <p>Returns *all* {@link Field}s of a given object type, 
	 * which contain the given type of {@link Annotation}.</p>
	 * @param objectType the type of object 
	 * @param annotationType the type of annotation
	 * @return a {@link List} of {@link Field}s
	 */
	public static List<Field> getFields(Class<?> objectType, Class<? extends Annotation> annotationType) {
		return getElements(objectType.getDeclaredFields(), annotationType);
	}

	/**
	 * <p>Returns *all* {@link Method}s of a given object type, 
	 * which contain the given type of {@link Annotation}.</p>
	 * @param objectType the type of object 
	 * @param annotationType the type of annotation
	 * @return a {@link List} of {@link Method}s
	 */
	public static List<Method> getMethods(Class<?> objectType, Class<? extends Annotation> annotationType) {
		// TODO evaluate optimized annotation loading
//		return MethodAnnotationProcessor.INSTANCE.findMethods(objectClass, annotation);
		
		return getElements(objectType.getDeclaredMethods(), annotationType);
	}

	/**
	 * <p>Returns a *single* {@link Method} which contains the given {@link Annotation}.<br />
	 * <i>Note</i>: if more than one {@link Method} was found, <code>null</code> will be returned.</p>
	 * @param objectType the type of object
	 * @param annotationType the type of annotation
	 * @return a {@link Method} object or <code>null</code>.
	 */
	public static Method getSingleMethod(Class<?> objectType, Class<? extends Annotation> annotationType) {
		List<Method> annotatedMethods = getMethods(objectType, annotationType);
		if (annotatedMethods != null && annotatedMethods.size() == 1) {
			return annotatedMethods.get(0);
		}
		
		return null;
	}
}
