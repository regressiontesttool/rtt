package rtt.annotation.editor.model.annotation;

public class InitAnnotation extends Annotation {

	public static final String PARAMS_KEY = "withParams";
	private boolean withParams = false;
	
	public InitAnnotation() {
		super(AnnotationType.INITIALIZE, PARAMS_KEY);
	}
	
	public boolean isWithParams() {
		return withParams;
	}
	
	public void setWithParams(boolean withParams) {
		this.withParams = withParams;
	}

	@Override
	public void setAttribute(String key, Object value) {
		if (PARAMS_KEY.equals(key)) {
			withParams = (Boolean) value;
		}
	}

	@Override
	public Object getAttribute(String key) {
		if (PARAMS_KEY.equals(key)) {
			return withParams;
		}
		
		return null;
	}

}
