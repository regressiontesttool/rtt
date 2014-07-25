package rtt.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
 * <td>{@link Node.Compare}</td>
 * <td>This method or field is compared, when regression testing is done.
 * Can be an {@link Iterable}, an array or another 
 * class annotated with Parser.Node</td>
 * </tr>
 * <tr>
 * <td>{@link Node.Informational}</td>
 * <td>This method or field is just saved for informational purposes. If it
 * differs, the test will not fail. Can be an {@link Iterable}, an array or another 
 * class annotated with Parser.Node</td>
 * </tr>
 * </table>
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Node {

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