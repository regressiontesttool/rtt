package rtt.annotation.editor.data.asm;

import org.objectweb.asm.Type;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotations.Node;
import rtt.annotations.Node.Initialize;
import rtt.annotations.Node.Value;

public enum ASMAnnotationConverter {
	
	NODE (Annotation.NODE, Type.getDescriptor(Node.class)),	
	VALUE (Annotation.VALUE, Type.getDescriptor(Value.class)),
	INITIALIZE (Annotation.INITIALIZE, Type.getDescriptor(Initialize.class));
	
	private String descriptor;
	private Annotation annotation;

	private ASMAnnotationConverter(Annotation annotation, String description) {
		this.descriptor = description;
		this.annotation = annotation;
	}
	
	public String getDescriptor() {
		return descriptor;
	}
	
	public Annotation getAnnotation() {
		return annotation;
	}

	public static ASMAnnotationConverter findByDescriptor(String descriptor) {
		for (ASMAnnotationConverter annotationDescriptor : values()) {
			if (annotationDescriptor.descriptor.equals(descriptor)) {
				return annotationDescriptor;
			}
		}
		
		return null;
	}
	
	public static ASMAnnotationConverter findByAnnotation (Annotation annotation) {
		for (ASMAnnotationConverter annotationDescriptor : values()) {
			if (annotationDescriptor.annotation.equals(annotation)) {
				return annotationDescriptor;
			}
		}
		
		return null;
	}

	public boolean equalsAnnotation(Annotation annotation) {
		return annotation.equals(this.annotation);
	}
}
