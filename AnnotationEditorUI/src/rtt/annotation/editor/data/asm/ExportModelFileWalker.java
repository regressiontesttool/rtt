package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;


final class ExportModelFileWalker extends AbstractFileWalker {
	
	private static final class WriteAnnotationClassVisitor extends ClassVisitor {

		private ClassElement element;
		private boolean hasNodeAnnotation = false;
		
		public WriteAnnotationClassVisitor(ClassVisitor cv) {
			super(Opcodes.ASM5, cv);
		}
		
		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			if (desc.equals(NODE_DESC)) {
				hasNodeAnnotation  = true;
				
				if (element.getAnnotation() == Annotation.NONE) {
					return null;
				}
			}		
			
			return cv.visitAnnotation(desc, visible);
		}
		
		public WriteAnnotationClassVisitor setElement(ClassElement element) {
			this.element = element;
			return this;
		}
		
		@Override
		public void visitEnd() {
			if (!hasNodeAnnotation && element.getAnnotation() == Annotation.NODE) {
				AnnotationVisitor av = cv.visitAnnotation(NODE_DESC, true);
				if (av != null) {
					av.visitEnd();
				}
			}
			
			cv.visitEnd();
		}		
	}
	
	public ExportModelFileWalker(ClassModel model) {
		super(model);
	}
	
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		ClassElement element = findClass(reader.getClassName()); 
		if (element != null && element.hasChanged()) {
			ClassWriter writer = new ClassWriter(reader, 0);
			
			WriteAnnotationClassVisitor visitor = new WriteAnnotationClassVisitor(writer);				
			reader.accept(visitor.setElement(element), ClassReader.SKIP_CODE);
			
			Files.write(file, writer.toByteArray(), StandardOpenOption.DSYNC);
		}
	}
	
	private ClassElement findClass(String completeName) {		
		String packageName = computePackageName(completeName);
		
		List<ClassElement> classes = model.getClasses(packageName);
		if (classes != null && !classes.isEmpty()) {
			String className = computeClassName(completeName);
			for (ClassElement classElement : classes) {
				if (classElement.getName().equals(className)) {
					return classElement;
				}
			}
		}
		
		return null;		
	}
}