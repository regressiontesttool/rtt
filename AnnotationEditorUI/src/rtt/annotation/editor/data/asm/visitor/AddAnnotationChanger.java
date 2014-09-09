package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.asm.AnnotationDescriptor;
import rtt.annotation.editor.model.Annotatable;

public class AddAnnotationChanger extends AnnotationChanger {
	
	private boolean hasCompareAnnotation = false;
	private boolean hasInfoAnnotation = false;
	private boolean hasNodeAnnotation = false;
	
	public AddAnnotationChanger(Annotatable<?> annotatable) {
		super(annotatable);
	}

	protected AnnotationVisitor checkAnnotation(IAnnotationVisitor visitor, String desc, boolean visible) {
		switch(AnnotationDescriptor.findAnnotation(desc)) {
//			case COMPARE:
//				hasCompareAnnotation = true;
//				break;
//			case INFORMATIONAL:
//				hasInfoAnnotation = true;
//				break;
		case VALUE:
			hasCompareAnnotation = true;
			break;
		case NODE:
			hasNodeAnnotation = true;
			break;
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
//		if (annotation == Annotation.COMPARE && !hasCompareAnnotation) {
//			hasCompareAnnotation = true;
//			return AnnotationDescriptor.COMPARE;
//		}
//		
//		if (annotation == Annotation.INFORMATIONAL && !hasInfoAnnotation) {
//			hasInfoAnnotation = true;
//			return AnnotationDescriptor.INFORMATIONAL;
//		}
		
		if (annotation == Annotation.VALUE && !hasCompareAnnotation) {
			hasCompareAnnotation = true;
			return AnnotationDescriptor.VALUE;
		}
		
		if (annotation == Annotation.NODE && !hasNodeAnnotation) {
			hasNodeAnnotation = true;
			return AnnotationDescriptor.NODE;
		}
		
		return null;
	}
}

