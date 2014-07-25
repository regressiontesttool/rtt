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
 * <td>The AST method returns a class, annotated with {@link Node}, which
 * represents the parsed AST. can return the ast or an iterable of ASTs (forest)
 * </td>
 * </tr>
 * </table>
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
}
