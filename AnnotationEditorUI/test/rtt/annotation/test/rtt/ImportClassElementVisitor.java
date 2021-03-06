package rtt.annotation.test.rtt;

import java.io.InputStream;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import rtt.annotation.editor.data.asm.ASMAnnotationConverter;
import rtt.annotation.editor.data.asm.ASMClassModelManager;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElement.ClassType;
import rtt.annotation.editor.model.annotation.Annotation;
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
		
		String className = ASMClassModelManager.RESOLVER.computeClassName(completeName);
		String packageName = ASMClassModelManager.RESOLVER.computePackageName(completeName);
		
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
		Annotation annotation = ASMAnnotationConverter.getAnnotation(desc);
		if (annotation != null) {
//			element.setAnnotation(annotation);
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
			
			element.addValuableField(field);
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
		final MethodElement method = factory.createMethodElement(element, name);		
		method.setType(methodType.getReturnType().getClassName());
		
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		MethodVisitor importVisitor = new ImportMethodElementVisitor(method, mv);		
		
		if (isValuableMethod(methodType)) {
			element.addValuableMethod(method);
			return importVisitor;
		} else if (isInitializableMethod(methodType)) {
			element.addInitializableMethod(method);
			return importVisitor;
		}
		
		return mv;
	}
	
	private boolean isValuableMethod(Type methodType) {
		return hasNonVoidReturnType(methodType) && 
				methodType.getArgumentTypes().length == 0;
	}

	private boolean isInitializableMethod(Type methodType) {
		if (hasNonVoidReturnType(methodType)) {
			return false;
		}
		
		int argumentCount = methodType.getArgumentTypes().length;
		if (argumentCount < 1 || argumentCount > 2) {
			return false;
		}
		
		Type firstArgument = methodType.getArgumentTypes()[0];
		if (!firstArgument.equals(Type.getType(InputStream.class))) {
			return false;
		}
		
		if (argumentCount == 2) {
			Type secondArgument = methodType.getArgumentTypes()[1];
			if (!secondArgument.equals(Type.getType(String[].class))) {
				return false;
			}
		}
		
		return true;
	}

	private boolean hasNonVoidReturnType(Type methodType) {
		return !methodType.getReturnType().equals(Type.VOID_TYPE);
	}
}