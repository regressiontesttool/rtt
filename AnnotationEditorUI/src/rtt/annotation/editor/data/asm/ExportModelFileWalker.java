package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;

final class ExportModelFileWalker extends AbstractFileWalker {
	
	public class ExportClassAdapter extends ClassNode {
		private ClassElement element;

		public ExportClassAdapter(ClassVisitor cv, ClassElement element) {
			super(Opcodes.ASM5);
			this.cv = cv;
			this.element = element;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void visitEnd() {
			// set JRE version to at least 1.5
			if ((version & 0xFF) < Opcodes.V1_5) {
				version = Opcodes.V1_5;
			}

			if (visibleAnnotations != null) {
				removeObsoleteAnnotations(visibleAnnotations);
			}

			if (element.hasAnnotation()) {
				if (visibleAnnotations == null) {
					visibleAnnotations = new ArrayList<>();
				}

				addAnnotation(element, visibleAnnotations);
			}

			processFields(this, element.getValuableFields());			
			processMethods(this, element.getValuableMethods());
			processMethods(this, element.getInitializableMethods());			
			
			accept(cv);
		}
	}

	public ExportModelFileWalker(ClassModel model) {
		super(model);
	}
	
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		String className = reader.getClassName().replace("/", ".");
		
		final ClassElement element = ASMClassModelManager.RESOLVER.findClass(className, model);
		
		if (element != null && element.hasChanged()) {
			ClassWriter writer = new ClassWriter(reader, 0);
			ClassVisitor visitor = new ExportClassAdapter(writer, element);
			
			reader.accept(visitor, 0);
			
			Files.write(file, writer.toByteArray(), StandardOpenOption.WRITE);
		}
	}

	private void removeObsoleteAnnotations(List<AnnotationNode> annotations) {
		Iterator<AnnotationNode> iterator = annotations.iterator();
		while (iterator.hasNext()) {
			AnnotationNode annotationNode = iterator.next();
			Annotation annotation = ASMAnnotationConverter.
					getAnnotation(annotationNode.desc);
			
			if (annotation != null) {
				iterator.remove();
			}
		}
	}
	
	private void addAnnotation(Annotatable<?> element, List<AnnotationNode> annotations) {
		if (element.hasAnnotation()) {
			Annotation annotation = element.getAnnotation();
			String descriptor = ASMAnnotationConverter.getDescriptor(annotation.getClass());
			AnnotationNode annotationNode = new AnnotationNode(descriptor);
			for (String key : annotation.getKeys()) {
				annotationNode.visit(key, annotation.getAttribute(key));
			}
			
			annotations.add(annotationNode);
		}
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Annotation> void processFields(ClassNode node, 
			List<FieldElement<T>> elements) {
		
		for (FieldElement<?> fieldElement : elements) {
			if (fieldElement.hasChanged()) {
				FieldNode fieldNode = findField(node.fields, fieldElement);
				
				if (fieldNode.visibleAnnotations != null) {
					removeObsoleteAnnotations(fieldNode.visibleAnnotations);
				}
				
				if (fieldElement.hasAnnotation()) {
					if (fieldNode.visibleAnnotations == null) {
						fieldNode.visibleAnnotations = new ArrayList<>();
					}
					
					addAnnotation(fieldElement, fieldNode.visibleAnnotations);
				}
			}
		}
	}
	
	private FieldNode findField(List<FieldNode> fields, FieldElement<?> fieldElement) {
		String className = null;
		for (FieldNode fieldNode : fields) {
			className = Type.getType(fieldNode.desc).getClassName();
			if (fieldElement.getName().equals(fieldNode.name) && 
					fieldElement.getType().equals(className)) {
				return fieldNode;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private <T extends Annotation> void processMethods(ClassNode node, 
			List<MethodElement<T>> elements) {
		
		for(MethodElement<?> methodElement : elements) {
			if (methodElement.hasChanged()) {
				MethodNode methodNode = findMethod(node.methods, methodElement);
				
				if (methodNode.visibleAnnotations != null) {
					removeObsoleteAnnotations(methodNode.visibleAnnotations);
				}
				
				if (methodElement.hasAnnotation()) {
					if (methodNode.visibleAnnotations == null) {
						methodNode.visibleAnnotations = new ArrayList<>();
					}
					
					addAnnotation(methodElement, methodNode.visibleAnnotations);
				}
			}
		}
	}
	
	private MethodNode findMethod(List<MethodNode> methods, MethodElement<?> method) {
		String returningClass = null;
		for (MethodNode methodNode : methods) {
			returningClass = Type.getReturnType(methodNode.desc).getClassName();
			if (method.getName().equals(methodNode.name) && 
					method.getType().equals(returningClass) &&
					equalParameters(method, methodNode)) {				
				
				return methodNode;
			}
		}
		
		return null;
	}

	private boolean equalParameters(MethodElement<?> method, MethodNode methodNode) {
		Type[] parameters = Type.getArgumentTypes(methodNode.desc);
		if (method.getParameters().size() != parameters.length) {
			return false;
		}
		
		for(int i = 0; i < parameters.length; i++) {
			if (!method.getParameters().get(i).equals(parameters[i].getClassName())) {
				return false;
			}
		}
		
		return true;
	}
}