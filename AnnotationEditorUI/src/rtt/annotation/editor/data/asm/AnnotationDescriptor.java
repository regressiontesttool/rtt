package rtt.annotation.editor.data.asm;

import org.objectweb.asm.Type;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotations.Node;
import rtt.annotations.Node.Value;

public enum AnnotationDescriptor {
	
	NODE (Annotation.NODE, Type.getDescriptor(Node.class)),
	COMPARE (Annotation.COMPARE, Type.getDescriptor(Value.class)),
	INFORMATIONAL (Annotation.INFORMATIONAL, Type.getDescriptor(Value.class));
	
	private String descriptor;
	private Annotation annotation;

	private AnnotationDescriptor(Annotation annotation, String description) {
		this.descriptor = description;
		this.annotation = annotation;
	}
	
	public String getDescriptor() {
		return descriptor;
	}
	
	public Annotation getAnnotation() {
		return annotation;
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
