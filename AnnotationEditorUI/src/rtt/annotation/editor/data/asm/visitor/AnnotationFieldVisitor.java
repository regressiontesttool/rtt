package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.visitor.AnnotationChanger.IAnnotationVisitor;

public class AnnotationFieldVisitor extends FieldVisitor implements IAnnotationVisitor {
	
	private AnnotationChanger instance;
	
	public AnnotationFieldVisitor(AnnotationChanger instance, FieldVisitor fv) {
		super(Opcodes.ASM5, fv);
		this.instance = instance;
	}
	
	public static FieldVisitor create(FieldVisitor fv, AnnotationChanger... changers) {		
		FieldVisitor nextVisitor = fv;
		for (AnnotationChanger changer : changers) {
			nextVisitor = new AnnotationFieldVisitor(changer, nextVisitor);
		}
		
		return nextVisitor;
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
