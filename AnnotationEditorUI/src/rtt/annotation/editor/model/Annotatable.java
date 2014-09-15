package rtt.annotation.editor.model;

import rtt.annotations.Node.Value;

/**
 * Adds the possibility to set/unset {@link Annotation}s 
 * to a {@link ModelElement}.
 *  
 * @author Christian Oelsner <C.Oelsner@web.de>
 * 
 */
public abstract class Annotatable extends ModelElement {
	
	protected Annotatable(ModelElement parent) {
		super(parent);
	}

	@Value private Annotation annotation;
	
	/**
	 * Sets an {@link Annotation} to the current element.
	 * If {@code null} was given, the current annotation will be erased.
	 * @param annotation a new {@link Annotation} or {@code null}
	 */
	public final void setAnnotation(Annotation annotation) {		
		this.annotation = annotation;
	}
	
	/**
	 * Returns the current {@link Annotation} or {@code null},
	 * if the element has no annotation.
	 * 
	 * @return an {@link Annotation} or {@code null}
	 */
	public final Annotation getAnnotation() {
		return annotation;
	}
	
	/**
	 * Returns {@code true}, if element has an annotation.
	 * @return {@code true}, if annotation is set.
	 */
	public final boolean hasAnnotation()  {
		return annotation != null;
	}
}
