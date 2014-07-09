package rtt.annotation.editor.data.asm;

import java.io.IOException;
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

import rtt.annotation.editor.data.asm.visitor.AddAnnotationClassVisitor;
import rtt.annotation.editor.data.asm.visitor.AddAnnotationFieldVisitor;
import rtt.annotation.editor.data.asm.visitor.AddAnnotationMethodVisitor;
import rtt.annotation.editor.data.asm.visitor.AddTest;
import rtt.annotation.editor.data.asm.visitor.AnnotationChanger;
import rtt.annotation.editor.data.asm.visitor.RemoveFieldAnnotationVisitor;
import rtt.annotation.editor.data.asm.visitor.RemoveMethodAnnotationVisitor;
import rtt.annotation.editor.data.asm.visitor.RemoveTest;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

final class ExportModelFileWalker extends AbstractFileWalker {
	
	private final class WriteFieldsVisitor extends ClassVisitor {
		private final ClassElement element;

		private WriteFieldsVisitor(int api, ClassVisitor cv,
				ClassElement element) {
			super(api, cv);
			this.element = element;
		}

		@Override
		public FieldVisitor visitField(int access, String name,
				String desc, String signature, Object value) {
			
			if (!isSynthetic(access)) {
				FieldElement field = element.getField(name, Type.getType(desc).getClassName());
				if (field != null) {
					FieldVisitor fv = super.visitField(access, name, desc, signature, value);
					
					AddTest instance = new AddTest(field);
					
					FieldVisitor removeVisitor = new RemoveFieldAnnotationVisitor(field, fv);
					FieldVisitor addVisitor = new AddAnnotationFieldVisitor(instance, removeVisitor);
					
					return addVisitor;
				}
			}
			
			return super.visitField(access, name, desc, signature, value);
		}

		private boolean isSynthetic(int access) {
			return (access & Opcodes.ACC_SYNTHETIC) == Opcodes.ACC_SYNTHETIC;
		}
	}

	private final class WriteMethodsVisitor extends ClassVisitor {
		private final ClassElement element;

		private WriteMethodsVisitor(int api, ClassVisitor cv,
				ClassElement element) {
			super(api, cv);
			this.element = element;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name,
				String desc, String signature, String[] exceptions) {
			
			Type methodType = Type.getType(desc);
			if (!hasVoidReturnType(methodType) && !hasArguments(methodType)) {
				MethodElement method = element.getMethod(name, methodType.getReturnType().getClassName());
				if (method != null) {
					MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);				
					
					AddTest instance = new AddTest(method);
					
					MethodVisitor removeVisitor = new RemoveMethodAnnotationVisitor(method, mv);
					MethodVisitor addVisitor = new AddAnnotationMethodVisitor(instance, removeVisitor);
					
					return addVisitor;
				}						
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

	public ExportModelFileWalker(ClassModel model) {
		super(model);
	}
	
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		final ClassElement element = ASMConverter.RESOLVER.findClass(reader.getClassName(), model);
		
		if (element != null && element.hasChanged()) {
			ClassWriter writer = new ClassWriter(reader, 0);		

			ClassVisitor methodVisitors = new WriteMethodsVisitor(Opcodes.ASM5, writer, element);
			ClassVisitor fieldVisitors = new WriteFieldsVisitor(Opcodes.ASM5, methodVisitors, element);
			
			AnnotationChanger addInstance = new AddTest(element);
			AnnotationChanger removeInstance = new RemoveTest(element);
			
			ClassVisitor removeVisitor = new AddAnnotationClassVisitor(removeInstance, fieldVisitors);
			ClassVisitor addVisitor = new AddAnnotationClassVisitor(addInstance, removeVisitor);			
			
			reader.accept(addVisitor, 0);
			
			Files.write(file, writer.toByteArray(), StandardOpenOption.WRITE);
		}
	}
}