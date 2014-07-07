package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.FieldElement;

public class AddFieldAnnotationVisitor extends FieldVisitor {

	private FieldElement element;
	private boolean comparePresent;
	private boolean infoPresent;

	public AddFieldAnnotationVisitor(FieldElement element, FieldVisitor mv) {
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
