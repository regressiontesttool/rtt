package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.MethodElement;

public class RemoveMethodAnnotationVisitor extends MethodVisitor {
	
	private MethodElement methodElement;

	public RemoveMethodAnnotationVisitor(MethodElement methodElement, MethodVisitor mv) {
		super(Opcodes.ASM5, mv);
		this.methodElement = methodElement;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {		
		if (isRTTAnnotation(desc) && !methodElement.hasAnnotation()) {
			return null;
		}
		
		return super.visitAnnotation(desc, visible);
	}
	
	private boolean isRTTAnnotation(String desc) {
		return desc.equals(ASMConverter.COMPARE_DESC) || desc.equals(ASMConverter.INFO_DESC);
	}
}
