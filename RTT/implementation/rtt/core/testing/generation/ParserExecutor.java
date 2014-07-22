/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.core.testing.generation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import rtt.annotations.AnnotationProcessor;
import rtt.annotations.Parser;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.Node;
import rtt.core.utils.RTTLogging;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class ParserExecutor extends Executor {

	Object parser;
	Parser parserAnnotation;

	public ParserExecutor(String parserClass, Classpath cp, String baseDir)
			throws Exception {
		
		super(parserClass, cp, baseDir);
		parserAnnotation = processor.getAnnotation(Parser.class);
	}

	@Override
	public void initialize(Input input, List<String> params) throws Throwable {
		
		if (parserAnnotation.withParams()) {
			setParams(params);
		}
		
		if (parserAnnotation.acceptedExceptions() != null) {
			setAcceptedExceptions(parserAnnotation.acceptedExceptions());
		}
		
		try {
			parser = initializeClass(input, Parser.Initialize.class);
		} catch (InvocationTargetException exception) {
			throw exception.getCause();
		}
	}

	public List<Node> getNodes() throws Throwable {
		List<Node> result = new LinkedList<Node>();
		
		Method astMethod = processor.getMethodWithAnnotation(Parser.AST.class);
		astMethod.setAccessible(true);
		
		Object methodResult = astMethod.invoke(parser);
		if (methodResult == null) {
			return null;
		}
		
		if (methodResult.getClass().isAnnotationPresent(Parser.Node.class)) {
			result.add(createNode(null, methodResult, astMethod.getName()));
		} else if (methodResult instanceof Object[]) {
			Object[] items = (Object[]) methodResult;
			for (Object item : items) {
				result.add(createNode(null, item, astMethod.getName()));
			}
		} else if (methodResult instanceof Iterable<?>) {
			Iterable<?> iterable = (Iterable<?>) methodResult;
			for (Object item : iterable) {
				result.add(createNode(null, 
						item, astMethod.getName()));
			}
		}		

		return result;

	}

//	private void sortNodeAttribs(List<Attribute> l) {
//		Comparator<Attribute> c = new Comparator<Attribute>() {
//
//			@Override
//			public int compare(Attribute o1, Attribute o2) {
//				return o1.getName().compareTo(o2.getName());
//			}
//		};
//
//		Collections.sort(l, c);
//	}

	private Node createNode(Node parentNode, Object curObj, String methodName) 
			throws Throwable {
		
		Node node = new Node();
		node.setMethod(methodName);
		
		if (curObj == null) {
			node.setIsNull(true);
			return node;
		}
		
		// TODO what if curObj is primitive type or iteratable ?
		
		AnnotationProcessor nodeProc = new AnnotationProcessor(curObj
				.getClass());
		// test, if node-annotation is present
		try {
			nodeProc.getAnnotation(Parser.Node.class);
		} catch (Exception e) {			
			RTTLogging.debug("Warning:" + Parser.Node.class.toString()
					+ " not present at " + curObj.getClass());
			
			if (parentNode != null) {
				Attribute attribute = new Attribute();
				attribute.setName(methodName);
				attribute.setValue(curObj.toString());
				attribute.setInformational(false);
				
				parentNode.getAttributes().add(attribute);
			}		
			
			return null;
		}
		
		node.setSimpleName(curObj.getClass().getSimpleName());
		node.setFullName(curObj.getClass().getName());

		addMethods(curObj, nodeProc, node, Parser.Node.Compare.class, false);
		addMethods(curObj, nodeProc, node, Parser.Node.Informational.class, true);
		addFields(curObj, nodeProc, node, Parser.Node.Compare.class, false);
		addFields(curObj, nodeProc, node, Parser.Node.Informational.class, true);
//		sortNodeAttribs(l);

//		List<Method> methodList = nodeProc.getMethodsWithAnnotation(Parser.Node.Child.class);
		
//		for (Method method : methodList) {
//			try {
//				Object value = method.invoke(curObj);
//				addNode(node, value, method.getName());
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

//		List<Field> fieldList = nodeProc
//				.getFieldsWithAnnotation(Parser.Node.Child.class);
//		
//		for (Field field : fieldList) {
//			Object value = field.get(curObj);
//			addNode(node, value, field.getName());
//		}

		return node;

	}

	private void addNode(Node parentNode, Node childNode) {
		if (parentNode != null && childNode != null && parentNode.getNodes() != null) {
			parentNode.getNodes().add(childNode);
		}
	}
	
	private void addNode(Node parentNode, Object object, String operationName) throws Throwable {
		
		if (object instanceof Object[]) {
			Object[] tmp = (Object[]) object;
			
			int i = 0;
			for (Object arrayItem : tmp) {
				Node newNode = createNode(parentNode, arrayItem, operationName + "[" + i + "]");
				addNode(parentNode, newNode);
				
				i++;
			}
		} else if (object instanceof Iterable<?>) {
			Iterable<?> tmp = (Iterable<?>) object;
			
			int i = 0;
			for (Object item : tmp) {
				Node newNode = createNode(parentNode, item, operationName + "[" + i + "]");
				addNode(parentNode, newNode);
				
				i++;
			}				
		} else if (object instanceof Map<?, ?>) {
			Map<?, ?> tmp = (Map<?, ?>) object;
			
			int i = 0;
			for (Entry<?, ?> entry : tmp.entrySet()) {
				Node keyNode = createNode(parentNode, entry.getKey(), operationName + "<" + i++ + ", ?>");
				addNode(keyNode, entry.getValue(), "mapValue");
				addNode(parentNode, keyNode);
			}			
		} else {
			Node newNode = createNode(parentNode, object, operationName);
			addNode(parentNode, newNode);
		}
	}

	private <A extends Annotation> void addMethods(Object tokenObj, AnnotationProcessor tokenProc,
			Node node, Class<A> annotationClass, boolean informational)
			throws Throwable {
		
		List<Method> methods = tokenProc.getMethodsWithAnnotation(annotationClass);

		for (Method method : methods) {		

			String methodName = "";
			Annotation annotation = method.getAnnotation(annotationClass);
			if (annotation != null) {
				if (informational)
					methodName = ((Parser.Node.Informational) annotation).value();
				else
					methodName = ((Parser.Node.Compare) annotation).value();
			}
			
			if (methodName.trim().isEmpty()) {
				methodName = method.getName().replace("get", "");
			}
			
			try {
				method.setAccessible(true);
				Object value = method.invoke(tokenObj);
				addNode(node, value, methodName);
			} catch (Throwable throwable) {
				if (throwable instanceof InvocationTargetException) {
					throwable = throwable.getCause();
				}
				
				if (!isAcceptedException(throwable)) {
					throw throwable;
				}
				
				String throwableName = throwable.getClass().getName();
				RTTLogging.warn("WARNING: accepted " + throwableName + " has been thrown.");
			}
			
//			attribute.setName(methodName);
//			attribute.setInformational(informational);
//			
//			Object methodResult = null;
//			try {
//				method.setAccessible(true);
//				methodResult = method.invoke(tokenObj);
//			} catch (Throwable throwable) {
//				if (throwable instanceof InvocationTargetException) {
//					throwable = throwable.getCause();
//				}
//				
//				if (!isAcceptedException(throwable)) {
//					throw throwable;
//				}
//				
//				// TODO was tun wenn exception ?
//				String throwableName = throwable.getClass().getName();
//				RTTLogging.warn("WARNING: accepted " + throwableName + " has been thrown.");
//				methodResult = "EXCEPTION: " + throwableName;
//			}
//			
//			if (methodResult.getClass().isAnnotationPresent(Parser.Node.class)) {
//				result.add(createNode(methodResult, astMethod.getName()));
//			} else if (methodResult instanceof Object[]) {
//				Object[] items = (Object[]) methodResult;
//				for (Object item : items) {
//					result.add(createNode(item, astMethod.getName()));
//				}
//			} else if (methodResult instanceof Iterable<?>) {
//				Iterable<?> iterable = (Iterable<?>) methodResult;
//				for (Object item : iterable) {
//					result.add(createNode(item, astMethod.getName()));
//				}
//			}
			
			
			
			
//			String value = "";
//			if (methodResult != null) {
//				value = methodResult.toString();
//			}
//
//			
//			attribute.setValue(value);
//			
//
//			node.add(attribute);
		}
	}

	private <A extends Annotation> void addFields(Object tokenObj, AnnotationProcessor tokenProc,
			Node node, Class<A> annotationClass, boolean informational)
			throws Throwable {
		List<Field> compareFields = tokenProc.getFieldsWithAnnotation(annotationClass);
		
		for (Field field : compareFields) {
			String fieldname = "";
			
			Annotation a = field.getAnnotation(annotationClass);
			if (informational) {
				fieldname = ((Parser.Node.Informational) a).value();
			} else {
				fieldname = ((Parser.Node.Compare) a).value();
			}
			
			if (fieldname.trim().isEmpty()) {
				fieldname = field.getName().replace("get", "");
			}
			
			try {
				Object value = field.get(tokenObj);
				addNode(node, value, fieldname);
			} catch (Throwable throwable) {
				if (throwable instanceof InvocationTargetException) {
					throwable = throwable.getCause();
				}
				
				if (!isAcceptedException(throwable)) {
					throw throwable;
				}
				
				String throwableName = throwable.getClass().getName();
				RTTLogging.warn("WARNING: accepted " + throwableName + " has been thrown.");
			}
		}
	}

	public String getSimpleName() {
		return parser.getClass().getSimpleName();
	}

	public String getFullName() {
		return parser.getClass().getName();
	}

}
