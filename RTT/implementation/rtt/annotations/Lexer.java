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
 * The annotation type for the lexer-adapter the loaded lexerclass has to be
 * annotated by this Annotation.
 * 
 * There are several other Annotations that are needed when providing an lexer
 * adapter:
 * 
 * <table>
 * <tr>
 * <td width="20%"><b>Name</b></td>
 * <td width="80%"><b>Purpose</b></td>
 * </tr>
 * <tr>
 * <td>{@link Lexer.Initialize}</td>
 * <td>The Lexer Input method. Has to accept an {@link java.io.InputStream}</td>
 * </tr>
 * <tr>
 * <td>{@link Lexer.NextToken}</td>
 * <td>The Lexer Token Method. Has no arguments and has to return a class
 * annotated with {@link Lexer.Token}. With every call, the next token is
 * returned</td>
 * </tr>
 * <tr>
 * <td>{@link Lexer.Token}</td>
 * <td>The token type. Has to be returned by the {@link Lexer.NextToken}</td>
 * </tr>
 * </table>
 * 
 * 
 * @author Peter Mucha
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface Lexer {
	
	boolean withParams() default false;
	Class<? extends Throwable>[] acceptedExceptions() default {};
	
	/**
	 * returns one token per call the returned token has to be annotated with
	 * {@link Lexer.Token}
	 * 
	 */
	String tokenMethod() default "";
	
	

	@Target(ElementType.METHOD)
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Deprecated
	public @interface NextToken {

	}

	/**
	 * method for loading the input has to accept only one inputStream
	 * 
	 */
	String inputMethod() default "";

	@Target( { ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Deprecated
	public @interface Initialize {
		
	}

	/**
	 * The annotation type for the Token.
	 * 
	 * There are several other Annotations that are needed:
	 * 
	 * <table>
	 * <tr>
	 * <td width="20%"><b>Name</b></td>
	 * <td width="80%"><b>Purpose</b></td>
	 * </tr>
	 * <tr>
	 * <td>{@link Lexer.Token.Compare}</td>
	 * <td>This Method or field is compared, when regression testing is done.</td>
	 * </tr>
	 * <tr>
	 * <td>{@link Lexer.Token.Informational}</td>
	 * <td>This Method or field is just saved for informational purposes. If it
	 * differes, the test will not fail.</td>
	 * </tr>
	 * <tr>
	 * <td>{@link Lexer.Token.EOF}</td>
	 * <td>The Method or Field, which returns a boolean value, stating that this
	 * token is the EOF token.</td>
	 * </tr>
	 * </table>
	 * 
	 * 
	 * @author Peter Mucha
	 */
	@Target(ElementType.TYPE)
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	@Deprecated
	public @interface Token {
		/**
		 * the xml-name of the specified token defaut: className
		 */
		String value() default "";

		/**
		 * the prefix of logged value-getters normaly "getXX"
		 * 
		 * @return the prefix
		 */
		String prefix() default "get";

		@Target( { ElementType.FIELD, ElementType.METHOD })
		@Inherited
		@Retention(RetentionPolicy.RUNTIME)
		public @interface Informational {
			String value() default "";
		}

		@Target( { ElementType.FIELD, ElementType.METHOD })
		@Inherited
		@Retention(RetentionPolicy.RUNTIME)
		public @interface EOF {

		}

		@Target( { ElementType.FIELD, ElementType.METHOD })
		@Inherited
		@Retention(RetentionPolicy.RUNTIME)
		public @interface Compare {
			String value() default "";
		}
	}

}
