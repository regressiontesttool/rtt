package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.visitor.AnnotationChanger.IAnnotationVisitor;

public class AddAnnotationMethodVisitor extends MethodVisitor implements IAnnotationVisitor {
	
	private AddTest instance;
	
	public AddAnnotationMethodVisitor(AddTest instance, MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
		this.instance = instance;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return instance.checkAnnotation(this, desc, visible);
	}
	
	@Override
	public void visitCode() {
		instance.addAnnotation(this);
		super.visitCode();
	}
	
	public void visitEnd() {
		instance.addAnnotation(this);
		super.visitEnd();
	};
	
	@Override
	public AnnotationVisitor getVisitor(String descriptor, boolean visible) {
		return super.visitAnnotation(descriptor, visible);
	}
}
