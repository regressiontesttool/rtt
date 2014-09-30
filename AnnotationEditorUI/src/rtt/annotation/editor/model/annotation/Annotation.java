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
		
		protected String getName() {
			return name;
		}
	}

	private AnnotationType type;
	
	protected Annotation(AnnotationType type) {
		this.type = type;
	}
	
	public String getName() {
		return type.getName();
	}
	
	public AnnotationType getType() {
		return type;
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T create(Class<T> type) {
		T annotation = null;
		
		if (NodeAnnotation.class.equals(type)) {
			annotation = (T) new NodeAnnotation();
		}
		
		return annotation;
	}
}
