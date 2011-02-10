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

	private Class clazz;

	public AnnotationProcessor(Class clazz) {
		this.clazz = clazz;
	}

	public <A extends Annotation> A getAnnotation(Class<A> annotationClass)
			throws Exception {
		if (!clazz.isAnnotationPresent(annotationClass))
			throw new Exception("Annotation " + annotationClass.toString()
					+ " is not present at class " + clazz.toString());

		return (A) clazz.getAnnotation(annotationClass);
	}

	public <A extends Annotation> List<Method> getMethodsWithAnnotation(
			Class<A> annotationClass) {
		Method[] ms = clazz.getMethods();
		List<Method> result = new LinkedList<Method>();
		for (Method m : ms)
			if (m.isAnnotationPresent(annotationClass))
				result.add(m);

		return result;
	}

	public <A extends Annotation> List<Constructor> getConstructorsWithAnnotation(
			Class<A> annotationClass) {
		Constructor[] cs = clazz.getConstructors();
		List<Constructor> result = new LinkedList<Constructor>();
		for (Constructor c : cs)
			if (c.isAnnotationPresent(annotationClass))
				result.add(c);

		return result;
	}

	public <A extends Annotation> List<Field> getFieldsWithAnnotation(
			Class<A> annotationClass) {
		Field[] fs = clazz.getFields();
		List<Field> result = new LinkedList<Field>();
		for (Field f : fs)
			if (f.isAnnotationPresent(annotationClass))
				result.add(f);

		return result;
	}


	public List<Method> getMethods(String s) throws Exception {
		Method[] ms = clazz.getMethods();
		List<Method> result = new LinkedList<Method>();
		for (Method m : ms)
			if (m.getName().equals(s))
				result.add(m);

		return result;
	}

	
	public Object getNewInstance() throws Exception {
		return clazz.newInstance();
	}

	
	@SuppressWarnings("unchecked")
	public <T> Object getNewInstance(Class<T> type, T parameter)
			throws Exception {
		return clazz.getConstructor(type).newInstance(parameter);
	}
}
