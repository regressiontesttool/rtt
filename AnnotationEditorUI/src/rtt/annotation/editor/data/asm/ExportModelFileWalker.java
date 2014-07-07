package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.eclipse.core.runtime.Status;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import rtt.annotation.editor.AnnotationEditorPlugin;
import rtt.annotation.editor.data.asm.visitor.AddClassAnnotationVisitor;
import rtt.annotation.editor.data.asm.visitor.RemoveClassAnnotationVisitor;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;


final class ExportModelFileWalker extends AbstractFileWalker {
	
//	private static final class WriteAnnotationClassVisitor extends ClassVisitor {
//
//		private ClassElement element;
//		private boolean hasNodeAnnotation = false;
//		
//		public WriteAnnotationClassVisitor(ClassVisitor cv) {
//			super(Opcodes.ASM5, cv);
//		}
//		
//		@Override
//		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//			if (desc.equals(NODE_DESC)) {
//				hasNodeAnnotation  = true;
//				
//				if (element.getAnnotation() == Annotation.NONE) {
//					return null;
//				}
//			}		
//			
//			return cv.visitAnnotation(desc, visible);
//		}
//		
//		public WriteAnnotationClassVisitor setElement(ClassElement element) {
//			this.element = element;
//			return this;
//		}
//		
//		@Override
//		public void visitEnd() {
//			if (!hasNodeAnnotation && element.getAnnotation() == Annotation.NODE) {
//				AnnotationVisitor av = cv.visitAnnotation(NODE_DESC, true);
//				if (av != null) {
//					av.visitEnd();
//				}
//			}
//			
//			cv.visitEnd();
//		}		
//	}
	
	public ExportModelFileWalker(ClassModel model) {
		super(model);
	}
	
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		ClassElement element = ASMConverter.RESOLVER.findClass(reader.getClassName(), model); 
		if (element != null && element.hasChanged()) {
			ClassWriter writer = new ClassWriter(reader, 0);
		
//			ClassVisitor removeVisitor = new RemoveClassAnnotationVisitor(element, writer);
			ClassVisitor addVisitor = new AddClassAnnotationVisitor(element, writer);
			
			reader.accept(addVisitor, ClassReader.SKIP_CODE);
			
//			AnnotationEditorPlugin.log(Status.INFO, "Delete1: " + Files.deleteIfExists(file));
			AnnotationEditorPlugin.log(Status.INFO, "Write: " + Files.write(file, writer.toByteArray(), StandardOpenOption.WRITE));
		}
	}
}