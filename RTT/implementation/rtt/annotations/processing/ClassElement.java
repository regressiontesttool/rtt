package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

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
	
	protected Map<String, Constructor<?>> getConstructorMap(
			Class<? extends Annotation> annotation) {
		
		return constructors.getAnnotatedElements(annotation);		
	}

	public Collection<Constructor<?>> getConstructors(
			Class<? extends Annotation> annotation) {
		
		return getConstructorMap(annotation).values();
	}
	
	protected Map<String, Method> getMethodMap(
			Class<? extends Annotation> annotation) {
		
		return methods.getAnnotatedElements(annotation);
	}

	public Collection<Method> getMethods(
			Class<? extends Annotation> annotation) {
		
		return getMethodMap(annotation).values();
	}
	
	protected Map<String, Field> getFieldMap(
			Class<? extends Annotation> annotation) {
		
		return fields.getAnnotatedElements(annotation);
	}

	public Collection<Field> getFields(
			Class<? extends Annotation> annotation) {
		
		return getFieldMap(annotation).values();
	}		
}