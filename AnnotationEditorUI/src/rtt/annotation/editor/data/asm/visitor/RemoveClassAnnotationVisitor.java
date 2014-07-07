package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;

import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.ClassElement;

public class RemoveClassAnnotationVisitor extends AbstractClassVisitor<ClassElement> {

	public RemoveClassAnnotationVisitor(ClassElement modelElement, ClassVisitor cv) {
		super(modelElement, cv);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (desc.equals(ASMConverter.NODE_DESC) && !element.hasAnnotation()) {
			return null;
		}		
		
		return super.visitAnnotation(desc, visible);
	}

}
