package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.model.FieldElement;

public class RemoveFieldAnnotationVisitor extends FieldVisitor {

	private FieldElement fieldElement;

	public RemoveFieldAnnotationVisitor(FieldVisitor fv, FieldElement fieldElement) {
		super(Opcodes.ASM5, fv);
		this.fieldElement = fieldElement;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (isRTTAnnotation(desc) && !fieldElement.hasAnnotation()) {
			return null;
		}
		
		return super.visitAnnotation(desc, visible);
	}
	
	private boolean isRTTAnnotation(String desc) {
		return desc.equals(ASMConverter.COMPARE_DESC) || desc.equals(ASMConverter.INFO_DESC);
	}
}
