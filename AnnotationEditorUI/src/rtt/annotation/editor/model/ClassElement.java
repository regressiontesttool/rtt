package rtt.annotation.editor.model;

import java.util.ArrayList;
import java.util.List;

import rtt.annotation.editor.rules.Annotation;

public class ClassElement extends Annotatable<ClassModel> {
	
	private String packageName = null;
	
	private List<FieldElement> fields;
	private List<MethodElement> methods;
	
	protected ClassElement() {		
		fields = new ArrayList<FieldElement>();
		methods = new ArrayList<MethodElement>();
	}
	
	public String getPackageName() {
		return packageName;
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public List<FieldElement> getFields() {
		return fields;
	}
	
	public void addField(FieldElement field) {
		if (!fields.contains(field)) {
			fields.add(field);
			field.setParent(this);
		}
	}

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
