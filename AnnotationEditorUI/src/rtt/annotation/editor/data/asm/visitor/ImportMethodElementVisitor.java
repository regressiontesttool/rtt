package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.ASMAnnotationConverter;
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
		ASMAnnotationConverter descriptor = ASMAnnotationConverter.findByDescriptor(desc);
		if (descriptor != null) {
			method.setAnnotation(descriptor.getAnnotation());
		}
		
		return super.visitAnnotation(desc, visible);
	}
}