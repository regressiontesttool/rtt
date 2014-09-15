package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Node;
import rtt.annotations.Node.Value;

/**
 * Represents a java method.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
@Node
public class MethodElement extends Annotatable {
	
	@Value private String type = null;
	@Value private List<String> parameters;
	
	protected MethodElement(ClassElement parent) {
		super(parent);
		parameters = new ArrayList<>();
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public List<String> getParameters() {
		return parameters;
	}
}
