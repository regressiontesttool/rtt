package rtt.annotation.editor.controller.rules;

public enum Annotation {
	NONE (""), 
	NODE ("Node"), 
	VALUE ("Value"),
	INITIALIZE ("Initialize");
	
	private String prettyName;

	private Annotation(String prettyName) {
		this.prettyName = prettyName;
	}

	public String getPrettyName() {
		return prettyName;
	}
}
