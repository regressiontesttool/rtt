package rtt.ui.editors.input;

public class StringDetail {

	private String key;
	private String value;
	
	public StringDetail(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public StringDetail(String key, int value) {
		this(key, "" + value);
	}
	
	public String getKey() {
		return key;
	}
	
	public String getValue() {
		return value;
	}
}
