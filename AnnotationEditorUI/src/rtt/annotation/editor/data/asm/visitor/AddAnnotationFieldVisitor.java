package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.visitor.AnnotationChanger.IAnnotationVisitor;

public class AddAnnotationFieldVisitor extends FieldVisitor implements IAnnotationVisitor {
	
	private AddTest instance;
	
	public AddAnnotationFieldVisitor(AddTest instance, FieldVisitor fv) {
		super(Opcodes.ASM5, fv);
		this.instance = instance;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return instance.checkAnnotation(this, desc, visible);
	}
	
	@Override
	public void visitAttribute(Attribute attr) {
		instance.addAnnotation(this);
		super.visitAttribute(attr);
	}
	
	@Override
	public void visitEnd() {
		instance.addAnnotation(this);
		super.visitEnd();
	}

	@Override
	public AnnotationVisitor getVisitor(String descriptor, boolean visible) {
		return super.visitAnnotation(descriptor, visible);
	}
}
