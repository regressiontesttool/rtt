package prototype.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import prototype.asm.model.ClassElement;

public class AddClassAnnotationVisitor extends ClassVisitor {

	private ClassElement classElement;
	private boolean annotationPresent = false;

	public AddClassAnnotationVisitor(ClassElement classElement, ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.classElement = classElement;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		int v = (version & 0xFF) < Opcodes.V1_5 ? Opcodes.V1_5 : version;
		super.visit(v, access, name, signature, superName, interfaces);
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (visible && desc.equals(ASMPrototype.NODE_DESC)) {
			annotationPresent = true;
		}		
		
		return super.visitAnnotation(desc, visible);
	}
	
	@Override
	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		addAnnotation();
		super.visitInnerClass(name, outerName, innerName, access);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		addAnnotation();		
		return super.visitField(access, name, desc, signature, value);
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		addAnnotation();
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	@Override
	public void visitEnd() {
		addAnnotation();
		super.visitEnd();
	}

	private void addAnnotation() {
		if (annotationPresent == false && classElement.hasAnnotation()) {
			AnnotationVisitor av = cv.visitAnnotation(ASMPrototype.NODE_DESC, true);
			if (av != null) {
				av.visitEnd();
			}
			
			annotationPresent = true;
		}
	}

}
