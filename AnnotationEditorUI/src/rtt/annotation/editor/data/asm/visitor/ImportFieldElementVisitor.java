package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.AnnotationDescriptor;
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
		AnnotationDescriptor descriptor = AnnotationDescriptor.findAnnotation(desc);
		if (descriptor != null) {
			field.setAnnotation(descriptor.getAnnotation());
		}
		
		return super.visitAnnotation(desc, visible);
	}
}