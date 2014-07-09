package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.visitor.AnnotationChanger.IAnnotationVisitor;

public class AnnotationClassVisitor extends ClassVisitor implements IAnnotationVisitor {

	private AnnotationChanger instance;

	public AnnotationClassVisitor(AnnotationChanger instance, ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.instance = instance;
	}
	
	public static ClassVisitor create(
			ClassVisitor cv, AnnotationChanger... changers) {
		
		ClassVisitor nextVisitor = cv;
		for (AnnotationChanger changer : changers) {
			nextVisitor = new AnnotationClassVisitor(changer, nextVisitor);
		}
		
		return nextVisitor;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return instance.checkAnnotation(this, desc, visible);
	}
	
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		int v = (version & 0xFF) < Opcodes.V1_5 ? Opcodes.V1_5 : version;
		super.visit(v, access, name, signature, superName, interfaces);
	}
	
	@Override
	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		instance.addAnnotation(this);
		super.visitInnerClass(name, outerName, innerName, access);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		instance.addAnnotation(this);		
		return super.visitField(access, name, desc, signature, value);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		instance.addAnnotation(this);
		return super.visitMethod(access, name, desc, signature, exceptions);
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
