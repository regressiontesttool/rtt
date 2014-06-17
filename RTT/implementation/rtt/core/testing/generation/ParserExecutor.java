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
import java.util.Collections;
import java.util.Comparator;
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

	public List<Node> getAst() throws Throwable {
		List<Node> result = new LinkedList<Node>();
		Method astMethod = processor.getMethodWithAnnotation(Parser.AST.class);
		Object o = astMethod.invoke(parser);
		if (o == null)
			return null;
		// can be an iterable or a normal AST

		if (Iterable.class.isAssignableFrom(o.getClass())
				&& !o.getClass().isAnnotationPresent(Parser.Node.class)) {
			Iterable<?> io = (Iterable<?>) o;
			for (Object tree : io) {
				result.add(createNode(tree, astMethod.getName()));
			}
		} else {
			result.add(createNode(o, astMethod.getName()));
		}
			

		return result;

	}

	private void sortNodeAttribs(List<Attribute> l) {
		Comparator<Attribute> c = new Comparator<Attribute>() {

			@Override
			public int compare(Attribute o1, Attribute o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};

		Collections.sort(l, c);
	}

	private Node createNode(Object curObj, String methodName) throws Throwable {

		Node node = new Node();
		node.setMethod(methodName);
		
		if (curObj == null) {
			node.setIsNull(true);
			return node;
		}
		
		node.setSimpleName(curObj.getClass().getSimpleName());
		node.setFullName(curObj.getClass().getName());
		
		AnnotationProcessor nodeProc = new AnnotationProcessor(curObj
				.getClass());
		// test, if node-annotation is present
		try {
			nodeProc.getAnnotation(Parser.Node.class);
		} catch (Exception e) {
			System.err.println("Warning:" + Parser.Node.class.toString()
					+ " not present at " + curObj.getClass());
			return node;
		}	
		
		List<Attribute> l = node.getAttributes();

		addMethods(curObj, nodeProc, l, Parser.Node.Compare.class, false);
		addMethods(curObj, nodeProc, l, Parser.Node.Informational.class, true);
		addFields(curObj, nodeProc, l, Parser.Node.Compare.class, false);
		addFields(curObj, nodeProc, l, Parser.Node.Informational.class, true);
		sortNodeAttribs(l);

		List<Method> methodList = nodeProc.getMethodsWithAnnotation(Parser.Node.Child.class);
		
		for (Method method : methodList) {
			try {
				Object value = method.invoke(curObj);
				addNode(node, value, method.getName());
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

		List<Field> fieldList = nodeProc
				.getFieldsWithAnnotation(Parser.Node.Child.class);
		
		for (Field field : fieldList) {
			Object value = field.get(curObj);
			addNode(node, value, field.getName());
		}

		return node;

	}

	private void addNode(Node parentNode, Node childNode) {
		if (parentNode.getNodes() != null) {
			parentNode.getNodes().add(childNode);
		}
	}
	
	private void addNode(Node parentNode, Object object, String operationName) throws Throwable {
		
		if (object instanceof Iterable) {
			Iterable<?> tmp = (Iterable<?>) object;
			
			int i = 0;
			for (Object o : tmp) {
				Node newNode = createNode(o, operationName + "[" + i + "]");
				addNode(parentNode, newNode);
				
				i++;
			}				
		} else if (object instanceof Map<?, ?>) {
			Map<?, ?> tmp = (Map<?, ?>) object;
			
			int i = 0;
			for (Entry<?, ?> entry : tmp.entrySet()) {
				Node keyNode = createNode(entry.getKey(), operationName + "<" + i++ + ", ?>");
				addNode(keyNode, entry.getValue(), "mapValue");
				addNode(parentNode, keyNode);
			}			
		} else {
			Node newNode = createNode(object, operationName);
			addNode(parentNode, newNode);
		}
	}

	private <A extends Annotation>void addMethods(Object tokenObj, AnnotationProcessor tokenProc,
			List<Attribute> l, Class<A> clazz, boolean informational)
			throws Throwable {

		for (Method method : tokenProc.getMethodsWithAnnotation(clazz)) {
			Attribute attribute = new Attribute();

			String name = "";
			Annotation annotation = method.getAnnotation(clazz);
			if (annotation != null) {
				if (informational)
					name = ((Parser.Node.Informational) annotation).value();
				else
					name = ((Parser.Node.Compare) annotation).value();
			}
			
			if (name.trim().isEmpty()) {
				name = method.getName().replace("get", "");
			}
			
			Object returnValue = null;
			try {
				returnValue = method.invoke(tokenObj);
			} catch (Throwable throwable) {
				if (throwable instanceof InvocationTargetException) {
					throwable = throwable.getCause();
				}
				
				if (!isAcceptedException(throwable)) {
					throw throwable;
				}
				
				// TODO was tun wenn exception ?
				String throwableName = throwable.getClass().getName();
				RTTLogging.warn("WARNING: accepted " + throwableName + " has been thrown.");
				returnValue = "EXCEPTION: " + throwableName;
			}
			
			String value = "";
			if (returnValue != null) {
				value = returnValue.toString();
			}

			attribute.setName(name);
			attribute.setValue(value);
			attribute.setInformational(informational);

			l.add(attribute);
		}
	}

	private <A extends Annotation> void addFields(Object tokenObj, AnnotationProcessor tokenProc,
			List<Attribute> l, Class<A> clazz, boolean informational)
			throws Exception {
		List<Field> compareFields = tokenProc.getFieldsWithAnnotation(clazz);
		for (Field f : compareFields) {
			Attribute ta = new Attribute();

			String name = "";
			Annotation a = f.getAnnotation(clazz);
			if (informational)
				name = ((Parser.Node.Informational) a).value();
			else
				name = ((Parser.Node.Compare) a).value();

			ta.setName(name.equals("") ? f.getName().replace("get", "") : name);

			String value = f.get(tokenObj).toString();
			ta.setValue(value);

			ta.setInformational(informational);

			l.add(ta);
		}
	}

	public String getSimpleName() {
		return parser.getClass().getSimpleName();
	}

	public String getFullName() {
		return parser.getClass().getName();
	}

}
