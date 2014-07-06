package rtt.annotation.editor.data.asm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import rtt.annotation.editor.data.Importer;
import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.FieldElement;
import rtt.annotation.editor.model.MethodElement;

public class ASMImporter implements Importer {
	
	private ClassModelFactory factory;	
	
	public ASMImporter() {
		factory = ClassModelFactory.getFactory();
	}

	@Override
	public ClassModel importModel(URI input) throws IOException {
		Map<String, String> env = new HashMap<>();
		env.put("create", "false");
		
		URI zipUri = URI.create("jar:" + input);
		ClassModel model = factory.createClassModel();
		
		try (FileSystem zipFs = FileSystems.newFileSystem(zipUri, env)) {
			Files.walkFileTree(zipFs.getPath("/"), new ImportModelFileWalker(model));
		}
		
		return model;
	}
	
	public void importClass(InputStream in, ClassModel model) throws IOException {
		ClassReader reader = new ClassReader(in);
		ClassNode node = new ClassNode(Opcodes.ASM5);
		
		reader.accept(node, ClassReader.SKIP_CODE);
		
		ASMClassNodeImporter nodeImporter = new ASMClassNodeImporter(node, factory);
		ClassElement element = nodeImporter.createClassElement(model);		
		
		for (Object fieldObject : node.fields) {
			importFieldElement((FieldNode) fieldObject, element);
		}
		
		for (Object method : node.methods) {
			importMethodElement((MethodNode) method, element);
		}
		
		model.addClassElement(element);
	}

	private void importFieldElement(FieldNode fieldNode, ClassElement classElement) {
		if (!isSynthetic(fieldNode)) {
			FieldElement element = factory.createFieldElement(classElement, fieldNode.name);
			
			Type fieldType = Type.getType(fieldNode.desc);
			element.setType(fieldType.getClassName());
			
			classElement.addField(element);
		}		
	}
	

	private boolean isSynthetic(FieldNode fieldNode) {
		return (fieldNode.access & Opcodes.ACC_SYNTHETIC) == Opcodes.ACC_SYNTHETIC;
	}

	private void importMethodElement(MethodNode methodNode, ClassElement classElement) {
		Type methodType = Type.getMethodType(methodNode.desc);
		if (!hasVoidReturnType(methodType) && !hasArguments(methodType)) {
			
			MethodElement methodElement = factory.createMethodElement(classElement, methodNode.name);
			methodElement.setType(methodType.getReturnType().getClassName());
			
			classElement.addMethod(methodElement);
		}
	}

	private boolean hasVoidReturnType(Type methodType) {
		return Type.VOID_TYPE.equals(methodType.getReturnType());
	}

	private boolean hasArguments(Type methodType) {
		return methodType.getArgumentTypes().length > 0;
	}
	
	
}
