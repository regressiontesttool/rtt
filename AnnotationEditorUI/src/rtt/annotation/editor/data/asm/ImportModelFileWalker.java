package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElement.ClassType;
import rtt.annotation.editor.model.ClassElementReference;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

final class ImportModelFileWalker extends AbstractFileWalker {

	final class ClassElementAnnotationAdapter extends ClassVisitor {
		
		private ClassElement element = null;

		protected ClassElementAnnotationAdapter() {
			super(Opcodes.ASM5);
		}
		
		public ClassElementAnnotationAdapter setElement(ClassElement element) {
			this.element = element;
			return this;
		}

		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			if (NODE_DESC.equals(desc) && element != null) {
				element.setAnnotation(Annotation.NODE);
			}

			return super.visitAnnotation(desc, visible);
		}
		
		@Override
		public FieldVisitor visitField(int access, String name, String desc,
				String signature, Object value) {
			
			if (!isSynthetic(access)) {
				FieldElement field = factory.createFieldElement(element, name);
				field.setType(Type.getType(desc).getClassName());
				
				element.addField(field);
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
				MethodElement method = factory.createMethodElement(element, name);
				method.setType(methodType.getReturnType().getClassName());
				
				element.addMethod(method);
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
	
	public ImportModelFileWalker(ClassModel model) {
		super(model);
	}
	
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		ClassElementAnnotationAdapter adapter = new ClassElementAnnotationAdapter();
		
		String className = computeClassName(reader.getClassName());
		String packageName = computePackageName(reader.getClassName());
		
		ClassElement classElement = factory.createClassElement(model);
		classElement.setName(className);
		classElement.setPackageName(packageName);
		
		if (checkAccess(reader.getAccess(), Opcodes.ACC_ABSTRACT)) {
			classElement.setType(ClassType.ABSTRACT);
		}
		
		if (checkAccess(reader.getAccess(), Opcodes.ACC_INTERFACE)) {
			classElement.setType(ClassType.INTERFACE);
		}
		
		if (checkAccess(reader.getAccess(), Opcodes.ACC_ENUM)) {
			classElement.setType(ClassType.ENUMERATION);
		}
		
		String superClass = reader.getSuperName();
		if (superClass != null && !superClass.equals("java/lang/Object")) {
			classElement.setSuperClass(ClassElementReference.create(superClass));
		}
		
		String[] interfaces = reader.getInterfaces();
		if (interfaces != null) {
			classElement.setInterfaces(ClassElementReference.create(interfaces));
		}
		
		
		reader.accept(adapter.setElement(classElement), ClassReader.SKIP_CODE);
		
		model.addClassElement(classElement);
	}
	
	private boolean checkAccess(int access, int code) {
		return (access & code) == code;
	}
}