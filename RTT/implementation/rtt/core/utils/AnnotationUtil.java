package rtt.core.utils;

import java.lang.annotation.Annotation;

import rtt.annotations.Node;
import rtt.annotations.processing.AnnotationProcessor;

public class AnnotationUtil {
	
	private static final Class<? extends Annotation> NODE_ANNOTATION = Node.class;
//	private static final Class<? extends Annotation> COMPARE_ANNOTATION = Compare.class;
//	private static final Class<? extends Annotation> INFORMATIONAL_ANNOTATION = Informational.class;
	
	public static boolean isNode(Object object) {
		return object != null && isNode(object.getClass());
	}
	
	public static boolean isNode(Class<?> objectType) {
		return AnnotationProcessor.hasAnnotation(objectType, NODE_ANNOTATION);
	}
	
//	public static List<Field> getCompareFields(Object object) {
//		return AnnotationProcessor.getFields(object.getClass(), COMPARE_ANNOTATION);
//	}
//
//	public static List<Field> getInformationalFields(Object object) {
//		return AnnotationProcessor.getFields(object.getClass(), INFORMATIONAL_ANNOTATION);
//	}
//	
//	public static List<Method> getCompareMethods(Object object) {
//		return AnnotationProcessor.getMethods(object.getClass(), COMPARE_ANNOTATION);
//	}
//	
//	public static List<Method> getInformationalMethods(Object object) {
//		return AnnotationProcessor.getMethods(object.getClass(), INFORMATIONAL_ANNOTATION);
//	}
//	
//	public static boolean isInformational(Field field) {
//		return AnnotationProcessor.getFields(field.getDeclaringClass(), INFORMATIONAL_ANNOTATION).contains(field);
//	}
//
//	public static boolean isInformational(Method method) {
//		return AnnotationProcessor.getMethods(method.getDeclaringClass(), INFORMATIONAL_ANNOTATION).contains(method);
//	}

}
