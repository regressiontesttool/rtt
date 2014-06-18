package rtt.annotation.editor.importer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import rtt.annotation.editor.importer.asm.ASMClassNodeImporter;
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
	public ClassModel importModel(File inputFile) throws IOException {
		JarFile jar = new JarFile(inputFile, false, JarFile.OPEN_READ);
		Enumeration<JarEntry> entries = jar.entries();
		
		ClassModel model = factory.createClassModel();
		
		while(entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			System.out.println("Entry: " + entry.getName());
			if (entry.getName().endsWith(".class")) {
				importClass(jar.getInputStream(entry), model);
			}
		}
		
		jar.close();
		
		return model;
	}
	
	@Override
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
		FieldElement element = factory.createFieldElement(classElement);		
		element.setName(fieldNode.name);
		
		classElement.addField(element);
	}
	
	private void importMethodElement(MethodNode methodNode, ClassElement classElement) {
		MethodElement methodElement = factory.createMethodElement(classElement);	
		methodElement.setName(methodNode.name);
		
		classElement.addMethod(methodElement);
	}
}
