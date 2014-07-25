/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.core.testing.generation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import rtt.annotations.Parser;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.input.Input;

@Deprecated
public class ParserExecutor {	

//	private <A extends Annotation> void addMethods(Object tokenObj, AnnotationProcessor tokenProc,
//			Node node, Class<A> annotationClass, boolean informational)
//			throws Throwable {
//		
//		List<Method> methods = tokenProc.getMethodsWithAnnotation(annotationClass);
//
//		for (Method method : methods) {		
//
//			String methodName = "";
//			Annotation annotation = method.getAnnotation(annotationClass);
//			if (annotation != null) {
//				if (informational)
//					methodName = ((Parser.Node.Informational) annotation).value();
//				else
//					methodName = ((Parser.Node.Compare) annotation).value();
//			}
//			
//			if (methodName.trim().isEmpty()) {
//				methodName = method.getName().replace("get", "");
//			}
//			
//			try {
//				method.setAccessible(true);
//				Object value = method.invoke(tokenObj);
//				addNode(node, value, methodName);
//			} catch (Throwable throwable) {
//				if (throwable instanceof InvocationTargetException) {
//					throwable = throwable.getCause();
//				}
//				
//				if (!isAcceptedException(throwable)) {
//					throw throwable;
//				}
//				
//				String throwableName = throwable.getClass().getName();
//				RTTLogging.warn("WARNING: accepted " + throwableName + " has been thrown.");
//			}
//			
////			attribute.setName(methodName);
////			attribute.setInformational(informational);
////			
////			Object methodResult = null;
////			try {
////				method.setAccessible(true);
////				methodResult = method.invoke(tokenObj);
////			} catch (Throwable throwable) {
////				if (throwable instanceof InvocationTargetException) {
////					throwable = throwable.getCause();
////				}
////				
////				if (!isAcceptedException(throwable)) {
////					throw throwable;
////				}
////				
////				// TODO was tun wenn exception ?
////				String throwableName = throwable.getClass().getName();
////				RTTLogging.warn("WARNING: accepted " + throwableName + " has been thrown.");
////				methodResult = "EXCEPTION: " + throwableName;
////			}
////			
////			if (methodResult.getClass().isAnnotationPresent(Parser.Node.class)) {
////				result.add(createNode(methodResult, astMethod.getName()));
////			} else if (methodResult instanceof Object[]) {
////				Object[] items = (Object[]) methodResult;
////				for (Object item : items) {
////					result.add(createNode(item, astMethod.getName()));
////				}
////			} else if (methodResult instanceof Iterable<?>) {
////				Iterable<?> iterable = (Iterable<?>) methodResult;
////				for (Object item : iterable) {
////					result.add(createNode(item, astMethod.getName()));
////				}
////			}
//			
//			
//			
//			
////			String value = "";
////			if (methodResult != null) {
////				value = methodResult.toString();
////			}
////
////			
////			attribute.setValue(value);
////			
////
////			node.add(attribute);
//		}
//	}
//
//	private <A extends Annotation> void addFields(Object tokenObj, AnnotationProcessor tokenProc,
//			Node node, Class<A> annotationClass, boolean informational)
//			throws Throwable {
//		List<Field> compareFields = tokenProc.getFieldsWithAnnotation(annotationClass);
//		
//		for (Field field : compareFields) {
//			String fieldname = "";
//			
//			Annotation a = field.getAnnotation(annotationClass);
//			if (informational) {
//				fieldname = ((Parser.Node.Informational) a).value();
//			} else {
//				fieldname = ((Parser.Node.Compare) a).value();
//			}
//			
//			if (fieldname.trim().isEmpty()) {
//				fieldname = field.getName().replace("get", "");
//			}
//			
//			try {
//				Object value = field.get(tokenObj);
//				addNode(node, value, fieldname);
//			} catch (Throwable throwable) {
//				if (throwable instanceof InvocationTargetException) {
//					throwable = throwable.getCause();
//				}
//				
//				if (!isAcceptedException(throwable)) {
//					throw throwable;
//				}
//				
//				String throwableName = throwable.getClass().getName();
//				RTTLogging.warn("WARNING: accepted " + throwableName + " has been thrown.");
//			}
//		}
//	}
}
