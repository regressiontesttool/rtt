package rtt.annotation.editor.data.asm;

import org.objectweb.asm.Type;

import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotation.editor.model.annotation.InitAnnotation;
import rtt.annotation.editor.model.annotation.NodeAnnotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;
import rtt.annotation.editor.model.annotation.Annotation.AnnotationType;
import rtt.annotations.Node;
import rtt.annotations.Node.Initialize;
import rtt.annotations.Node.Value;

public enum ASMAnnotationConverter {
	
	NODE (NodeAnnotation.class, Type.getDescriptor(Node.class)),	
	VALUE (ValueAnnotation.class, Type.getDescriptor(Value.class)),
	INITIALIZE (InitAnnotation.class, Type.getDescriptor(Initialize.class));
	
	private String descriptor;
	private Class<? extends Annotation> annotation;

	private ASMAnnotationConverter(Class<? extends Annotation> annotationType, String description) {
		this.descriptor = description;
		this.annotation = annotationType;
	}
	
	public String getDescriptor() {
		return descriptor;
	}
	
	public Class<? extends Annotation> getAnnotation() {
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
	
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A getAnnotation(String descriptor) {
		for (ASMAnnotationConverter annotationDescriptor : values()) {
			if (annotationDescriptor.descriptor.equals(descriptor)) {
				return (A) Annotation.create(annotationDescriptor.annotation);
			}
		}
		
		return null;
	}
}
