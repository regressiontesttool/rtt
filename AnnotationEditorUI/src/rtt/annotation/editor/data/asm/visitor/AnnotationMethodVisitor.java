package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.visitor.AnnotationChanger.IAnnotationVisitor;

public class AnnotationMethodVisitor extends MethodVisitor implements IAnnotationVisitor {
	
	private AnnotationChanger instance;
	
	public AnnotationMethodVisitor(AnnotationChanger instance, MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
		this.instance = instance;
	}
	
	public static MethodVisitor create(MethodVisitor mv, AnnotationChanger... changers) {
		MethodVisitor nextVisitor = mv;
		for (AnnotationChanger changer : changers) {
			nextVisitor = new AnnotationMethodVisitor(changer, nextVisitor);
		}
		
		return nextVisitor;
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
