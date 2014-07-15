package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Parser.Node;

@Node
public class ClassElement extends Annotatable<ClassModel> {
	
	public enum ClassType {
		INTERFACE("Interface"), 
		ABSTRACT("Abstract"), 
		ENUMERATION("Enum"), 
		CONCRETE("Concrete");
		
		private String text;

		private ClassType(String text) {
			this.text = text;
		}
		
		public String getText() {
			return text;
		}
	}
	
	private String packageName = null;	
	private ClassType type = ClassType.CONCRETE;
	
	private ClassElementReference superClass = null;
	private List<ClassElementReference> interfaces;
	
	private List<FieldElement> fields;
	private List<MethodElement> methods;
	
	protected ClassElement(ClassModel parent) {
		super(parent);
		fields = new ArrayList<FieldElement>();
		methods = new ArrayList<MethodElement>();
	}
	
	@Node.Compare
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(final String packageName) {
		this.packageName = packageName;
	}
	
	@Node.Compare
	public ClassType getType() {
		return type;
	}
	
	public void setType(ClassType type) {
		this.type = type;
	}	
	
	@Node.Compare
	public List<ClassElementReference> getInterfaces() {
		return interfaces;
	}
	
	public void setInterfaces(List<ClassElementReference> interfaces) {
		this.interfaces = interfaces;
	}
	
	public boolean hasInterfaces() {
		return interfaces != null && !interfaces.isEmpty();
	}
	
	@Node.Compare
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
	
	public boolean hasMemberAnnotation() {
		
		if (fields != null && !fields.isEmpty()) {
			for (FieldElement element : fields) {
				if (element.hasAnnotation()) {
					return true;
				}
			}
		}
		
		if (methods != null && !methods.isEmpty()) {
			for (MethodElement element : methods) {
				if (element.hasAnnotation()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	@Node.Child
	public List<FieldElement> getFields() {
		return fields;
	}
	
	public void addField(FieldElement field) {
		if (!fields.contains(field)) {
			fields.add(field);
			field.setParent(this);
		}
	}
	
	public FieldElement getField(String name, String className) {
		for (FieldElement field : fields) {
			if (name.equals(field.getName()) 
					&& className.equals(field.getType())) {
				return field;
			}
		}
		
		return null;
	}

	@Node.Child
	public List<MethodElement> getMethods() {
		return methods;
	}
	
	public void addMethod(MethodElement method) {
		if (!methods.contains(method)) {
			methods.add(method);
			method.setParent(this);
		}
	}
	
	public MethodElement getMethod(String name, String className) {
		for (MethodElement method : methods) {
			if (name.equals(method.getName()) 
					&& className.equals(method.getType())) {
				
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
