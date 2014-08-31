package rtt.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation type for a Node.
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
 * Can be an {@link Iterable}, an array or a single object. If a single given object is
 * also annotated with a {@link Node} annotation it will be checked for 
 * further {@link Compare} or {@link Informational} annotations.
 * {@link Iterable}s and arrays will be expanded automatically during output generation.</td>
 * </tr>
 * <tr>
 * <td>{@link Node.Informational}</td>
 * <td>This method or field is just saved for informational purposes. If it
 * differs, the test will not fail. Can be an {@link Iterable}, an array or 
 * a single object. If the given object is also annotated with a {@link Node} 
 * annotation it will be checked for further {@link Compare} or {@link Informational} 
 * annotations, <i>but all children will be saved as {@link Informational}</i>. 
 * {@link Iterable}s and arrays will be expanded automatically 
 * during output generation</td>
 * </tr>
 * <tr>
 * <td>{@link Node.Initialize}</td>
 * <td>If a method or constructor is annotated with {@link Initialize} it 
 * will be used as a starting point for the creation of the output data. 
 * An annotated method or constructor must accept at least an {@link java.io.InputStream}
 * as parameter. If the {@link Initialize.withParams} option is used, it also must accept
 * a {@link String} array as second parameter.</td>
 * </tr>
 * </table>
 */
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface Node {
	
	@Target({ElementType.FIELD})
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Address {}
	
	@Target( { ElementType.METHOD, ElementType.CONSTRUCTOR })
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Initialize {
		boolean withParams() default false;
		Class<? extends Throwable>[] acceptedExceptions() default {};
	}

	@Target( { ElementType.FIELD, ElementType.METHOD })
	@Inherited
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Value {
		int index() default 100;
		String name() default "";
		boolean informational() default false;
	}
}