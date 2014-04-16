package rtt.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Christian Oelsner <C.Oelsner@gmail.com> 
 */
public class MethodAnnotationProcessor {

	/**
	 * The current annotation processor instance
	 */
	public static final MethodAnnotationProcessor INSTANCE = new MethodAnnotationProcessor();
	
	private Map<Class<?>, Map<Class<? extends Annotation>, List<Method>>> visitedClasses;
	
	private MethodAnnotationProcessor() {
		visitedClasses = new HashMap<Class<?>, Map<Class<? extends Annotation>, List<Method>>>();
	}
	
	/**
	 * 
	 * @param initClass
	 * @param annotation
	 * @return
	 */
	public synchronized List<Method> findMethods(Class<?> initClass, Class<? extends Annotation> annotation) {
		Deque<Class<?>> classHierarchie = findHierarchie(initClass);
		List<Method> result = new ArrayList<Method>();

		for (Class<?> clazz : classHierarchie) {
			List<Method> annotatedMethods = null;
			if (!isVisited(clazz, annotation)) {
				annotatedMethods = getAnnotatedMethods(clazz, annotation, result);			
			} else {
				annotatedMethods = visitedClasses.get(clazz).get(annotation);
			}
			
			for (Method method : annotatedMethods) {
				replaceMethod(method, result, annotation, true);						
			}
		}		
		
		setVisited(initClass, annotation, result);
		
		return result;
	}

	/*
	 * find a hierarchie
	 */
	private Deque<Class<?>> findHierarchie(Class<?> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("The given class object was null.");
		}
		
		Deque<Class<?>> result = new LinkedList<Class<?>>();
		
		while(clazz != null && !clazz.equals(Object.class)) {
			result.addFirst(clazz);
			clazz = clazz.getSuperclass();			
		}
		
		return result;
	}
	
	/*
	 * check if given class is already visited
	 */
	private boolean isVisited(Class<?> clazz, Class<? extends Annotation> annotation) {
		return visitedClasses.containsKey(clazz) && visitedClasses.get(clazz).containsKey(annotation);
	}
	
	/*
	 * visits all methods of given class and his interfaces and integrates them to the given list
	 */
	private List<Method> getAnnotatedMethods(Class<?> initClass, Class<? extends Annotation> annotation, List<Method> presentMethods) {
		
		List<Method> annotatedMethods = new ArrayList<Method>(presentMethods);

		Class<?>[] interfaces = initClass.getInterfaces();
		for (Class<?> interfaceObject : interfaces) {
			List<Method> interfaceMethods = getAnnotatedInterfaceMethods(interfaceObject, annotation);
			for (Method method : interfaceMethods) {
				replaceMethod(method, annotatedMethods, annotation, false);
			}
		}
		
		Method[] methods = initClass.getDeclaredMethods();
		for (Method method : methods) {
			replaceMethod(method, annotatedMethods, annotation, false);			
		}
		
		return annotatedMethods;
	}
	
	/*
	 * returns all interface methods that are annotated
	 */
	private List<Method> getAnnotatedInterfaceMethods(Class<?> interfaceObject, Class<? extends Annotation> annotation) {
		if (isVisited(interfaceObject, annotation)) {
			return visitedClasses.get(interfaceObject).get(annotation);
		}
		
		List<Method> methodList = new ArrayList<Method>();
		Method[] methods = interfaceObject.getMethods();

		for (Method method : methods) {
			if (method.isAnnotationPresent(annotation)) {
				methodList.add(method);
			}
		}
		
		setVisited(interfaceObject, annotation, methodList);

		return methodList;
	}

	/*
	 * 
	 */
	private void replaceMethod(Method method, List<Method> methodList, Class<? extends Annotation> annotation, boolean alwaysAdd) {
		Method similarMethod = getSimilarMethod(method, methodList);
		if (similarMethod != null) {
			if (Modifier.isAbstract(similarMethod.getModifiers()) || !Modifier.isAbstract(method.getModifiers())) {
				methodList.remove(similarMethod);
				methodList.add(method);				
			}
		} else if (method.isAnnotationPresent(annotation) || alwaysAdd) {
			methodList.add(method);
		}		
	}
	
	/*
	 * returns a similar method from the list (similar means that name and parameter types are equal)
	 */
	private Method getSimilarMethod(Method method, List<Method> methodList) {		
		for (Method methodElement : methodList) {
			if (methodElement.getName().equals(method.getName()) &&
					Arrays.equals(methodElement.getParameterTypes(), method.getParameterTypes())) {
				return methodElement;
			}
		}
		
		return null;
	}
	
	private void setVisited(Class<?> clazz, Class<? extends Annotation> annotation, List<Method> methodList) {
		Map<Class<? extends Annotation>, List<Method>> element = visitedClasses.get(clazz);
		if (element == null) {
			element = new HashMap<Class<? extends Annotation>, List<Method>>();
			visitedClasses.put(clazz, element);
		}
		
		element.put(annotation, methodList);
	}
}
