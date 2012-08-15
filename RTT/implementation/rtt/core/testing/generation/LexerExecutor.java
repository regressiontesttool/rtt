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
import java.util.List;

import rtt.annotations.AnnotationProcessor;
import rtt.annotations.Lexer;
import rtt.core.archive.configuration.Classpath;
import rtt.core.archive.output.Attribute;
import rtt.core.archive.output.AttributeList;
import rtt.core.archive.output.Token;
import rtt.core.archive.input.Input;

/**
 * 
 * @author Peter Mucha
 * 
 */
public class LexerExecutor extends Executor {

	Object lexer;

	Method nextTokenMethod;
	Lexer lexerAnnotation;

	public LexerExecutor(String lexerClass, Classpath cp, String baseDir)
			throws Exception {
		
		super(lexerClass, cp, baseDir);
		lexerAnnotation = annotationProcessor.getAnnotation(Lexer.class);
	}

	@Override
	public void loadInput(Input i) throws Exception {
		lexer = loadInputImpl(i, Lexer.Initialize.class, annotationProcessor);
		nextTokenMethod = null;
	}

	public Token getToken() throws Exception {
		if (nextTokenMethod == null)
			nextTokenMethod = getSingleMethod(Lexer.NextToken.class, annotationProcessor);

		Object o = nextTokenMethod.invoke(lexer);		
		Token t = computeToken(o);
		
		return t;

	}

	private Token computeToken(Object tokenObj) throws Exception {
		Token result = new Token();
		
		result.setSimpleName(tokenObj.getClass().getSimpleName());
		result.setFullName(tokenObj.getClass().getName());
		result.setAttributes(new AttributeList());		
		
		AnnotationProcessor tokenProc = new AnnotationProcessor(tokenObj
				.getClass());
		// test, if annotation is present

		Lexer.Token tokenAnnotation = tokenProc
				.getAnnotation(Lexer.Token.class);

		Method eofMethod = null;
		try {
			eofMethod = getSingleMethod(Lexer.Token.EOF.class, tokenProc);
		} catch (Exception e) {
			// no Method of this type, try attribute
		}
		if (eofMethod != null) {
			if (!(eofMethod.getReturnType().equals(Boolean.class) || eofMethod
					.getReturnType().equals(boolean.class)))
				throw new Exception("EofCheck method has to return Boolean");

			result.setIsEof((Boolean) eofMethod.invoke(tokenObj));
		} else // eof could be an attribute
		{
			List<Field> fields = tokenProc
					.getFieldsWithAnnotation(Lexer.Token.EOF.class);
			if (fields.size() != 1)
				throw new Exception("No or too many EOF Annotations");

			if (!(fields.get(0).getType().equals(Boolean.class) || fields
					.get(0).getType().equals(boolean.class)))
				throw new Exception("EofCheck method has to be Boolean");

			result.setIsEof((Boolean) fields.get(0).get(tokenObj));
		}

		List<Attribute> l = result.getAttributes().getAttribute();
		// TODO: liste der strings abfragen

		// compare methods
		addMethods(tokenObj, tokenProc, l, Lexer.Token.Compare.class, false);
		addMethods(tokenObj, tokenProc, l, Lexer.Token.Informational.class,true);
		addFields(tokenObj, tokenProc, l, Lexer.Token.Compare.class, false);
		addFields(tokenObj, tokenProc, l, Lexer.Token.Informational.class, true);

		sortTokenAttribs(l);

		return result;
	}

	private void sortTokenAttribs(List<Attribute> l) {
		Comparator<Attribute> c = new Comparator<Attribute>() {

			@Override
			public int compare(Attribute o1, Attribute o2) {
				return o1.getName().compareTo(o2.getName());
			}
		};

		Collections.sort(l, c);
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
				name = ((Lexer.Token.Informational) a).value();
			else
				name = ((Lexer.Token.Compare) a).value();

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
				name = ((Lexer.Token.Informational) a).value();
			else
				name = ((Lexer.Token.Compare) a).value();

			ta.setName(name.equals("") ? f.getName().replace("get", "") : name);

			String value = f.get(tokenObj).toString();
			ta.setValue(value);

			ta.setInformational(informational);

			l.add(ta);
		}
	}

}