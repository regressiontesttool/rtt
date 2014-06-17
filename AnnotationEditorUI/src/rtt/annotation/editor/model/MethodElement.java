package rtt.annotation.editor.model;

import rtt.annotations.Parser.Node;

@Node
public class MethodElement extends Annotatable<ClassElement> {
	
	private String name = null;
	
	protected MethodElement() {}
	
	@Node.Compare
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
}
