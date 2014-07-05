package rtt.annotation.editor.data.asm;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import rtt.annotation.editor.model.ClassElement;
import rtt.annotation.editor.model.ClassElement.ClassType;
import rtt.annotation.editor.model.ClassElementReference;
import rtt.annotation.editor.model.ClassModel;
import rtt.annotation.editor.model.ClassModelFactory;
import rtt.annotation.editor.model.NameResolver;

public class ASMClassNodeImporter {
	
	private ClassNode node;
	private ClassModelFactory factory;
	
	public ASMClassNodeImporter(ClassNode node, ClassModelFactory factory) {
		this.node = node;
		this.factory = factory;
	}	

	@SuppressWarnings("unchecked")
	public ClassElement createClassElement(ClassModel parent) {
		ClassElement element = factory.createClassElement(parent);
		
		String nodeName = node.name.replace("/", ".");
		String packageName = NameResolver.computePackageName(nodeName);
		String className = NameResolver.computeClassName(nodeName);
		
		element.setName(className);
		element.setPackageName(packageName);
		
		if (checkAccess(node, Opcodes.ACC_ABSTRACT)) {
			element.setType(ClassType.ABSTRACT);
		}
		
		if (checkAccess(node, Opcodes.ACC_INTERFACE)) {
			element.setType(ClassType.INTERFACE);
		}
		
		List<ClassElementReference> interfaceList = new ArrayList<ClassElementReference>();
		for (String interfaceName : (List<String>) node.interfaces) {
			interfaceList.add(new ClassElementReference(interfaceName.replace("/", ".")));
		}
		element.setInterfaces(interfaceList);
		
		if (node.superName != null && !node.superName.equals("java/lang/Object")) {
			element.setSuperClass(new ClassElementReference(node.superName.replace("/", ".")));
		}

		return element;
	}
	
	private boolean checkAccess(ClassNode node, int code) {
		return (node.access & code) == code;
	}

}
