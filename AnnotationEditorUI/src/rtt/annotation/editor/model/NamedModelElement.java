package rtt.annotation.editor.model;

import rtt.annotations.Parser.Node;

public class NamedModelElement<T extends ModelElement<?>> extends ModelElement<T> {
	
	protected String name = null;
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Node.Compare
	public String getName() {
		return name;
	}
	
	@Override
	public String getLabel() {
		return getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}		
		if (getClass() != obj.getClass()) {
			return false;
		}
		NamedModelElement<?> other = (NamedModelElement<?>) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
