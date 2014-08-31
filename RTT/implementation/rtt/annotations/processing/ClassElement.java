package rtt.annotations.processing;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
	private SortedSet<InitMember<?>> initMembers;
	
	public ClassElement(Class<?> objectType) {
		valueMembers = populateValueMembers(objectType, null);
		initMembers = populateInitMembers(objectType, null);
	}
	
	public ClassElement(Class<?> objectType, ClassElement parent) {
		valueMembers = populateValueMembers(objectType, parent.getValueMembers());
		initMembers = populateInitMembers(objectType, parent.getInitMembers());
	}

	private SortedSet<ValueMember<?>> populateValueMembers(Class<?> objectType, 
			SortedSet<ValueMember<?>> parentMembers) {
		
		SortedSet<ValueMember<?>> members = new TreeSet<>();		
		if (parentMembers != null) {
			members.addAll(parentMembers);
		}
		
		addValueFields(objectType, members);
		addValueMethods(objectType, members);
		
		return members;
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
	
	public SortedSet<ValueMember<?>> getValueMembers() {
		return valueMembers;		
	}
	
	private SortedSet<InitMember<?>> populateInitMembers(Class<?> objectType, 
			SortedSet<InitMember<?>> parentMembers) {
		
		SortedSet<InitMember<?>> members = new TreeSet<>();
		
		addInitConstructors(objectType, members);
		addInitMethods(objectType, members);
		
		return members;
	}
	
	private void addInitConstructors(Class<?> objectType,
			Set<InitMember<?>> annotatedConstructors) {
		
		for (Constructor<?> constructor : objectType.getDeclaredConstructors()) {
			if (constructor.isAnnotationPresent(INIT_ANNOTATION)) {				
				annotatedConstructors.add(InitMember.create(constructor));
			}
		}
	}

	private void addInitMethods(Class<?> objectType,
			Set<InitMember<?>> annotatedMethods) {
		
		for (Method method : objectType.getDeclaredMethods()) {			
			if (method.isAnnotationPresent(INIT_ANNOTATION)) {				
				annotatedMethods.add(InitMember.create(method));
			}
		}
	}
	
	public SortedSet<InitMember<?>> getInitMembers() {
		return initMembers;
	}
}
