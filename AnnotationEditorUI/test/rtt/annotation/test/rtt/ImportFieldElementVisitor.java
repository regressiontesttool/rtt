package rtt.annotation.test.rtt;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.ASMAnnotationConverter;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.annotation.Annotation;

final class ImportFieldElementVisitor extends FieldVisitor {
	private final FieldElement field;

	ImportFieldElementVisitor(FieldElement field, FieldVisitor fv) {
		super(Opcodes.ASM5, fv);
		this.field = field;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc,
			boolean visible) {
		Annotation annotation = ASMAnnotationConverter.getAnnotation(desc);
		if (annotation != null) {
			field.setAnnotation(annotation);
		}
		
		return super.visitAnnotation(desc, visible);
	}
}