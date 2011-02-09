/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the BSD 3-clause license which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.managing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import rtt.annotations.AnnotationProcessor;
import rtt.annotations.Parser;
import rtt.archive.Classpath;
import rtt.archive.Input;
import rtt.archive.Node;
import rtt.archive.NodeAttribute;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class ParserExecuter extends Executer {

	AnnotationProcessor parserProc;
	Object parser;
	String parserClass;
	Parser parserAnnotation;

	public ParserExecuter(String parserClass, Classpath cp, String baseDir)
			throws Exception {

		Class c = this.loadClass(parserClass, cp, baseDir);

		this.parserClass = parserClass;
		parserProc = new AnnotationProcessor(c);
		parserAnnotation = parserProc.getAnnotation(Parser.class);
	}

	public void loadInput(Input i) throws Exception {
		parser = loadInputImpl(i, Parser.Initialize.class, parserProc);
	}

	public List<Node> getAst() throws Exception {
		List<Node> result = new LinkedList<Node>();
		Method astMethod = getSingleMethod(Parser.AST.class, parserProc);
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

	private void sortNodeAttribs(List<NodeAttribute> l) {
		Comparator<NodeAttribute> c = new Comparator<NodeAttribute>() {

			@Override
			public int compare(NodeAttribute o1, NodeAttribute o2) {
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

		List<NodeAttribute> l = n.getNodeAttribute();

		addMethods(curObj, nodeProc, l, Parser.Node.Compare.class, false);
		addMethods(curObj, nodeProc, l, Parser.Node.Informational.class, true);
		addFields(curObj, nodeProc, l, Parser.Node.Compare.class, false);
		addFields(curObj, nodeProc, l, Parser.Node.Informational.class, true);
		sortNodeAttribs(l);

		Node.Children children = new Node.Children();

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

		return n;

	}

	private void addMethods(Object tokenObj, AnnotationProcessor tokenProc,
			List<NodeAttribute> l, Class clazz, boolean informational)
			throws Exception {
		List<Method> compareMethods = tokenProc.getMethodsWithAnnotation(clazz);
		for (Method m : compareMethods) {
			NodeAttribute ta = new NodeAttribute();

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
			List<NodeAttribute> l, Class clazz, boolean informational)
			throws Exception {
		List<Field> compareFields = tokenProc.getFieldsWithAnnotation(clazz);
		for (Field f : compareFields) {
			NodeAttribute ta = new NodeAttribute();

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

}
