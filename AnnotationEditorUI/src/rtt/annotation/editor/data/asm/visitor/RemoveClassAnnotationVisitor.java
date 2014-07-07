package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.ClassElement;

public class RemoveClassAnnotationVisitor extends ClassVisitor {

	private ClassElement element;

	public RemoveClassAnnotationVisitor(ClassElement modelElement, ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.element = modelElement;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (desc.equals(ASMConverter.NODE_DESC) && !element.hasAnnotation()) {
			return null;
		}		
		
		return super.visitAnnotation(desc, visible);
	}

}
