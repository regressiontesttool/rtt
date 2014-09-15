package rtt.annotation.editor.model;

/**
 * Used for referencing other {@link ModelElement}s.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 * @param <T> the type of {@link ModelElement}, 
 * 	which should be referenced.
 */
public class ElementReference<T extends ModelElement> {
	
	private String name;
	private T reference;
	
	protected ElementReference(String name) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((reference == null) ? 0 : reference.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementReference<?> other = (ElementReference<?>) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		return true;
	}
}
