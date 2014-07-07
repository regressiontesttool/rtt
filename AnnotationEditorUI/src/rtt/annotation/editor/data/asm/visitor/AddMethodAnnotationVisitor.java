package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.MethodElement;

public class AddMethodAnnotationVisitor extends MethodVisitor {

	private MethodElement element;
	private boolean comparePresent;
	private boolean infoPresent;

	public AddMethodAnnotationVisitor(MethodElement element, MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
		this.element = element;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (desc.equals(ASMConverter.COMPARE_DESC)) {
			comparePresent = true;
		}
		
		if (desc.equals(ASMConverter.INFO_DESC)) {
			infoPresent = true;
		}
		
		return super.visitAnnotation(desc, visible);
	}
	
	@Override
	public void visitCode() {
		if (!comparePresent && element.getAnnotation() == Annotation.COMPARE) {
			AnnotationVisitor av = super.visitAnnotation(ASMConverter.COMPARE_DESC, true);
			if (av != null) {
				av.visitEnd();
			}
			comparePresent = true;
		}
		
		if (!infoPresent && element.getAnnotation() == Annotation.INFORMATIONAL) {
			AnnotationVisitor av = super.visitAnnotation(ASMConverter.INFO_DESC, true);
			if (av != null) {
				av.visitEnd();
			}
			infoPresent = true;
		}
		
		super.visitCode();
	}
	
	@Override
	public void visitEnd() {
		if (!isAnnotationPresent() && element.hasAnnotation()) {
			String desc = null;
			if (element.getAnnotation() == Annotation.COMPARE) {
				desc = ASMConverter.COMPARE_DESC;
			}
			
			if (element.getAnnotation() == Annotation.INFORMATIONAL) {
				desc = ASMConverter.INFO_DESC;
			}
			
			AnnotationVisitor av = super.visitAnnotation(desc, true);
			if (av != null) {
				av.visitEnd();
			}
		}		
		
		super.visitEnd();
	}

	private boolean isAnnotationPresent() {
		return comparePresent || infoPresent;
	}
}
