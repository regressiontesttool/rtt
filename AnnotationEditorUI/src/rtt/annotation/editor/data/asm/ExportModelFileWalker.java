package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import rtt.annotation.editor.data.asm.visitor.AddAnnotationChanger;
import rtt.annotation.editor.data.asm.visitor.AnnotationClassVisitor;
import rtt.annotation.editor.data.asm.visitor.AnnotationFieldVisitor;
import rtt.annotation.editor.data.asm.visitor.AnnotationMethodVisitor;
import rtt.annotation.editor.data.asm.visitor.RemoveAnnotationChanger;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

final class ExportModelFileWalker extends AbstractFileWalker {
	
	private final class WriteFieldsVisitor extends ClassVisitor {
		private final ClassElement element;

		private WriteFieldsVisitor(ClassVisitor cv, ClassElement element) {
			super(Opcodes.ASM5, cv);
			this.element = element;
		}

		@Override
		public FieldVisitor visitField(int access, String name,
				String desc, String signature, Object value) {
			
			FieldVisitor fieldVisitor = super.visitField(access, name, desc, signature, value); 
			
			if (!isSynthetic(access)) {
				FieldElement field = element.getValuableField(name, Type.getType(desc).getClassName());
				if (field != null) {
					fieldVisitor = AnnotationFieldVisitor.create(fieldVisitor, 
							new RemoveAnnotationChanger(field), new AddAnnotationChanger(field));
				}
			}
			
			return fieldVisitor;
		}

		private boolean isSynthetic(int access) {
			return (access & Opcodes.ACC_SYNTHETIC) == Opcodes.ACC_SYNTHETIC;
		}
	}

	private final class WriteMethodsVisitor extends ClassVisitor {
		private final ClassElement element;

		private WriteMethodsVisitor(ClassVisitor cv, ClassElement element) {
			super(Opcodes.ASM5, cv);
			this.element = element;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name,
				String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			
			Type methodType = Type.getType(desc);
			if (isValuableMethod(methodType)) {
				MethodElement valueableMethod = element.getValuableMethod(
						name, methodType.getReturnType().getClassName());
				
				if (valueableMethod != null) {				
					methodVisitor = AnnotationMethodVisitor.create(methodVisitor, 
							new RemoveAnnotationChanger(valueableMethod), 
							new AddAnnotationChanger(valueableMethod));
				}
			} else if (isInitializableMethod(methodType)) {
				MethodElement initializableMethod = element.getInitializableMethod(name);
				if (initializableMethod != null) {
					methodVisitor = AnnotationMethodVisitor.create(methodVisitor, 
							new RemoveAnnotationChanger(initializableMethod), 
							new AddAnnotationChanger(initializableMethod));
				}
			}
			
			return methodVisitor;
		}

		private boolean isValuableMethod(Type methodType) {
			return hasNonVoidReturnType(methodType) && methodType.getArgumentTypes().length == 0;
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

	public ExportModelFileWalker(ClassModel model) {
		super(model);
	}
	
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		String className = reader.getClassName().replace("/", ".");
		
		final ClassElement element = ASMConverter.RESOLVER.findClass(className, model);
		
		if (element != null && element.hasChanged()) {
			ClassWriter writer = new ClassWriter(reader, 0);		

			ClassVisitor methodVisitors = new WriteMethodsVisitor(writer, element);
			ClassVisitor fieldVisitors = new WriteFieldsVisitor(methodVisitors, element);			
			
			ClassVisitor classVisitors = AnnotationClassVisitor.create(fieldVisitors, 
					new RemoveAnnotationChanger(element), new AddAnnotationChanger(element));
			
			reader.accept(classVisitors, 0);
			
			Files.write(file, writer.toByteArray(), StandardOpenOption.WRITE);
		}
	}
}