package rtt.annotations.processing;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import rtt.annotations.Node.Initialize;
import rtt.annotations.Node.Value;
import rtt.core.utils.RTTLogging;

public class ClassElement {
	
	private static final String ONLY_NONVOID_METHODS = 
			"Only methods with a non-void return type allowed.";
	private static final String ONLY_PARAMETERLESS_METHODS = 
			"Only methods without parameters allowed.";

	private static final Class<Value> VALUE_ANNOTATION = Value.class;
	private static final Class<Initialize> INIT_ANNOTATION = Initialize.class;
	
	private SortedSet<ValueMember<?>> valueMembers;
	private SortedSet<InitialMember<?>> initMembers;
	
	public ClassElement(Class<?> objectType) {
		valueMembers = createValueMembers(objectType, null);
		initMembers = createInitMembers(objectType, null);
	}
	
	public ClassElement(Class<?> objectType, ClassElement parent) {
		valueMembers = createValueMembers(objectType, parent.valueMembers);
		initMembers = createInitMembers(objectType, parent.initMembers);
	}

	private SortedSet<ValueMember<?>> createValueMembers(Class<?> objectType, 
			SortedSet<ValueMember<?>> parentMembers) {
		
		SortedSet<ValueMember<?>> members = new TreeSet<>();		
		if (parentMembers != null) {
			members.addAll(parentMembers);
		}
		
		addValueFields(objectType, members);
		addValueMethods(objectType, members);
		
		return members;
	}
	
	public SortedSet<ValueMember<?>> getValueMembers() {
		return valueMembers;		
	}
	
	private SortedSet<InitialMember<?>> createInitMembers(Class<?> objectType, 
			SortedSet<InitialMember<?>> parentMembers) {
		
		SortedSet<InitialMember<?>> members = new TreeSet<>();		
		if (parentMembers != null) {
			members.addAll(parentMembers);
		}
		
		addInitConstructors(objectType, members);
		addInitMethods(objectType, members);
		
		return members;
	}
	
	public SortedSet<InitialMember<?>> getInitMembers() {
		return initMembers;
	}

	private void addValueFields(Class<?> objectType,
			Set<ValueMember<?>> annotatedFields) {
		
		for (Class<?> interfaceType : objectType.getInterfaces()) {
			addValueFields(interfaceType, annotatedFields);
		}
		
		for (Field field : objectType.getDeclaredFields()){
			if (field.isAnnotationPresent(VALUE_ANNOTATION)) {
				annotatedFields.add(ValueMember.create(field));
			}
		}
	}

	private void addValueMethods(Class<?> objectType,
			Set<ValueMember<?>> annotatedMethods) {
		
		for (Class<?> interfaceType : objectType.getInterfaces()) {
			addValueMethods(interfaceType, annotatedMethods);
		}
		
		for (Method method : objectType.getDeclaredMethods()) {
			if (method.isAnnotationPresent(VALUE_ANNOTATION)) {
				if (method.getReturnType() == Void.TYPE) {
					RTTLogging.warn(ONLY_NONVOID_METHODS);
					continue;
				}
				
				if (method.getParameterTypes().length > 0) {
					RTTLogging.warn(ONLY_PARAMETERLESS_METHODS);
					continue;
				}
				
				annotatedMethods.add(ValueMember.create(method));				
			}
		}
	}
	
	private void addInitConstructors(Class<?> objectType,
			Set<InitialMember<?>> members) {
		
		for (Constructor<?> constructor : objectType.getDeclaredConstructors()) {
			if (constructor.isAnnotationPresent(INIT_ANNOTATION)) {
				members.add(new InitialConstructor(
						constructor, constructor.getAnnotation(INIT_ANNOTATION)));
			}
		}
	}

	private void addInitMethods(Class<?> objectType,
			Set<InitialMember<?>> annotatedMethods) {
		
		for (Method method : objectType.getDeclaredMethods()) {			
			if (method.isAnnotationPresent(INIT_ANNOTATION)) {				
				annotatedMethods.add(new InitialMethod(
						method, method.getAnnotation(INIT_ANNOTATION)));
				
			} else if (!Modifier.isPrivate(method.getModifiers())) {
				
				InitialMember<?> annotatedMethod = searchInitMember(method, annotatedMethods);
				if (annotatedMethod != null) {
					annotatedMethods.add(new InitialMethod(
							method, annotatedMethod.getAnnotation()));
				}
			}
		}
	}
	
	private InitialMember<?> searchInitMember(Member member, 
			Set<InitialMember<?>> annotatedMethods) {
		
		for (InitialMember<?> annotatedMember : annotatedMethods) {
			if (annotatedMember.hasMember(member)) {
				return annotatedMember;
			}
		}
		
		return null;
	}	
}
