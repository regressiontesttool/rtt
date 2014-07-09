package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;

import rtt.annotation.editor.model.Annotatable;

public abstract class AnnotationChanger {
	
	public static interface IAnnotationVisitor {
		AnnotationVisitor getVisitor(String descriptor, boolean visible);
	}
	
	protected final Annotatable<?> annotatable;
	
	protected AnnotationChanger(Annotatable<?> annotatable) {
		this.annotatable = annotatable;
	}

	protected abstract AnnotationVisitor checkAnnotation(
			IAnnotationVisitor visitor,	String desc, boolean visible);
	
	protected abstract void addAnnotation(IAnnotationVisitor visitor);
}
