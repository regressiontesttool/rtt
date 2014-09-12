package prototype.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import prototype.asm.model.ClassElement;
import prototype.asm.model.ClassModel;
import prototype.asm.model.ClassModel.PackageElement;
import annotation.MyAnnotation;

final class WriteFileWalker extends AbstractFileWalker {
	
	public WriteFileWalker(ClassModel model) {
		super(model);
	}
	
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		final ClassElement element = findClass(reader.getClassName()); 
		if (element != null && element.hasChanged()) {
			final ClassWriter writer = new ClassWriter(reader, 0);
			System.out.println("Change: " + file.toString());
			
			final ClassVisitor myClassVisitor = new ClassNode(Opcodes.ASM5) {
				public void visitEnd() {
					boolean annotationPresent = false;
					
					if (this.visibleAnnotations != null) {
						Iterator<AnnotationNode> iterator = this.visibleAnnotations.iterator();
						while (iterator.hasNext()) {
							AnnotationNode annotation = iterator.next();
							if (annotation.desc.equals(Type.getDescriptor(MyAnnotation.class))) {
								annotationPresent = true;
								if (element.hasAnnotation() == false) {
									iterator.remove();
								}
							}
						}
					}					
					
					if (!annotationPresent && element.hasAnnotation()) {
						AnnotationNode annotation = new AnnotationNode(Type.getDescriptor(MyAnnotation.class));
						annotation.visit("name", "HelloWorld");
						annotation.visit("index", 499);
						annotation.visit("informational", true);
						
						if (this.visibleAnnotations == null) {
							this.visibleAnnotations = new ArrayList<>();
						}
						
						this.visibleAnnotations.add(annotation);
					}
					
					accept(writer);					
				};
			};
			
			final ClassVisitor myMethodClassVisitor = new ClassNode(Opcodes.ASM5) {
				@Override
				public FieldVisitor visitField(int access, String name,
						String desc, String signature, Object value) {
					return new FieldNode(Opcodes.ASM5, access, name, desc, signature, value) {
						@Override
						public void visitEnd() {
							boolean annotationPresent = false;
							if (this.visibleAnnotations != null) {
								Iterator<AnnotationNode> iterator = this.visibleAnnotations.iterator();
								while (iterator.hasNext()) {
									AnnotationNode annotation = iterator.next();
									if (annotation.desc.equals(Type.getDescriptor(MyAnnotation.class))) {
										annotationPresent = true;
										if (element.hasAnnotation() == false) {
											iterator.remove();
										}
									}
								}
							}
							
							if (!annotationPresent && element.hasAnnotation()) {
								AnnotationNode annotation = new AnnotationNode(Type.getDescriptor(MyAnnotation.class));
								annotation.visit("name", "HelloWorld");
								annotation.visit("index", 499);
								annotation.visit("informational", true);
								
								if (this.visibleAnnotations == null) {
									this.visibleAnnotations = new ArrayList<>();
								}
								
								this.visibleAnnotations.add(annotation);
							}
							
							accept(myClassVisitor);
						}
					};
				}				
				
				@Override
				public MethodVisitor visitMethod(int access, String name,
						String desc, String signature, String[] exceptions) {
					return new MethodNode(Opcodes.ASM5, access, name, desc, signature, exceptions) {
						@Override
						public void visitEnd() {
							boolean annotationPresent = false;
							if (this.visibleAnnotations != null) {
								Iterator<AnnotationNode> iterator = this.visibleAnnotations.iterator();
								while (iterator.hasNext()) {
									AnnotationNode annotation = iterator.next();
									if (annotation.desc.equals(Type.getDescriptor(MyAnnotation.class))) {
										annotationPresent = true;
										if (element.hasAnnotation() == false) {
											iterator.remove();
										}
									}
								}
							}
							
							if (!annotationPresent && element.hasAnnotation()) {
								AnnotationNode annotation = new AnnotationNode(Type.getDescriptor(MyAnnotation.class));
								annotation.visit("name", "HelloWorld");
								annotation.visit("index", 499);
								annotation.visit("informational", true);
								
								if (this.visibleAnnotations == null) {
									this.visibleAnnotations = new ArrayList<>();
								}
								
								this.visibleAnnotations.add(annotation);
							}
							
							accept(myClassVisitor);
						}
					};
				}
				
				@Override
				public void visitEnd() {
					accept(myClassVisitor);
				}
			};			
			
			reader.accept(myMethodClassVisitor, ClassReader.SKIP_CODE);
			
			Files.write(file, writer.toByteArray(), StandardOpenOption.WRITE);
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