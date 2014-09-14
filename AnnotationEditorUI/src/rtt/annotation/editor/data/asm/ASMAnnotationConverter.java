package rtt.annotation.editor.data.asm;

import java.lang.annotation.Annotation;

import org.objectweb.asm.Type;

import rtt.annotation.editor.controller.rules.RTTAnnotation;
import rtt.annotation.editor.controller.rules.RTTAnnotation.AnnotationType;
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
	
	public static ASMAnnotationConverter findByAnnotation (Annotation annotation) {
		for (ASMAnnotationConverter annotationDescriptor : values()) {
			if (annotationDescriptor.annotation.equals(annotation)) {
				return annotationDescriptor;
			}
		}
		
		return null;
	}
	
	public static RTTAnnotation getAnnotation(String descriptor) {
		for (ASMAnnotationConverter annotationDescriptor : values()) {
			if (annotationDescriptor.descriptor.equals(descriptor)) {
				return RTTAnnotation.create(annotationDescriptor.annotation);
			}
		}
		
		return null;
	} 

	public boolean equalsAnnotation(Annotation annotation) {
		return annotation.equals(this.annotation);
	}
}
