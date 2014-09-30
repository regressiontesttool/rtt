package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.model.annotation.Annotatable;
import rtt.annotation.editor.model.annotation.InitAnnotation;
import rtt.annotation.editor.model.annotation.NodeAnnotation;
import rtt.annotation.editor.model.annotation.ValueAnnotation;
import rtt.annotations.Node;
import rtt.annotations.Node.Value;

/**
 * Represents a java class.
 * 
 * @author Christian Oelsner <C.Oelsner@web.de>
 *
 */
@Node
public class ClassElement extends Annotatable<NodeAnnotation> {
	
	public enum ClassType {
		INTERFACE("Interface"), 
		ABSTRACT("Abstract"), 
		ENUMERATION("Enum"), 
		CONCRETE("Concrete");
		
		private String prettyName;

		private ClassType(String text) {
			this.prettyName = text;
		}
		
		public String getText() {
			return prettyName;
		}
	}
	
	@Value private String packageName = null;	
	@Value private ClassType type = ClassType.CONCRETE;
	
	@Value private ClassElementReference superClass = null;
	@Value private List<ClassElementReference> interfaces;
	
	@Value private List<FieldElement<ValueAnnotation>> valuablefields;
	@Value private List<MethodElement<ValueAnnotation>> valuableMethods;
	@Value private List<MethodElement<InitAnnotation>> initializableMethods;
	
	protected ClassElement(ClassModel parent) {
		super(parent);
		valuablefields = new ArrayList<>();
		valuableMethods = new ArrayList<>();
		initializableMethods = new ArrayList<>();
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}
	
	public ClassType getType() {
		return type;
	}
	
	public void setType(ClassType type) {
		this.type = type;
	}	
	
	public List<ClassElementReference> getInterfaces() {
		return interfaces;
	}
	
	public void setInterfaces(List<ClassElementReference> interfaces) {
		this.interfaces = interfaces;
	}
	
	public boolean hasInterfaces() {
		return interfaces != null && !interfaces.isEmpty();
	}
	
	public ElementReference<ClassElement> getSuperClass() {
		return superClass;
	}
	
	public void setSuperClass(ClassElementReference superClass) {
		this.superClass = superClass;
	}
	
	public boolean hasSuperClass() {
		return superClass != null;
	}
	
	public boolean hasExtendedAnnotation() {
		boolean hasExtendedAnnotation = false;
		
		if (hasSuperClass() && superClass.isResolved()) {
			hasExtendedAnnotation = superClass.isAnnotated();
		}
		
		if (!hasExtendedAnnotation && hasInterfaces()) {
			for (ClassElementReference interfaceRef : interfaces) {
				if (interfaceRef.isResolved()) {
					hasExtendedAnnotation = interfaceRef.isAnnotated();
					if (hasExtendedAnnotation == true) {
						break;
					}
				}				
			}
		}
		
		return hasExtendedAnnotation;
	}
	
	public boolean hasValues() {
		
		if (!valuablefields.isEmpty()) {
			for (FieldElement<ValueAnnotation> element : valuablefields) {
				if (element.hasAnnotation()) {
					return true;
				}
			}
		}
		
		if (!valuableMethods.isEmpty()) {
			for (MethodElement<ValueAnnotation> element : valuableMethods) {
				if (element.hasAnnotation()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean hasInits() {
		if (!initializableMethods.isEmpty()) {
			for (MethodElement<InitAnnotation> methodElement : initializableMethods) {
				if (methodElement.hasAnnotation()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public List<FieldElement<ValueAnnotation>> getValuableFields() {
		return valuablefields;
	}
	
	public void addValuableField(FieldElement<ValueAnnotation> field) {
		if (!valuablefields.contains(field)) {
			valuablefields.add(field);
			field.setParent(this);
		}
	}
	
	public FieldElement<ValueAnnotation> getValuableField(String name, String className) {
		for (FieldElement<ValueAnnotation> field : valuablefields) {
			if (name.equals(field.getName()) 
					&& className.equals(field.getType())) {
				return field;
			}
		}
		
		return null;
	}

	public List<MethodElement<ValueAnnotation>> getValuableMethods() {
		return valuableMethods;
	}
	
	public List<MethodElement<InitAnnotation>> getInitializableMethods() {
		return initializableMethods;
	}
	
	public void addValuableMethod(MethodElement<ValueAnnotation> method) {
		if (!valuableMethods.contains(method)) {
			valuableMethods.add(method);
			method.setParent(this);
		}
	}
	
	public void addInitializableMethod(MethodElement<InitAnnotation> method) {
		if (!initializableMethods.contains(method)) {
			initializableMethods.add(method);
			method.setParent(this);
		}
	}
	
	public MethodElement<ValueAnnotation> getValuableMethod(String name, String className) {
		for (MethodElement<ValueAnnotation> method : valuableMethods) {
			if (name.equals(method.getName()) 
					&& className.equals(method.getType())) {
				
				return method;
			}
		}
		
		return null;
	}
	
	public MethodElement<InitAnnotation> getInitializableMethod(String name) {
		for (MethodElement<InitAnnotation> method : initializableMethods) {
			if (name.equals(method.getName())) {				
				return method;
			}
		}
		
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + getName().hashCode();
		result = prime * result + packageName.hashCode();
		result = prime * result + type.hashCode();
		result = prime * result
				+ ((superClass == null) ? 0 : superClass.hashCode());
		result = prime * result
				+ ((interfaces == null) ? 0 : interfaces.hashCode());		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}		
		
		ClassElement other = (ClassElement) obj;
		if (!getName().equals(other.getName())) {
			return false;
		}
		if (!packageName.equals(other.packageName)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		if (superClass == null) {
			if (other.superClass != null) {
				return false;
			}
		} else if (!superClass.equals(other.superClass)) {
			return false;
		}
		if (interfaces == null) {
			if (other.interfaces != null) {
				return false;
			}
		} else if (!interfaces.equals(other.interfaces)) {
			return false;
		}

		return true;
	}		
}
