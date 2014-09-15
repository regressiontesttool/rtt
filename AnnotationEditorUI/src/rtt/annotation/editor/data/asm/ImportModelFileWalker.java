package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import rtt.annotation.editor.model.Annotatable;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElement.ClassType;
import rtt.annotation.editor.model.ClassElementReference;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;
import rtt.annotation.editor.model.Annotation;

final class ImportModelFileWalker extends AbstractFileWalker {
	
	protected static final class RTTAnnotationVisitor extends AnnotationVisitor {

		private Annotation annotation;

		public RTTAnnotationVisitor(Annotation annotation) {
			super(Opcodes.ASM5);
			this.annotation = annotation;
		}
		
		@Override
		public void visit(String name, Object value) {
			annotation.setAttribute(name, value);
		}		
	}
	
	public ImportModelFileWalker(ClassModel model) {
		super(model);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void processData(Path file) throws IOException {
		ClassReader reader = new ClassReader(Files.readAllBytes(file));
		
		ClassNode node = new ClassNode();	
		reader.accept(node, ClassReader.SKIP_CODE);
		
		String completeName = node.name.replace("/", ".");
		
		String className = ASMClassModelManager.RESOLVER.computeClassName(completeName);
		String packageName = ASMClassModelManager.RESOLVER.computePackageName(completeName);
		
		ClassModelFactory factory = ClassModelFactory.getFactory();
		
		ClassElement classElement = factory.createClassElement(
				model, className, packageName);
		
		if (checkAccess(node.access, Opcodes.ACC_ABSTRACT)) {
			classElement.setType(ClassType.ABSTRACT);
		}
		
		if (checkAccess(node.access, Opcodes.ACC_INTERFACE)) {
			classElement.setType(ClassType.INTERFACE);
		}
		
		if (checkAccess(node.access, Opcodes.ACC_ENUM)) {
			classElement.setType(ClassType.ENUMERATION);
		}
		
		if (node.superName != null && !node.superName.equals("java/lang/Object")) {
			String superName = node.superName.replace("/", ".");
			classElement.setSuperClass(ClassElementReference.create(superName));
		}
		
		if (node.interfaces != null && !node.interfaces.isEmpty()) {
			List<ClassElementReference> interfaceReferences = new ArrayList<>();
			String interfaceName = null;
			for (Object interfaceObject : node.interfaces) {
				interfaceName = ((String) interfaceObject).replace("/", ".");
				interfaceReferences.add(ClassElementReference.create(interfaceName));
			}
			
			classElement.setInterfaces(interfaceReferences);
		}
		
		setAnnotation(node.visibleAnnotations, classElement);
		
		if (node.fields != null && !node.fields.isEmpty()) {
			addFields(node.fields, classElement);
		}
		
		if (node.methods != null && !node.methods.isEmpty()) {
			addMethods(node.methods, classElement);
		}		
		
		model.addClassElement(classElement);
	}

	private boolean checkAccess(int access, int code) {
		return (access & code) == code;
	}
	
	private boolean isSynthetic(int access) {
		return checkAccess(access, Opcodes.ACC_SYNTHETIC);
	}
	
	private void setAnnotation(List<AnnotationNode> annotations, 
			Annotatable annotatable) {
		
		if (annotations != null && !annotations.isEmpty()) {
			for (AnnotationNode annotationNode : annotations) {
				Annotation annotation = ASMAnnotationConverter.
						getAnnotation(annotationNode.desc);
				
				if (annotation != null) {
					annotationNode.accept(new RTTAnnotationVisitor(annotation));					
					annotatable.setAnnotation(annotation);
				}
			}
		}	
	}
	
	@SuppressWarnings("unchecked")
	private void addFields(List<FieldNode> fields, ClassElement classElement) {
		for (FieldNode fieldNode : fields) {
			if (!isSynthetic(fieldNode.access)) {
				FieldElement fieldElement = factory.createFieldElement(
						classElement, fieldNode.name);
				
				fieldElement.setType(Type.getType(fieldNode.desc).getClassName());
				
				setAnnotation(fieldNode.visibleAnnotations, fieldElement);
				
				classElement.addValuableField(fieldElement);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void addMethods(List<MethodNode> methods, ClassElement classElement) {
		for (MethodNode methodNode : methods) {			
			if (!isSynthetic(methodNode.access)) {
				Type methodType = Type.getType(methodNode.desc);
				MethodElement method = factory.createMethodElement(
						classElement, methodNode.name);
				
				method.setType(methodType.getReturnType().getClassName());	
				setAnnotation(methodNode.visibleAnnotations, method);
				
				if (isValuableMethod(methodType)) {
					classElement.addValuableMethod(method);
				} else if (isInitializableMethod(methodType)) {
					Type[] arguments = Type.getArgumentTypes(methodNode.desc);
					for (Type argument : arguments) {
						method.getParameters().add(argument.getClassName());
					}				
					
					classElement.addInitializableMethod(method);
				}
			}			
		}
	}
	
	private boolean isValuableMethod(Type methodType) {
		return hasNonVoidReturnType(methodType) && 
				methodType.getArgumentTypes().length == 0;
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