package rtt.annotation.editor.model.importer;

import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.eclipse.core.resources.IFile;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

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
	public ClassModel importModel(IFile inputFile) throws IOException {
		JarFile jar = new JarFile(inputFile.getLocation().toFile(), false, JarFile.OPEN_READ);
		Enumeration<JarEntry> entries = jar.entries();
		
		ClassReader reader = null;
		ClassModel model = factory.createClassModel();
		
		while(entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			System.out.println("Entry: " + entry.getName());
			if (entry.getName().endsWith(".class")) {
				reader = new ClassReader(jar.getInputStream(entry));
				ClassNode node = new ClassNode(Opcodes.ASM5);
				
				reader.accept(node, ClassReader.SKIP_CODE);
				
				importClass(node, model);
			}
		}
		
		jar.close();
		
		return model;
	}
	
	public void importClass(ClassNode node, ClassModel model) {
		String className = node.name.replace("/", ".");
		String packageName = "";
		int packageBoundary = className.lastIndexOf(".");
		
		if (packageBoundary > -1) {
			packageName = className.substring(0, packageBoundary);
			className = className.substring(packageBoundary + 1, className.length());
		}
		
		ClassElement element = factory.createClassElement();		
		element.setName(className);
		element.setPackageName(packageName);
		
		for (Object fieldObject : node.fields) {
			importFieldElement((FieldNode) fieldObject, element);
		}
		
		for (Object method : node.methods) {
			importMethodElement((MethodNode) method, element);
		}
		
		model.addClassElement(element);
	}

	private void importFieldElement(FieldNode fieldNode, ClassElement classElement) {
		FieldElement element = factory.createFieldElement();		
		element.setName(fieldNode.name);
		
		classElement.addField(element);
	}
	
	private void importMethodElement(MethodNode methodNode, ClassElement classElement) {
		MethodElement methodElement = factory.createMethodElement();	
		methodElement.setName(methodNode.name);
		
		classElement.addMethod(methodElement);
	}
}
