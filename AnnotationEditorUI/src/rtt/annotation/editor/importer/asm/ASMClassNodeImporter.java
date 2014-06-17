package rtt.annotation.editor.importer.asm;

import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassModelFactory;

public class ASMClassNodeImporter {
	
	private ClassNode node;
	private ClassModelFactory factory;
	
	public ASMClassNodeImporter(ClassNode node, ClassModelFactory factory) {
		this.node = node;
		this.factory = factory;
	}	

	public ClassElement createClassElement() {
		ClassElement element = factory.createClassElement();
		
		String nodeName = node.name;
		int packageBoundary = nodeName.lastIndexOf("/");
		
		String packageName = "";		
		if (packageBoundary > -1) {
			packageName = nodeName.substring(0, packageBoundary);
			nodeName = nodeName.substring(packageBoundary + 1, nodeName.length());
		}		
		
		element.setName(nodeName);
		element.setPackageName(packageName.replace("/", "."));
		
		element.setInterface(checkAccess(node, Opcodes.ACC_INTERFACE));
		element.setAbstract(checkAccess(node, Opcodes.ACC_ABSTRACT));
		
		element.setInterfaces((List<String>) node.interfaces);

		return element;
	}
	
	private boolean checkAccess(ClassNode node, int code) {
		return (node.access & code) == code;
	}

}
