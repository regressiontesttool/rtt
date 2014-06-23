package rtt.annotation.editor.model;

import rtt.annotations.Parser.Node;

@Node
public class MethodElement extends Annotatable<ClassElement> {
	
	private String type = null;
	
	protected MethodElement(ClassElement parent) {
		super(parent);
	}
	
	@Node.Compare
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
