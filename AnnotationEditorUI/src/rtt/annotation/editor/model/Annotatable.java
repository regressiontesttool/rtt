package rtt.annotation.editor.model;

import rtt.annotation.editor.controller.rules.Annotation;

public abstract class Annotatable<T extends ModelElement<?>> extends ModelElement<T> {
	
	protected Annotatable(T parent) {
		super(parent);
	}

	private Annotation annotation = Annotation.NONE;
	private boolean changed = false;
	
	public final void setAnnotation(Annotation annotation) {
		if (annotation == null) {
			throw new IllegalArgumentException("Annotation can not be null.");
		}
		
		this.annotation = annotation;
	}
	
	public final Annotation getAnnotation() {
		return annotation;
	}
	
	public final boolean hasAnnotation()  {
		return annotation != Annotation.NONE;
	}
	
	public final boolean hasChanged() {
		return changed;
	}
	
	public final void setChanged(boolean changed) {
		this.changed = changed;
	}
}
