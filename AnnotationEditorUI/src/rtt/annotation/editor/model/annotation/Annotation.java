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
	
	public static <T extends Annotation> T create(AnnotationType type) {
//		Annotation annotation = new Annotation(type);
//		
//		switch (type) {
//		case VALUE:
//			annotation.setAttribute("index", 100);
//			annotation.setAttribute("name", "");
//			annotation.setAttribute("informational", false);
//			break;
//		case INITIALIZE:
//			annotation.setAttribute("withParams", false);
//			break;
//		default:
//			break;
//		}
//		
//		return annotation;
		
		return null;
	}
}
