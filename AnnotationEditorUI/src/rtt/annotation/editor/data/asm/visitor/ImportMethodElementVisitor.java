package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.MethodElement;

final class ImportMethodElementVisitor extends MethodVisitor {
	private final MethodElement method;

	ImportMethodElementVisitor(MethodElement method, MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
		this.method = method;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc,
			boolean visible) {
		if (desc.equals(ASMConverter.COMPARE_DESC)) {
			method.setAnnotation(Annotation.COMPARE);
		}
		
		if (desc.equals(ASMConverter.INFO_DESC)) {
			method.setAnnotation(Annotation.INFORMATIONAL);
		}
		
		return super.visitAnnotation(desc, visible);
	}
}