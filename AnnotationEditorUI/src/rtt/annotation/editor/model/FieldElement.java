package rtt.annotation.editor.model;

import rtt.annotations.Parser.Node;


@Node
public class FieldElement extends Annotatable<ClassElement> {	

	private String type;

	protected FieldElement(ClassElement parent) {
		super(parent);		
	}

	@Node.Compare
	public String getType() {
		return type;
	}
	
	public void setType(String typeName) {
		this.type = typeName;
	}
}
