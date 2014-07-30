package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class ClassElement {
	
	Class<?> type;
	private ClassElement parentElement;
	
	private MemberElement<Constructor<?>> constructors;
	private MemberElement<Field> fields;
	private MemberElement<Method> methods;
	
	public ClassElement(Class<?> objectType, ClassElement parentElement) {
		this.type = objectType;
		this.parentElement = parentElement;
		
		constructors = new ConstructorMemberElement(this);			
		fields = new FieldMemberElement(this);			
		methods = new MethodMemberElement(this);
	}
	
	public Class<?> getType() {
		return type;
	}
	
	public ClassElement getParentElement() {
		return parentElement;
	}

	public List<Constructor<?>> getConstructors(
			Class<? extends Annotation> annotation) {
		
		return constructors.getAnnotatedElements(annotation);
	}
	
	public List<Method> getMethods(
			Class<? extends Annotation> annotation) {
		
		return methods.getAnnotatedElements(annotation);
	}

	public List<Field> getFields(
			Class<? extends Annotation> annotation) {
		
		return fields.getAnnotatedElements(annotation);
	}		
}