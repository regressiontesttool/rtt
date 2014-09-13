package prototype.asm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import rtt.annotation.editor.controller.rules.Annotation;
import rtt.annotation.editor.data.asm.ASMAnnotationConverter;
import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElement.ClassType;
import rtt.annotation.editor.model.ClassElementReference;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

final class ReadFileWalker extends AbstractFileWalker {
	
	private final class MyAnnotationVisitor extends AnnotationVisitor {
		private final Annotation annotation;

		private MyAnnotationVisitor(Annotation annotation) {
			super(Opcodes.ASM5);
			this.annotation = annotation;
		}

		@Override
		public void visit(String name, Object value) {
			annotation.setAttribute(name, value);
		}
	}
	
	public ReadFileWalker(ClassModel model) {
		super(model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		
		ClassNode node = new ClassNode();
		reader.accept(node, ClassReader.SKIP_CODE);		
		
		String className = computeClassName(node.name);
		String packageName = computePackageName(node.name);
		System.out.println("Reading class: " + packageName + "." + className);		
		
		ClassModelFactory factory = ClassModelFactory.getFactory();
		
		ClassElement newElement = factory.createClassElement(model);
		newElement.setPackageName(packageName);
		newElement.setName(className);
		
		if (checkAccess(node.access, Opcodes.ACC_ABSTRACT)) {
			newElement.setType(ClassType.ABSTRACT);
		}
		
		if (checkAccess(node.access, Opcodes.ACC_INTERFACE)) {
			newElement.setType(ClassType.INTERFACE);
		}
		
		if (checkAccess(node.access, Opcodes.ACC_ENUM)) {
			newElement.setType(ClassType.ENUMERATION);
		}
		
		if (node.superName != null && !node.superName.equals("java/lang/Object")) {
			String superName = node.superName.replace("/", ".");
			newElement.setSuperClass(new ClassElementReference(superName));
		}
		
		if (node.interfaces != null && !node.interfaces.isEmpty()) {
			List<ClassElementReference> interfaceReferences = new ArrayList<>();
			String interfaceName = null;
			for (Object interfaceObject : node.interfaces) {
				interfaceName = ((String) interfaceObject).replace("/", ".");
				interfaceReferences.add(new ClassElementReference(interfaceName));
			}
			
			newElement.setInterfaces(interfaceReferences);
		}
		
		if (node.superName != null && !node.superName.equals("java/lang/Object")) {
			newElement.setSuperClass(new ClassElementReference(node.superName));
		}
		
		if (node.visibleAnnotations != null && !node.visibleAnnotations.isEmpty()) {
			readAnnotation(node.visibleAnnotations, newElement);
		}
		
		if (node.fields != null && !node.fields.isEmpty()) {
			for (Object fieldObject : node.fields) {
				FieldNode fieldNode = (FieldNode) fieldObject;
				
				if (!isSynthetic(fieldNode.access)) {
					FieldElement fieldElement = factory.createFieldElement(newElement, fieldNode.name);
					fieldElement.setType(Type.getType(fieldNode.desc).getClassName());
					
					readAnnotation(fieldNode.visibleAnnotations, fieldElement);
					
					newElement.addValuableField(fieldElement);
				}
			}
		}
		
		if (node.methods != null && !node.methods.isEmpty()) {
			for (Object methodObject : node.methods) {
				MethodNode methodNode = (MethodNode) methodObject;
				MethodElement methodElement = factory.createMethodElement(newElement, methodNode.name);
				methodElement.setType(methodNode.desc);
				
				System.out.println("Method: " + methodNode.desc);
				System.out.println(Arrays.toString(Type.getArgumentTypes(methodNode.desc)));
				
				readAnnotation(methodNode.visibleAnnotations, methodElement);
				
				newElement.addValuableMethod(methodElement);
			}
		}
		
		model.addClassElement(newElement);
	}
	
	private boolean checkAccess(int access, int code) {
		return (access & code) == code;
	}
	
	private boolean isSynthetic(int access) {
		return (access & Opcodes.ACC_SYNTHETIC) == Opcodes.ACC_SYNTHETIC;
	}

	private void readAnnotation(List<AnnotationNode> visibleAnnotations, Annotatable<?> newElement) {
		if (visibleAnnotations != null && !visibleAnnotations.isEmpty()) {
			for (AnnotationNode annotationNode : visibleAnnotations) {
				ASMAnnotationConverter descriptor = 
						ASMAnnotationConverter.findByDescriptor(annotationNode.desc);
				
				if (descriptor != null) {
					Annotation annotation = descriptor.getAnnotation();
					annotationNode.accept(new MyAnnotationVisitor(annotation));
					
					newElement.setAnnotation(annotation);
				}
			}
		}	
	}	
}