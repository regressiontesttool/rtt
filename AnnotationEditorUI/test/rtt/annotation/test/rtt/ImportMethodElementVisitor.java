package rtt.annotation.test.rtt;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.ASMAnnotationConverter;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotation;

final class ImportMethodElementVisitor extends MethodVisitor {
	private final MethodElement method;

	ImportMethodElementVisitor(MethodElement method, MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
		this.method = method;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc,
			boolean visible) {
		Annotation annotation = ASMAnnotationConverter.getAnnotation(desc);
		if (annotation != null) {
			method.setAnnotation(annotation);
		}
		
		return super.visitAnnotation(desc, visible);
	}
}