package rtt.annotation.editor.data.asm;

import org.objectweb.asm.Type;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotations.Parser.Node;
import rtt.annotations.Parser.Node.Compare;
import rtt.annotations.Parser.Node.Informational;

public enum AnnotationDescriptor {
	
	NODE (Annotation.NODE, Type.getDescriptor(Node.class)),
	COMPARE (Annotation.COMPARE, Type.getDescriptor(Compare.class)),
	INFORMATIONAL (Annotation.INFORMATIONAL, Type.getDescriptor(Informational.class));
	
	private String descriptor;
	private Annotation annotation;

	private AnnotationDescriptor(Annotation annotation, String description) {
		this.descriptor = description;
		this.annotation = annotation;
	}
	
	public String getDescriptor() {
		return descriptor;
	}

	public static AnnotationDescriptor findAnnotation(String descriptor) {
		for (AnnotationDescriptor annotation : values()) {
			if (descriptor.equals(annotation.descriptor)) {
				return annotation;
			}
		}
		
		return null;
	}

	public boolean equalsAnnotation(Annotation annotation) {
		return annotation.equals(this.annotation);
	}
}
