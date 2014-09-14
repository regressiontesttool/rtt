package rtt.annotation.editor.model;


public abstract class Annotatable<T extends ModelElement<?>> extends ModelElement<T> {
	
	protected Annotatable(T parent) {
		super(parent);
	}

	private RTTAnnotation annotation;
	
	public final void setAnnotation(RTTAnnotation annotation) {		
		this.annotation = annotation;
	}
	
	public final RTTAnnotation getAnnotation() {
		return annotation;
	}
	
	public final boolean hasAnnotation()  {
		return annotation != null;
	}
}
