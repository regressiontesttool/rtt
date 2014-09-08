package rtt.annotation.editor.model;

import rtt.annotations.Node.Value;


public abstract class ModelElement<T extends ModelElement<?>> {
	
	@Value private String name = null;
	@Value private T parent = null;

	private boolean changed = false;
	
	protected ModelElement(T parent) {
		this.parent = parent;
	}
	
	public final String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
	
	public T getParent() {
		return parent;
	}
	
	public void setParent(T parent) {
		this.parent = parent;
	}
	
	public final boolean hasChanged() {
		return changed;
	}
	
	public final void setChanged(boolean changed) {
		this.changed = changed;
		if (parent != null) {
			parent.setChanged(changed);
		}
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
