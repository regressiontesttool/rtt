package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

import rtt.annotations.Parser.Node;

@Node
public class ClassElement extends Annotatable<ClassModel> {
	
	private String packageName = null;
	private boolean isInterface = false;
	private boolean isAbstract = false;
	
	private List<String> interfaces;
	
	private List<FieldElement> fields;
	private List<MethodElement> methods;
	
	protected ClassElement() {		
		interfaces = new ArrayList<String>();
		
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
	public boolean isInterface() {
		return isInterface;
	}
	
	public void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}
	
	@Node.Compare
	public boolean isAbstract() {
		return isAbstract;
	}
	
	public void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}
	
	@Node.Compare
	public List<String> getInterfaces() {
		return interfaces;
	}
	
	public void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
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
				+ ((packageName == null) ? 0 : packageName.hashCode());
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
		return true;
	}	
}
