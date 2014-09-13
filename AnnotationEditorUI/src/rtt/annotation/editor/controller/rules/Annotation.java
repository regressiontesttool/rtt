package rtt.annotation.editor.controller.rules;

import java.util.Hashtable;
import java.util.Map;

public enum Annotation {
	// TODO remove NONE
	NONE (""), 
	NODE ("Node"), 
	VALUE ("Value"),
	INITIALIZE ("Initialize");
	
	private String prettyName;
	private Map<String, Object> attributes;

	private Annotation(String prettyName) {
		this.prettyName = prettyName;
		this.attributes = new Hashtable<>();
	}

	public String getPrettyName() {
		return prettyName;
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}
}
