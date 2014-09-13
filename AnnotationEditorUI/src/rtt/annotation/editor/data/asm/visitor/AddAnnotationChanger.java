package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.asm.ASMAnnotationConverter;
import rtt.annotation.editor.model.Annotatable;

public class AddAnnotationChanger extends AnnotationChanger {
	
	private boolean hasValueAnnotation = false;
	private boolean hasInitializeAnnotation = false;
	private boolean hasNodeAnnotation = false;
	
	public AddAnnotationChanger(Annotatable<?> annotatable) {
		super(annotatable);
	}

	protected AnnotationVisitor checkAnnotation(IAnnotationVisitor visitor, String desc, boolean visible) {
		ASMAnnotationConverter descriptor = ASMAnnotationConverter.findByDescriptor(desc);
		if (descriptor != null) {
			switch(ASMAnnotationConverter.findByDescriptor(desc)) {
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
		ASMAnnotationConverter descriptor = getDescriptor(annotatable.getAnnotation());
		if (descriptor != null) {
			AnnotationVisitor av = visitor.getVisitor(descriptor.getDescriptor(), true);
			if (av != null) {
				av.visitEnd();
			}
		}
	}
	
	private ASMAnnotationConverter getDescriptor(Annotation annotation) {		
		if (annotation == Annotation.INITIALIZE && !hasInitializeAnnotation) {
			hasInitializeAnnotation = true;
			return ASMAnnotationConverter.INITIALIZE;
		}
		
		if (annotation == Annotation.VALUE && !hasValueAnnotation) {
			hasValueAnnotation = true;
			return ASMAnnotationConverter.VALUE;
		}
		
		if (annotation == Annotation.NODE && !hasNodeAnnotation) {
			hasNodeAnnotation = true;
			return ASMAnnotationConverter.NODE;
		}
		
		return null;
	}
}

