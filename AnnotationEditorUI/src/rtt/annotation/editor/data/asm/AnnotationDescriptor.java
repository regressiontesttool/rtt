package rtt.annotation.editor.data.asm;

import org.objectweb.asm.Type;

import rtt.annotations.Parser.Node;
import rtt.annotations.Parser.Node.Compare;
import rtt.annotations.Parser.Node.Informational;

public enum AnnotationDescriptor {
	
	NODE (Type.getDescriptor(Node.class)),
	COMPARE (Type.getDescriptor(Compare.class)),
	INFORMATIONAL (Type.getDescriptor(Informational.class));
	
	private String descriptor;

	private AnnotationDescriptor(String description) {
		this.descriptor = description;
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
}
