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
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import rtt.annotations.AnnotationProcessor;
import rtt.annotations.Parser;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.input.Input;
import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.AttributeList;
import rtt.core.archive.output.ChildrenList;
import rtt.core.archive.output.Node;

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
		parserAnnotation = annotationProcessor.getAnnotation(Parser.class);
	}

	public void loadInput(Input i) throws Exception {
		parser = loadInputImpl(i, Parser.Initialize.class, annotationProcessor);
	}

	public List<Node> getAst() throws Exception {
		List<Node> result = new LinkedList<Node>();
		Method astMethod = getSingleMethod(Parser.AST.class, annotationProcessor);
		Object o = astMethod.invoke(parser);
		if (o == null)
			return null;
		// can be an iterable or a normal AST

		if (Iterable.class.isAssignableFrom(o.getClass())
				&& !o.getClass().isAnnotationPresent(Parser.Node.class)) {
			Iterable io = (Iterable) o;
			for (Object tree : io) {
				result.add(createNode(tree));
			}
		} else
			result.add(createNode(o));

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

	private Node createNode(Object curObj) throws Exception {

		Node n = new Node();
		if (curObj == null) {
			n.setIsNull(true);
			return n;
		}
		
		AnnotationProcessor nodeProc = new AnnotationProcessor(curObj
				.getClass());
		// test, if node-annotation is present
		try {
			nodeProc.getAnnotation(Parser.Node.class);
		} catch (Exception e) {
			System.err.println("Warning:" + Parser.Node.class.toString()
					+ " not present at " + curObj.getClass());
			return null;
		}
		
		n.setSimpleName(curObj.getClass().getSimpleName());
		n.setFullName(curObj.getClass().getName());
		n.setAttributes(new AttributeList());
		
		List<Attribute> l = n.getAttributes().getAttribute();

		addMethods(curObj, nodeProc, l, Parser.Node.Compare.class, false);
		addMethods(curObj, nodeProc, l, Parser.Node.Informational.class, true);
		addFields(curObj, nodeProc, l, Parser.Node.Compare.class, false);
		addFields(curObj, nodeProc, l, Parser.Node.Informational.class, true);
		sortNodeAttribs(l);

		ChildrenList children = new ChildrenList();

		List<Method> ms = nodeProc
				.getMethodsWithAnnotation(Parser.Node.Child.class);
		for (Method m : ms) {
			Object c = m.invoke(curObj);
			if (c instanceof Iterable) {

				Iterable tmp = (Iterable) c;
				for (Object o : tmp)
					children.getNode().add(createNode(o));
			} else
				children.getNode().add(createNode(c));
		}

		List<Field> fs = nodeProc
				.getFieldsWithAnnotation(Parser.Node.Child.class);
		for (Field f : fs) {
			Object c = f.get(curObj);
			if (c instanceof Iterable) {
				Iterable tmp = (Iterable) c;
				for (Object o : tmp)
					children.getNode().add(createNode(o));
			} else
				children.getNode().add(createNode(c));
		}
		if (children.getNode().size() > 0)
			n.setChildren(children);
		
//		 CHRISTIAN
//		Publisher.publish(n);

		return n;

	}

	private void addMethods(Object tokenObj, AnnotationProcessor tokenProc,
			List<Attribute> l, Class clazz, boolean informational)
			throws Exception {
		List<Method> compareMethods = tokenProc.getMethodsWithAnnotation(clazz);
		for (Method m : compareMethods) {
			Attribute ta = new Attribute();

			String name = "";
			Annotation a = m.getAnnotation(clazz);
			if (informational)
				name = ((Parser.Node.Informational) a).value();
			else
				name = ((Parser.Node.Compare) a).value();

			ta.setName(name.equals("") ? m.getName().replace("get", "") : name);
			Object v = m.invoke(tokenObj);
			String value = v == null ? "" : v.toString();
			ta.setValue(value);

			ta.setInformational(informational);

			l.add(ta);
		}
	}

	private void addFields(Object tokenObj, AnnotationProcessor tokenProc,
			List<Attribute> l, Class clazz, boolean informational)
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
