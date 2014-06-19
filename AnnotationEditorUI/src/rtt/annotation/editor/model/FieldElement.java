package rtt.annotation.editor.model;

import rtt.annotations.Parser.Node;


@Node
public class FieldElement extends Annotatable<ClassElement> {	

	private String className;

	protected FieldElement(ClassElement parent) {
		super(parent);		
	}

	@Node.Compare
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
}
