package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;

import rtt.annotation.editor.data.asm.ASMAnnotationConverter;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.RTTAnnotation;

public class RemoveAnnotationChanger extends AnnotationChanger {
	
	public RemoveAnnotationChanger(Annotatable<?> annotatable) {
		super(annotatable); 
	}
	
	protected AnnotationVisitor checkAnnotation(IAnnotationVisitor visitor, String desc, boolean visible) {
		RTTAnnotation annotation = ASMAnnotationConverter.getAnnotation(desc);
		if (annotation != null && !annotation.equals(annotatable.getAnnotation())) {
			return null;
		}
		
		return visitor.getVisitor(desc, visible);
	}
	
	@Override
	protected void addAnnotation(IAnnotationVisitor visitor) {
		// do nothing
	}
}
