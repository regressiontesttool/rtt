package rtt.annotation.editor.model;

import java.util.Observable;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotations.Node.Value;

/**
 * An abstract class for various java elements.
 *
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 * @see ClassModel
 * @see Annotatable
 * @see ClassElement
 * @see FieldElement
 * @sse MethodElement
 *
 */
public abstract class ModelElement extends Observable {
	
	@Value private String name = null;
	@Value private ModelElement parent = null;
	
	protected ModelElement(ModelElement parent) {
		this.parent = parent;
	}
	
	public final String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}	
	
	public ModelElement getParent() {
		return parent;
	}
	
	public void setParent(ModelElement parent) {
		this.parent = parent;
	}
	
	public final void setChanged() {
		super.setChanged();
		if (parent != null) {
			parent.setChanged();
		} else {
			notifyObservers();
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
		ModelElement other = (ModelElement) obj;
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
