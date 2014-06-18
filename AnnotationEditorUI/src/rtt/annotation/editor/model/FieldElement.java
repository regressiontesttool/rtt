package rtt.annotation.editor.model;

import rtt.annotations.Parser.Node;


@Node
public class FieldElement extends Annotatable<ClassElement> {	

	protected FieldElement(ClassElement parent) {
		super(parent);		
	}
}
