package rtt.annotation.editor.model;

public class ElementReference<T extends ModelElement<?>> {
	
	private String name;
	private T reference;
	
	public ElementReference(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public T getReference() {
		return reference;
	}
	
	public void setReference(T reference) {
		this.reference = reference;
	}
	
	public boolean isResolved() {
		return reference != null;
	}

}
