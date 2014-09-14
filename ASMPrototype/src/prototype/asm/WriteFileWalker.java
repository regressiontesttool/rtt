package prototype.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import rtt.annotation.editor.data.asm.ASMAnnotationConverter;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.RTTAnnotation;

final class WriteFileWalker extends AbstractFileWalker {
	
	public WriteFileWalker(ClassModel model) {
		super(model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		final ClassElement element = findClass(reader.getClassName()); 
		if (element != null && element.hasChanged()) {
			ClassNode node = new ClassNode();
			reader.accept(node, ClassReader.SKIP_CODE);
			
			// set JRE version to at least 1.5
			if ((node.version & 0xFF) < Opcodes.V1_5) {
				node.version = Opcodes.V1_5;
			}
			
			for (FieldElement fieldElement : element.getValuableFields()) {
				if (fieldElement.hasChanged()) {
					FieldNode fieldNode = findField(node.fields, 
							fieldElement.getName(), fieldElement.getType());
					
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
			
			for(MethodElement methodElement: element.getValuableMethods()) {
				if (methodElement.hasChanged()) {
					MethodNode methodNode = findMethod(node.methods,
							methodElement.getName(), methodElement.getType());
					
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
			
			if (node.visibleAnnotations != null) {
				removeObsoleteAnnotations(node.visibleAnnotations);
			}
			
			if (element.hasAnnotation()) {
				if (node.visibleAnnotations == null) {
					node.visibleAnnotations = new ArrayList<>();
				}
				
				addAnnotation(element, node.visibleAnnotations);
			}
			
			System.out.println("Operating: " + file.toString());
			final ClassWriter writer = new ClassWriter(reader, 0);			
			node.accept(writer);
			
			Files.write(file, writer.toByteArray(), StandardOpenOption.WRITE);
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
	
	private FieldNode findField(List<FieldNode> fields, 
			String fieldName, String fieldType) {
		
		for (FieldNode fieldNode : fields) {
			if (fieldNode.name.equals(fieldName) && 
					fieldNode.desc.equals(fieldType)) {				
				return fieldNode;
			}
		}

		return null;
	}
	
	private MethodNode findMethod(List<MethodNode> methods, 
			String methodName, String methodType) {
		
		for (MethodNode methodNode : methods) {
			if (methodNode.name.equals(methodName) && 
					methodNode.desc.equals(methodType)) {
				return methodNode;
			}
		}
		
		return null;
	}
	
	private void removeObsoleteAnnotations(List<AnnotationNode> visibleAnnotations) {
		Iterator<AnnotationNode> iterator = visibleAnnotations.iterator();
		while (iterator.hasNext()) {
			AnnotationNode annotationNode = iterator.next();
			RTTAnnotation annotation = ASMAnnotationConverter.getAnnotation(
					annotationNode.desc);
			
			if (annotation != null) {
				iterator.remove();
			}
		}
	}
	
	private void addAnnotation(Annotatable<?> element, List<AnnotationNode> visibleAnnotations) {
		RTTAnnotation annotation = element.getAnnotation();
		if (annotation != null) {
			String descriptor = ASMAnnotationConverter.getDescriptor(annotation.getType());
			AnnotationNode annotationNode = new AnnotationNode(descriptor);
			for (Entry<String, Object> attribute : annotation.getAttributes().entrySet()) {
				annotationNode.visit(attribute.getKey(), attribute.getValue());
			}
			
			visibleAnnotations.add(annotationNode);
		}		
	}	
}
