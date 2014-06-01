package rtt.annotation;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.tree.ClassNode;

public class ClassModel {

	private List<ClassNode> nodes;
	
	public ClassModel() {
		nodes = new ArrayList<ClassNode>();
	}

	public void add(ClassNode node) {
		nodes.add(node);
	}

}
