package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;

import rtt.annotation.editor.data.asm.AnnotationDescriptor;
import rtt.annotation.editor.model.Annotatable;

public class RemoveTest extends AnnotationChanger {
	
	public RemoveTest(Annotatable<?> annotatable) {
		super(annotatable); 
	}
	
	protected AnnotationVisitor checkAnnotation(IAnnotationVisitor visitor, String desc, boolean visible) {
		AnnotationDescriptor descriptor = AnnotationDescriptor.findAnnotation(desc);
		if (descriptor != null && !descriptor.equalsAnnotation(annotatable.getAnnotation())) {
			return null;
		}
		
		return visitor.getVisitor(desc, visible);
	}
	
	@Override
	protected void addAnnotation(IAnnotationVisitor visitor) {
		// do nothing
	}
}
