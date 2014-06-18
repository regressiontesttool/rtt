package rtt.annotation.editor.model;

import rtt.annotations.Parser.Node;


public abstract class ModelElement<T extends ModelElement<?>> {
	
	private String name = null;
	private T parent = null;	
	
	@Node.Compare
	public final String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
	
	@Node.Compare
	public T getParent() {
		return parent;
	}
	
	public void setParent(T parent) {
		this.parent = parent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ModelElement<?> other = (ModelElement<?>) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (parent == null) {
			if (other.parent != null) {
				return false;
			}
		} else if (!parent.equals(other.parent)) {
			return false;
		}
		return true;
	}	
}
