package rtt.annotation.editor.model;

import rtt.annotations.Node;
import rtt.annotations.Node.Value;


@Node
public class FieldElement extends Annotatable<ClassElement> {	

	@Value private String type;

	protected FieldElement(ClassElement parent) {
		super(parent);		
	}

	public String getType() {
		return type;
	}
	
	public void setType(String typeName) {
		this.type = typeName;
	}
}
