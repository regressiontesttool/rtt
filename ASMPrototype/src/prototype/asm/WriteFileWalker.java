package prototype.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import prototype.asm.model.ClassElement;
import prototype.asm.model.ClassElement.Annotation;
import prototype.asm.model.ClassModel;
import prototype.asm.model.ClassModel.PackageElement;

final class WriteFileWalker extends AbstractFileWalker {
	
	private static final class WriteAnnotationClassVisitor extends ClassVisitor {

		private ClassElement element;
		private boolean hasNodeAnnotation = false;
		
		public WriteAnnotationClassVisitor(ClassVisitor cv) {
			super(Opcodes.ASM5, cv);
		}
		
		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			if (desc.equals(ASMPrototype.NODE_DESC)) {
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
				AnnotationVisitor av = cv.visitAnnotation(ASMPrototype.NODE_DESC, true);
				if (av != null) {
					av.visitEnd();
				}
			}
			
			cv.visitEnd();
		}		
	}
	
	public WriteFileWalker(ClassModel model) {
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
		PackageElement packageElement = new PackageElement(computePackageName(completeName));
		
		Set<PackageElement> packages = model.getClasses().keySet();
		if (packages.contains(packageElement)) {
			List<ClassElement> classes = model.getClasses(packageElement);
			
			String className = computeClassName(completeName);
			for (ClassElement classElement : classes) {
				if (classElement.getClassName().equals(className)) {
					return classElement;
				}
			}
		}
		
		return null;		
	}
}