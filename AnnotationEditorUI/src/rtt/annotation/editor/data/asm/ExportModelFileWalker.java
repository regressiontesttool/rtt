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

import rtt.annotation.editor.data.asm.visitor.AddClassAnnotationVisitor;
import rtt.annotation.editor.data.asm.visitor.AddFieldAnnotationVisitor;
import rtt.annotation.editor.data.asm.visitor.AddMethodAnnotationVisitor;
import rtt.annotation.editor.data.asm.visitor.RemoveClassAnnotationVisitor;
import rtt.annotation.editor.data.asm.visitor.RemoveFieldAnnotationVisitor;
import rtt.annotation.editor.data.asm.visitor.RemoveMethodAnnotationVisitor;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

final class ExportModelFileWalker extends AbstractFileWalker {
	
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
					
					MethodVisitor removeVisitor = new RemoveMethodAnnotationVisitor(method, mv);
					MethodVisitor addVisitor = new AddMethodAnnotationVisitor(method, removeVisitor);
					
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

			ClassVisitor fieldVisitors = new ClassVisitor(Opcodes.ASM5, writer) {
				@Override
				public FieldVisitor visitField(int access, String name,
						String desc, String signature, Object value) {
					
					if (!isSynthetic(access)) {
						FieldElement field = element.getField(name, Type.getType(desc).getClassName());
						if (field != null) {
							FieldVisitor fv = super.visitField(access, name, desc, signature, value);
							FieldVisitor removeVisitor = new RemoveFieldAnnotationVisitor(field, fv);
							FieldVisitor addVisitor = new AddFieldAnnotationVisitor(field, removeVisitor);
							
							return addVisitor;
						}
					}
					
					return super.visitField(access, name, desc, signature, value);
				}
				
				private boolean isSynthetic(int access) {
					return (access & Opcodes.ACC_SYNTHETIC) == Opcodes.ACC_SYNTHETIC;
				}
			};
			ClassVisitor methodVisitors = new WriteMethodsVisitor(Opcodes.ASM5, fieldVisitors, element);
			
			ClassVisitor removeVisitor = new RemoveClassAnnotationVisitor(element, methodVisitors);
			ClassVisitor addVisitor = new AddClassAnnotationVisitor(element, removeVisitor);			
			
			reader.accept(addVisitor, 0);
			
			Files.write(file, writer.toByteArray(), StandardOpenOption.WRITE);
		}
	}
}