package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.asm.AnnotationDescriptor;
import rtt.annotation.editor.model.Annotatable;

public class AddAnnotationChanger extends AnnotationChanger {
	
	private boolean hasValueAnnotation = false;
	private boolean hasInitializeAnnotation = false;
	private boolean hasNodeAnnotation = false;
	
	public AddAnnotationChanger(Annotatable<?> annotatable) {
		super(annotatable);
	}

	protected AnnotationVisitor checkAnnotation(IAnnotationVisitor visitor, String desc, boolean visible) {
		AnnotationDescriptor descriptor = AnnotationDescriptor.findAnnotation(desc);
		if (descriptor != null) {
			switch(AnnotationDescriptor.findAnnotation(desc)) {
			case NODE:
				hasNodeAnnotation = true;
				break;
			case VALUE:
				hasValueAnnotation = true;
				break;
			case INITIALIZE:
				hasInitializeAnnotation = true;
				break;		
			}
		}		
		
		return visitor.getVisitor(desc, visible);
	}

	protected void addAnnotation(IAnnotationVisitor visitor) {
		AnnotationDescriptor descriptor = getDescriptor(annotatable.getAnnotation());
		if (descriptor != null) {
			AnnotationVisitor av = visitor.getVisitor(descriptor.getDescriptor(), true);
			if (av != null) {
				av.visitEnd();
			}
		}
	}
	
	private AnnotationDescriptor getDescriptor(Annotation annotation) {		
		if (annotation == Annotation.INITIALIZE && !hasInitializeAnnotation) {
			hasInitializeAnnotation = true;
			return AnnotationDescriptor.INITIALIZE;
		}
		
		if (annotation == Annotation.VALUE && !hasValueAnnotation) {
			hasValueAnnotation = true;
			return AnnotationDescriptor.VALUE;
		}
		
		if (annotation == Annotation.NODE && !hasNodeAnnotation) {
			hasNodeAnnotation = true;
			return AnnotationDescriptor.NODE;
		}
		
		return null;
	}
}

