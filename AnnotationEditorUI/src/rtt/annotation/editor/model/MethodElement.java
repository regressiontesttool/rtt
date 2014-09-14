package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Node;
import rtt.annotations.Node.Value;

@Node
public class MethodElement extends Annotatable<ClassElement> {
	
	@Value private String type = null;
	@Value private List<Class<?>> parameters;
	
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

	public List<Class<?>> getParameters() {
		return parameters;
	}
}
