package rtt.annotation.editor.model.annotation;


/**
 * This class represents an annotations from RTT.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
public abstract class Annotation {
	
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

	private AnnotationType type;
	private String[] keys;
	
	protected Annotation(AnnotationType type, String... keys) {
		this.type = type;
		this.keys = keys;
	}
	
	public String getName() {
		return type.getName();
	}
	
	public AnnotationType getType() {
		return type;
	}
	
	public String[] getKeys() {
		return keys;
	}
	
	public abstract void setAttribute(String key, Object value);
	public abstract Object getAttribute(String key);
	
	@SuppressWarnings("unchecked")
	public static <A extends Annotation> A create(Class<A> type) {
		A annotation = null;
		
		if (NodeAnnotation.class.equals(type)) {
			annotation = (A) new NodeAnnotation();
		} else if (ValueAnnotation.class.equals(type)) {
			annotation = (A) new ValueAnnotation();
		} else if (InitAnnotation.class.equals(type)) {
			annotation = (A) new InitAnnotation();
		}
		
		return annotation;
	}
}
