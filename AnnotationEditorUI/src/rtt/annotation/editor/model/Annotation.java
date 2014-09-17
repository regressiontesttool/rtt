package rtt.annotation.editor.model;

import java.util.Hashtable;
import java.util.Map;

import rtt.annotations.Node.Value;

/**
 * This class represents an annotations from RTT.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
public class Annotation {
	
	public enum AnnotationType {
		NODE("Node"), VALUE("Value"), INITIALIZE("Initialize");
		
		private String name;

		private AnnotationType(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
	
	@Value private AnnotationType type;
	private Map<String, Object> attributes;	

	private Annotation(AnnotationType type) {
		this.type = type;
		this.attributes = new Hashtable<>();
	}
	
	public AnnotationType getType() {
		return type;
	}
	
	public String getName() {
		return type.getName();
	}
	
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	
	public void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}
	
	public static Annotation create(AnnotationType type) {
		Annotation annotation = new Annotation(type);
		
		switch (type) {
		case VALUE:
			annotation.setAttribute("index", 100);
			annotation.setAttribute("name", "");
			annotation.setAttribute("informational", false);
			break;
		case INITIALIZE:
			annotation.setAttribute("withParams", false);
			break;
		default:
			break;
		}
		
		return annotation;
	}
}
