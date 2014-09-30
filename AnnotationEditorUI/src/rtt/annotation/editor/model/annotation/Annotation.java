package rtt.annotation.editor.model.annotation;


/**
 * This class represents an annotations from RTT.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
public abstract class Annotation {	

	private String name;
	
	protected Annotation(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static <T extends Annotation> T create(Class<T> annotationType) {
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
