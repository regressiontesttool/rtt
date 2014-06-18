package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Parser.Node;

@Node
public class ClassElement extends Annotatable<ClassModel> {
	
	public enum ClassType {
		INTERFACE, ABSTRACT, CONCRETE;
	}
	
	private String packageName = null;	
	private ClassType type = ClassType.CONCRETE;
	
	private String superClass = null;
	private List<String> interfaces;
	
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
	public List<String> getInterfaces() {
		return interfaces;
	}
	
	public boolean hasInterfaces() {
		return interfaces != null && ! interfaces.isEmpty();
	}
	
	public void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
	}
	
	@Node.Compare
	public String getSuperClass() {
		return superClass;
	}
	
	public boolean hasSuperClass() {
		return superClass != null && !superClass.equals("");
	}
	
	public void setSuperClass(String superClass) {
		this.superClass = superClass;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((interfaces == null) ? 0 : interfaces.hashCode());
		result = prime * result
				+ ((packageName == null) ? 0 : packageName.hashCode());
		result = prime * result
				+ ((superClass == null) ? 0 : superClass.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		if (packageName == null) {
			if (other.packageName != null) {
				return false;
			}
		} else if (!packageName.equals(other.packageName)) {
			return false;
		}		
		if (interfaces == null) {
			if (other.interfaces != null) {
				return false;
			}
		} else if (!interfaces.equals(other.interfaces)) {
			return false;
		}
		
		if (superClass == null) {
			if (other.superClass != null) {
				return false;
			}
		} else if (!superClass.equals(other.superClass)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
}
