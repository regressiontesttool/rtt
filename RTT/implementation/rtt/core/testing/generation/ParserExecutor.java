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
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Parser;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.ClassNode;
import rtt.core.archive.output.Node;
import rtt.core.archive.output.Output;
import rtt.core.archive.output.ValueNode;

public class ParserExecutor extends Executor {
	
	private static final class CheckAnnotation {
		
		private static final Class<? extends Annotation> INFORMATIONAL_ANNOTATION = 
				rtt.annotations.Node.Informational.class;

		public static Method getASTMethod(Executor executor) {
			// TODO Auto-generated method stub
			return null;
		}
		
		public static boolean isNode(Object currentObject) {
			// TODO Auto-generated method stub
			return false;
		}

		public static List<Method> getAnnotatedMethods(Class<?> objectType) {
			// TODO Auto-generated method stub
			return null;
		}

		public static List<Field> getAnnotatedFields(Class<?> objectType) {
			// TODO Auto-generated method stub
			return null;
		}

		public static boolean isInformational(AnnotatedElement element) {
			return element.isAnnotationPresent(INFORMATIONAL_ANNOTATION);
		}
	}	

	private static final String NO_AST_METHOD = "Could not find a method annotated with @Parser.AST";
	private static final String NODE_NULL = "Resulting node was null.";	
	
	Object parser;
	Parser parserAnnotation;

	public ParserExecutor(String parserClass, Classpath cp, String baseDir)
			throws Exception {
		
		super(parserClass, cp, baseDir);
		parserAnnotation = processor.getAnnotation(Parser.class);
	}

	@Override
	public void initialize(Input input, List<String> params) throws Throwable {
//		
//		if (parserAnnotation.withParams()) {
//			setParams(params);
//		}
//		
//		if (parserAnnotation.acceptedExceptions() != null) {
//			setAcceptedExceptions(parserAnnotation.acceptedExceptions());
//		}
//		
//		try {
//			parser = initializeClass(input, Parser.Initialize.class);
//		} catch (InvocationTargetException exception) {
//			throw exception.getCause();
//		}
	}
	
	public void createOutput(Output outputData, Executor executor) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException {
		Method astMethod = getASTMethod(executor);
		if (astMethod == null) {
			throw new NoSuchMethodException(NO_AST_METHOD);
		}
		
		astMethod.setAccessible(true);
		String astMethodName = astMethod.getName();
		Object astMethodResult = astMethod.invoke(executor);
		if (astMethodResult != null) {
			outputData.getNodes().addAll(createNodes(astMethodResult, astMethodName, false));
		}
	}

//	public List<Node> getNodes() throws Throwable {
//		return null;
//		List<Node> result = new LinkedList<Node>();
//		
//		Method astMethod = processor.getMethodWithAnnotation(Parser.AST.class);
//		astMethod.setAccessible(true);
//		
//		Object methodResult = astMethod.invoke(parser);
//		if (methodResult == null) {
//			return null;
//		}
//		
//		if (methodResult.getClass().isAnnotationPresent(Parser.Node.class)) {
//			result.add(createNode(null, methodResult, astMethod.getName()));
//		} else if (methodResult instanceof Object[]) {
//			Object[] items = (Object[]) methodResult;
//			for (Object item : items) {
//				result.add(createNode(null, item, astMethod.getName()));
//			}
//		} else if (methodResult instanceof Iterable<?>) {
//			Iterable<?> iterable = (Iterable<?>) methodResult;
//			for (Object item : iterable) {
//				result.add(createNode(null, 
//						item, astMethod.getName()));
//			}
//		}		
//
//		return result;
		
		
	private Method getASTMethod(Executor executor) {
		return CheckAnnotation.getASTMethod(executor);
	}
	
	private List<Node> createNodes(final Object currentObject, 
			final String generatedBy, final boolean isInformational) 
					throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		List<Node> resultList = new ArrayList<>();
		if (currentObject instanceof Object[]) {
			resultList.addAll(createNodes((Object[]) currentObject, generatedBy, isInformational));
		} else if (currentObject instanceof Iterable<?>) {
			resultList.addAll(createNodes((Iterable<?>) currentObject, generatedBy, isInformational));
		} else {
			resultList.add(createNode(currentObject, generatedBy, isInformational));
		}
		
		return resultList;
	}

	private List<Node> createNodes(final Object[] currentObject, final String generatedBy, final boolean isInformational) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Node> resultList = new ArrayList<>();
		for (Object item : currentObject) {
			resultList.addAll(createNodes(item, generatedBy, isInformational));
		}
		
		return resultList;
	}
	
	private List<Node> createNodes(final Iterable<?> currentObject, final String generatedBy, final boolean isInformational) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		List<Node> resultList = new ArrayList<>();
		for (Object item : currentObject) {
			resultList.addAll(createNodes(item, generatedBy, isInformational));
		}
		
		return resultList;
	}
	
	private Node createNode(final Object currentObject, final String generatedBy, boolean isInformational) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Node resultNode = null;
		
		if (currentObject == null) {
			resultNode = new Node();
			resultNode.setIsNull(true);
			resultNode.setGeneratorName(generatedBy);
		} else {
			if (hasNodeAnnotation(currentObject)) {
				resultNode = createClassNode(currentObject, generatedBy, isInformational);
			} else {
				resultNode = createValueNode(currentObject, generatedBy, isInformational);
			}
		}
		
		if (resultNode == null) {
			throw new IllegalStateException(NODE_NULL);
		}		
		
		return resultNode;
	}

	private boolean hasNodeAnnotation(Object currentObject) {
		return CheckAnnotation.isNode(currentObject);
	}

	private Node createClassNode(final Object currentObject, final String generatedBy, final boolean isInformational) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> objectType = currentObject.getClass();		
		ClassNode resultNode = new ClassNode();
		
		resultNode.setGeneratorName(generatedBy);
		resultNode.setFullName(objectType.getName());
		resultNode.setSimpleName(objectType.getSimpleName());
		resultNode.setInformational(isInformational);
		
		List<Method> annotatedMethods = CheckAnnotation.getAnnotatedMethods(objectType);
		boolean childIsInformational = isInformational;
		
		for (Method method : annotatedMethods) {
			method.setAccessible(true);
			childIsInformational = isInformational(method, isInformational);
			
			String methodName = method.getName();
			Object methodResult = method.invoke(currentObject);
			
			resultNode.getNodes().addAll(createNodes(methodResult, methodName, childIsInformational));
		}
		
		List<Field> annotatedFields = CheckAnnotation.getAnnotatedFields(objectType);
		
		for (Field field : annotatedFields) {
			field.setAccessible(true);
			childIsInformational = isInformational(field, isInformational);
			
			String fieldName = field.getName();
			Object fieldResult = field.get(currentObject);
			
			resultNode.getNodes().addAll(createNodes(fieldResult, fieldName, childIsInformational));
		}
		
		return resultNode;
	}
	
	private boolean isInformational(AnnotatedElement element, boolean parentIsInformational) {
		return !parentIsInformational && CheckAnnotation.isInformational(element);	
	}

	private Node createValueNode(final Object currentObject, final String generatedBy, final boolean isInformational) {
		ValueNode resultNode = new ValueNode();
		resultNode.setGeneratorName(generatedBy);
		resultNode.setValue(currentObject.toString());
		resultNode.setInformational(isInformational);

		return resultNode;
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

//	private Node createNode(Node parentNode, Object curObj, String methodName) 
//			throws Throwable {
//		
//		return null;
//		
//		Node node = new Node();
//		node.setMethod(methodName);
//		
//		if (curObj == null) {
//			node.setIsNull(true);
//			return node;
//		}
//		
//		// TODO what if curObj is primitive type or iteratable ?
//		
//		AnnotationProcessor nodeProc = new AnnotationProcessor(curObj
//				.getClass());
//		// test, if node-annotation is present
//		try {
//			nodeProc.getAnnotation(Parser.Node.class);
//		} catch (Exception e) {			
//			RTTLogging.debug("Warning:" + Parser.Node.class.toString()
//					+ " not present at " + curObj.getClass());
//			
//			if (parentNode != null) {
//				Attribute attribute = new Attribute();
//				attribute.setName(methodName);
//				attribute.setValue(curObj.toString());
//				attribute.setInformational(false);
//				
//				parentNode.getAttributes().add(attribute);
//			}		
//			
//			return null;
//		}
//		
//		node.setSimpleName(curObj.getClass().getSimpleName());
//		node.setFullName(curObj.getClass().getName());
//
//		addMethods(curObj, nodeProc, node, Parser.Node.Compare.class, false);
//		addMethods(curObj, nodeProc, node, Parser.Node.Informational.class, true);
//		addFields(curObj, nodeProc, node, Parser.Node.Compare.class, false);
//		addFields(curObj, nodeProc, node, Parser.Node.Informational.class, true);
////		sortNodeAttribs(l);
//
////		List<Method> methodList = nodeProc.getMethodsWithAnnotation(Parser.Node.Child.class);
//		
////		for (Method method : methodList) {
////			try {
////				Object value = method.invoke(curObj);
////				addNode(node, value, method.getName());
////			} catch (Throwable throwable) {
////				if (throwable instanceof InvocationTargetException) {
////					throwable = throwable.getCause();
////				}
////				
////				if (!isAcceptedException(throwable)) {
////					throw throwable;
////				}
////				
////				String throwableName = throwable.getClass().getName();
////				RTTLogging.warn("WARNING: accepted " + throwableName + " has been thrown.");
////			}							
////		}
//
////		List<Field> fieldList = nodeProc
////				.getFieldsWithAnnotation(Parser.Node.Child.class);
////		
////		for (Field field : fieldList) {
////			Object value = field.get(curObj);
////			addNode(node, value, field.getName());
////		}
//
//		return node;
//
//	}
		
//	private Node createNode(Node parentNode, Object curObj, String methodName) 
//			throws Throwable {
//		
//		Node node = new Node();
//		node.setMethod(methodName);
//		
//		if (curObj == null) {
//			node.setIsNull(true);
//			return node;
//		}
//		
//		// TODO what if curObj is primitive type or iteratable ?
//		
//		AnnotationProcessor nodeProc = new AnnotationProcessor(curObj
//				.getClass());
//		// test, if node-annotation is present
//		try {
//			nodeProc.getAnnotation(Parser.Node.class);
//		} catch (Exception e) {			
//			RTTLogging.debug("Warning:" + Parser.Node.class.toString()
//					+ " not present at " + curObj.getClass());
//			
//			if (parentNode != null) {
//				Attribute attribute = new Attribute();
//				attribute.setName(methodName);
//				attribute.setValue(curObj.toString());
//				attribute.setInformational(false);
//				
//				parentNode.getAttributes().add(attribute);
//			}		
//			
//			return null;
//		}
//		
//		node.setSimpleName(curObj.getClass().getSimpleName());
//		node.setFullName(curObj.getClass().getName());
//
//		addMethods(curObj, nodeProc, node, Parser.Node.Compare.class, false);
//		addMethods(curObj, nodeProc, node, Parser.Node.Informational.class, true);
//		addFields(curObj, nodeProc, node, Parser.Node.Compare.class, false);
//		addFields(curObj, nodeProc, node, Parser.Node.Informational.class, true);
//		sortNodeAttribs(l);

//	private void addNode(Node parentNode, Node childNode) {
//		if (parentNode != null && childNode != null && parentNode.getNodes() != null) {
//			parentNode.getNodes().add(childNode);
//		}
//	}
	
//	private void addNode(Node parentNode, Object object, String operationName) throws Throwable {
//		
//		if (object instanceof Object[]) {
//			Object[] tmp = (Object[]) object;
//			
//			int i = 0;
//			for (Object arrayItem : tmp) {
//				Node newNode = createNode(parentNode, arrayItem, operationName + "[" + i + "]");
//				addNode(parentNode, newNode);
//				
//				i++;
//			}
//		} else if (object instanceof Iterable<?>) {
//			Iterable<?> tmp = (Iterable<?>) object;
//			
//			int i = 0;
//			for (Object item : tmp) {
//				Node newNode = createNode(parentNode, item, operationName + "[" + i + "]");
//				addNode(parentNode, newNode);
//				
//				i++;
//			}				
//		} else if (object instanceof Map<?, ?>) {
//			Map<?, ?> tmp = (Map<?, ?>) object;
//			
//			int i = 0;
//			for (Entry<?, ?> entry : tmp.entrySet()) {
//				Node keyNode = createNode(parentNode, entry.getKey(), operationName + "<" + i++ + ", ?>");
//				addNode(keyNode, entry.getValue(), "mapValue");
//				addNode(parentNode, keyNode);
//			}			
//		} else {
//			Node newNode = createNode(parentNode, object, operationName);
//			addNode(parentNode, newNode);
//		}
//	}
//
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
