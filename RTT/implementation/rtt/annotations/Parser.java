/**
 * <copyright>
 *
 * This program and the accompanying materials are made available under the
 * terms of the MIT license (X11 license) which accompanies this distribution.
 *
 * </copyright>
 */
package rtt.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * There are several other Annotations that are needed when providing an parser
 * adapter:
 * 
 * <table>
 * <tr>
 * <td width="20%"><b>Name</b></td>
 * <td width="80%"><b>Purpose</b></td>
 * </tr>
 * <tr>
 * <td>{@link Parser.Initialize}</td>
 * <td>The Parser Input method. Has to accept an {@link java.io.InputStream}.
 * Can be ommitted, if a parser gets an inputstream in the constructor</td>
 * </tr>
 * <tr>
 * <td>{@link Parser.AST}</td>
 * <td>The AST method returns a class, annotated with {@link Parser.Node}, which
 * represents the parsed AST. can return the ast or an iterable of ASTs (forest)
 * </td>
 * </tr>
 * <tr>
 * <td>{@link Parser.Node}</td>
 * <td>The node type. Has to be returned by the {@link Parser.AST}.</td>
 * </tr>
 * </table>
 * 
 * 
 * @author Peter Mucha
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Parser {
	
	boolean withParams() default false;
	Class<? extends Throwable>[] acceptedExceptions() default {};

	@Target( { ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Initialize {
		
	}

	@Target(ElementType.METHOD)
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	public @interface AST {

	}

	/**
	 * The annotation type for the Node.
	 * 
	 * There are several other Annotations that are needed:
	 * 
	 * <table>
	 * <tr>
	 * <td width="20%"><b>Name</b></td>
	 * <td width="80%"><b>Purpose</b></td>
	 * </tr>
	 * <tr>
	 * <td>{@link Parser.Node.Compare}</td>
	 * <td>This Method or field is compared, when regression testing is done.</td>
	 * </tr>
	 * <tr>
	 * <td>{@link Parser.Node.Informational}</td>
	 * <td>This Method or field is just saved for informational purposes. If it
	 * differes, the test will not fail.</td>
	 * </tr>
	 * <tr>
	 * <td>{@link Parser.Node.Child}</td>
	 * <td>This Method or Field returns a class, annotated with
	 * {@link Parser.Node}. It represents the child(ren) of the current Node <br>
	 * <b> can be an iterable or just a class</b></td>
	 * </tr>
	 * </table>
	 * 
	 * 
	 * @author Peter Mucha
	 */
	@Target(ElementType.TYPE)
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Node {

		@Target( { ElementType.FIELD, ElementType.METHOD })
		@Inherited
		@Retention(RetentionPolicy.RUNTIME)
		public @interface Child {

		}

		@Target( { ElementType.FIELD, ElementType.METHOD })
		@Inherited
		@Retention(RetentionPolicy.RUNTIME)
		public @interface Compare {
			String value() default "";
		}

		@Target( { ElementType.FIELD, ElementType.METHOD })
		@Inherited
		@Retention(RetentionPolicy.RUNTIME)
		public @interface Informational {
			String value() default "";
		}

	}

}
