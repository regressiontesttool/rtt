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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * Helperclass for accessing annotations of an object.
 * 
 * @author Peter Mucha
 * 
 */
public class AnnotationProcessor {
	
	private Class<?> objectClass;
	
	/**
	 * <p>Initialize a new {@link AnnotationProcessor}. <br />
	 * Note: <br />
	 * - if the object is null, then a {@link NullPointerException} 
	 * will be thrown. <br/>
	 * - if the given class is an interface, then an 
	 * {@link IllegalArgumentException} will be thrown.</p>
	 * @param objectClass
	 */
	public AnnotationProcessor(Class<?> objectClass) {
		if (objectClass == null) {
			throw new NullPointerException("The given object was null.");
		}
		
		if (objectClass.isInterface()) {
			throw new IllegalArgumentException("The given class was an interface.");
		}
		
		this.objectClass = objectClass;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass)
			throws IllegalArgumentException {
		if (objectClass.isAnnotationPresent(annotationClass)) {
			return objectClass.getAnnotation(annotationClass);
		}
		
		for (Class<?> interfaceObject : objectClass.getInterfaces()) {
			if (interfaceObject.isAnnotationPresent(annotationClass)) {
				return interfaceObject.getAnnotation(annotationClass);
			}
		}
		
		throw new IllegalArgumentException("Annotation " + annotationClass.toString()
				+ " is not present at class " + objectClass.toString());
	}

	/**
	 * <p>Returns *all* {@link Method}s which contain the given {@link Annotation}.</p>
	 * @param annotation
	 */
	public List<Method> getMethodsWithAnnotation(Class<? extends Annotation> annotation) {		
		return MethodAnnotationProcessor.INSTANCE.findMethods(objectClass, annotation);
	}
	
	/**
	 * <p>Returns a *single* {@link Method} which contains the given {@link Annotation}.<br />
	 * Note: if more than one {@link Method} was found, then an {@link Exception} will be thrown.</p>
	 * @param annotationClass
	 * 
	 * @throws Exception
	 */
	public Method getMethodWithAnnotation(Class<? extends Annotation> annotationClass) throws Exception {
		List<Method> methodList = getMethodsWithAnnotation(annotationClass);
		if (methodList.size() == 1) {
			return methodList.get(0);
		} else {
			throw new Exception("Can't specify a single method annotated with "
					+ annotationClass.toString());
		}
	}

	/**
	 * Returns *all* {@link Constructor}s which contain the given {@link Annotation}.
	 * @param annotationClass
	 * 
	 */
	public List<Constructor<?>> getConstructorsWithAnnotation(Class<? extends Annotation> annotationClass) {
		Constructor<?>[] cs = objectClass.getConstructors();
		List<Constructor<?>> result = new LinkedList<Constructor<?>>();
		for (Constructor<?> c : cs)
			if (c.isAnnotationPresent(annotationClass))
				result.add(c);

		return result;
	}

	/**
	 * Returns *all* {@link Field} which contain the given {@link Annotation}.
	 * @param annotationClass
	 * 
	 */
	public List<Field> getFieldsWithAnnotation(Class<? extends Annotation> annotationClass) {
		Field[] fs = objectClass.getFields();
		List<Field> result = new LinkedList<Field>();
		for (Field f : fs)
			if (f.isAnnotationPresent(annotationClass))
				result.add(f);

		return result;
	}
	
	public Object getObjectInstance() throws Exception {
		return objectClass.newInstance();
	}

	
//	public <T> Object getNewInstance(Class<T> type, T parameter)
//			throws Exception {
//		return objectClass.getConstructor(type).newInstance(parameter);
//	}
}
