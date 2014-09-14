package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;

import rtt.annotation.editor.controller.rules.RTTAnnotation;
import rtt.annotation.editor.controller.rules.RTTAnnotation.AnnotationType;
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
		RTTAnnotation annotation = ASMAnnotationConverter.getAnnotation(desc);
		
		switch(annotation.getType()) {
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
	
	private ASMAnnotationConverter getDescriptor(RTTAnnotation annotation) {		
		if (annotation.getType() == AnnotationType.INITIALIZE && !hasInitializeAnnotation) {
			hasInitializeAnnotation = true;
			return ASMAnnotationConverter.INITIALIZE;
		}
		
		if (annotation.getType() == AnnotationType.VALUE && !hasValueAnnotation) {
			hasValueAnnotation = true;
			return ASMAnnotationConverter.VALUE;
		}
		
		if (annotation.getType() == AnnotationType.NODE && !hasNodeAnnotation) {
			hasNodeAnnotation = true;
			return ASMAnnotationConverter.NODE;
		}
		
		return null;
	}
}

