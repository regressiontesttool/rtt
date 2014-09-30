package rtt.annotation.editor.model.annotation;


public class NodeAnnotation extends Annotation {
	
	protected NodeAnnotation() {
		super(AnnotationType.NODE);
	}
	
	@Override
	public Object getAttribute(String key) {
		return null;
	}
	
	@Override
	public void setAttribute(String key, Object value) {}
}
