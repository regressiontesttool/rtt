package rtt.annotation.editor.model;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.Annotation;
import rtt.annotations.Node;
import rtt.annotations.Node.Value;

/**
 * Represents a java field.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
@Node
public class FieldElement<T extends Annotation> extends Annotatable<T> {	

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
