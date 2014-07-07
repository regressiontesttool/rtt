package prototype.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import prototype.asm.model.ClassElement;
import prototype.asm.model.ClassElement.Annotation;

public class RemoveClassAnnotationVisitor extends ClassVisitor {

	private ClassElement element;

	public RemoveClassAnnotationVisitor(ClassElement modelElement, ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.element = modelElement;
	}
	
	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		if (desc.equals(ASMPrototype.NODE_DESC) && element.getAnnotation() == Annotation.NODE) {
			return null;
		}		
		
		return super.visitAnnotation(desc, visible);
	}

}
