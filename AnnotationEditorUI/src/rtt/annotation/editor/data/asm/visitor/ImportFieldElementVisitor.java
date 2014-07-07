package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.FieldElement;

final class ImportFieldElementVisitor extends FieldVisitor {
	private final FieldElement field;

	ImportFieldElementVisitor(FieldElement field, FieldVisitor fv) {
		super(Opcodes.ASM5, fv);
		this.field = field;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc,
			boolean visible) {
		if (desc.equals(ASMConverter.COMPARE_DESC)) {
			field.setAnnotation(Annotation.COMPARE);
		}
		
		if (desc.equals(ASMConverter.INFO_DESC)) {
			field.setAnnotation(Annotation.INFORMATIONAL);
		}
		
		return super.visitAnnotation(desc, visible);
	}
}