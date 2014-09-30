package rtt.annotation.editor.model.annotation;

public class ValueAnnotation extends Annotation {
	
	public static final String INDEX_KEY = "index";
	public static final String NAME_KEY = "name";
	public static final String INFORMATIONAL_KEY = "informational";
	
	private int index = 100;
	private String name = "";
	private boolean informational = false;

	protected ValueAnnotation() {
		super(AnnotationType.VALUE, 
				INDEX_KEY, NAME_KEY, INFORMATIONAL_KEY);
	}
	
	public int getValueIndex() {
		return index;
	}
	
	public String getValueName() {
		return name;
	}
	
	public boolean isInformational() {
		return informational;
	}
	
	@Override
	public void setAttribute(String key, Object value) {
		if (INDEX_KEY.equals(key)) {
			index = (Integer) value;
		} else if (NAME_KEY.equals(key)) {
			name = (String) value;
		} else if (INFORMATIONAL_KEY.equals(key)) {
			informational = (Boolean) value;
		}
	}

	@Override
	public Object getAttribute(String key) {
		if (INDEX_KEY.equals(key)) {
			return index;
		} else if (NAME_KEY.equals(key)) {
			return name;
		} else if (INFORMATIONAL_KEY.equals(key)) {
			return informational;
		}
		return null;
	}
}
