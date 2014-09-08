package rtt.annotation.editor.model;

import rtt.annotations.Node;
import rtt.annotations.Node.Value;

@Node
public class MethodElement extends Annotatable<ClassElement> {
	
	@Value private String type = null;
	
	protected MethodElement(ClassElement parent) {
		super(parent);
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
}
