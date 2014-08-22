package rtt.annotations.processing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassElement {
	
	Class<?> type;
	ClassElement parentElement;
	
	private AbstractMemberElement<Constructor<?>> constructors;
	private AbstractMemberElement<Field> fields;
	private AbstractMemberElement<Method> methods;
	
	protected ClassElement(Class<?> objectType) {
		this.type = objectType;

		constructors = new ConstructorMemberElement(this);			
		fields = new FieldMemberElement(this);			
		methods = new MethodMemberElement(this);
	}
	
	protected ClassElement(Class<?> objectType, ClassElement parentElement) {
		this(objectType);
		this.parentElement = parentElement;		
	}
	
	protected Map<String, Constructor<?>> getConstructorMap(
			Class<? extends Annotation> annotation) {
		return constructors.getAnnotatedElements(annotation);
	}

	public List<Constructor<?>> getConstructors(
			Class<? extends Annotation> annotation) {		
		return new ArrayList<>(getConstructorMap(annotation).values());
	}
	
	protected Map<String, Method> getMethodMap(
			Class<? extends Annotation> annotation) {
		return methods.getAnnotatedElements(annotation);
	}	
	
	public List<Method> getMethods(
			Class<? extends Annotation> annotation) {		
		return new ArrayList<>(getMethodMap(annotation).values());
	}
	
	protected Map<String, Field> getFieldMap(
			Class<? extends Annotation> annotation) {
		return fields.getAnnotatedElements(annotation);
	}

	public List<Field> getFields(
			Class<? extends Annotation> annotation) {		
		return new ArrayList<>(getFieldMap(annotation).values());
	}		
}