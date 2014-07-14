package rtt.annotation.editor.data.asm.visitor;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import rtt.annotation.editor.data.asm.ASMConverter;
import rtt.annotation.editor.data.asm.AnnotationDescriptor;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElement.ClassType;
import rtt.annotation.editor.model.ClassElementReference;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public final class ImportClassElementVisitor extends ClassVisitor {
	
	private ClassModelFactory factory;
	private ClassElement element;

	public ImportClassElementVisitor(ClassElement element, ClassModelFactory factory) {
		super(Opcodes.ASM5, null);
		this.factory = factory;
		this.element = element;
	}
	
	@Override
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		
		String completeName = name.replace("/", ".");
		
		String className = ASMConverter.RESOLVER.computeClassName(completeName);
		String packageName = ASMConverter.RESOLVER.computePackageName(completeName);
		
		element.setName(className);
		element.setPackageName(packageName);
		
		if (checkAccess(access, Opcodes.ACC_ABSTRACT)) {
			element.setType(ClassType.ABSTRACT);
		}
		
		if (checkAccess(access, Opcodes.ACC_INTERFACE)) {
			element.setType(ClassType.INTERFACE);
		}
		
		if (checkAccess(access, Opcodes.ACC_ENUM)) {
			element.setType(ClassType.ENUMERATION);
		}
		
		if (superName != null && !superName.equals("java/lang/Object")) {
			element.setSuperClass(ClassElementReference.create(superName.replace("/", ".")));
		}
		
		if (interfaces != null) {
			String[] changedInterfaces = interfaces.clone();
			for (int i = 0; i < changedInterfaces.length; i++) {
				changedInterfaces[i] = changedInterfaces[i].replace("/", ".");
			}
			element.setInterfaces(ClassElementReference.create(changedInterfaces));
		}
		
		super.visit(version, access, name, signature, superName, interfaces);
	}
	
	private boolean checkAccess(int access, int code) {
		return (access & code) == code;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		AnnotationDescriptor descriptor = AnnotationDescriptor.findAnnotation(desc);
		if (descriptor != null) {
			element.setAnnotation(descriptor.getAnnotation());
		}

		return super.visitAnnotation(desc, visible);
	}
	
	@Override
	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		
		if (!isSynthetic(access)) {
			FieldElement field = factory.createFieldElement(element, name);
			field.setType(Type.getType(desc).getClassName());
			
			FieldVisitor fv = super.visitField(access, name, desc, signature, value);
			FieldVisitor importVisitor = new ImportFieldElementVisitor(field, fv);
			
			element.addField(field);
			return importVisitor;
		}			
		
		return super.visitField(access, name, desc, signature, value);
	}
	
	private boolean isSynthetic(int access) {
		return (access & Opcodes.ACC_SYNTHETIC) == Opcodes.ACC_SYNTHETIC;
	}
	
	@Override
	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		
		Type methodType = Type.getType(desc);
		if (!hasVoidReturnType(methodType) && !hasArguments(methodType)) {
			final MethodElement method = factory.createMethodElement(element, name);
			method.setType(methodType.getReturnType().getClassName());
			
			MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
			MethodVisitor importVisitor = new ImportMethodElementVisitor(method, mv);
			
			element.addMethod(method);
			return importVisitor;
		}			
		
		return super.visitMethod(access, name, desc, signature, exceptions);
	}
	
	private boolean hasVoidReturnType(Type methodType) {
		return Type.VOID_TYPE.equals(methodType.getReturnType());
	}

	private boolean hasArguments(Type methodType) {
		return methodType.getArgumentTypes().length > 0;
	}
}