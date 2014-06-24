package rtt.annotation.editor.model;

import rtt.annotation.editor.rules.Annotation;

public abstract class Annotatable<T extends ModelElement<?>> extends ModelElement<T> {
	
	protected Annotatable(T parent) {
		super(parent);
	}

	private Annotation annotation = Annotation.NONE;
	
	public final void setAnnotation(Annotation annotation) {
		this.annotation = annotation;
	}
	
	public final Annotation getAnnotation() {
		return annotation;
	}
	
	public final boolean hasAnnotation()  {
		return annotation != Annotation.NONE;
	}
}
