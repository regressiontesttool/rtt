package rtt.annotation.editor.data.asm;

import org.objectweb.asm.Type;

import rtt.annotation.editor.model.Annotation;
import rtt.annotation.editor.model.Annotation.AnnotationType;
import rtt.annotations.Node;
import rtt.annotations.Node.Initialize;
import rtt.annotations.Node.Value;

public enum ASMAnnotationConverter {
	
	NODE (AnnotationType.NODE, Type.getDescriptor(Node.class)),	
	VALUE (AnnotationType.VALUE, Type.getDescriptor(Value.class)),
	INITIALIZE (AnnotationType.INITIALIZE, Type.getDescriptor(Initialize.class));
	
	private String descriptor;
	private AnnotationType annotation;

	private ASMAnnotationConverter(AnnotationType annotationType, String description) {
		this.descriptor = description;
		this.annotation = annotationType;
	}
	
	public String getDescriptor() {
		return descriptor;
	}
	
	public AnnotationType getAnnotation() {
		return annotation;
	}
	
	public static String getDescriptor(AnnotationType type) {
		for (ASMAnnotationConverter annotationDescriptor : values()) {
			if (annotationDescriptor.annotation.equals(type)) {
				return annotationDescriptor.descriptor;
			}
		}
		
		return null;
	}
	
	public static Annotation getAnnotation(String descriptor) {
		for (ASMAnnotationConverter annotationDescriptor : values()) {
			if (annotationDescriptor.descriptor.equals(descriptor)) {
				return Annotation.create(annotationDescriptor.annotation);
			}
		}
		
		return null;
	}
}
