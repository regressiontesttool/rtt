package rtt.annotations.processing2;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.TreeSet;

import rtt.annotations.Node.Value;
import rtt.core.utils.RTTLogging;

public class ClassElement2 {
	
	private static final String ONLY_NONVOID_METHODS = "Only methods with a non-void return type allowed.";
	private static final String ONLY_PARAMETERLESS_METHODS = "Only methods without parameters allowed.";

	private static final Class<Value> VALUE_ANNOTATION = Value.class;
	
	private Set<AnnotatedElement<?>> valueMembers;
	
	public ClassElement2(Class<?> objectType, ClassElement2 parent) {
		valueMembers = new TreeSet<>();
		
		if (parent != null) {
			valueMembers.addAll(parent.valueMembers);
		}
		
		addFields(objectType, valueMembers);
		addMethods(objectType, valueMembers);
	}

	public ClassElement2(Class<?> objectType) {
		this(objectType, null);
	}
	
	public Set<AnnotatedElement<?>> getValueMembers() {
		return valueMembers;		
	}
	
	private void addFields(Class<?> objectType,
			Set<AnnotatedElement<?>> annotatedFields) {
		
		for (Class<?> interfaceType : objectType.getInterfaces()) {
			addFields(interfaceType, annotatedFields);
		}
		
		for (Field field : objectType.getDeclaredFields()){
			if (field.isAnnotationPresent(VALUE_ANNOTATION)) {
				annotatedFields.add(new AnnotatedField(
						field, field.getAnnotation(VALUE_ANNOTATION)));
			}
		}
	}

	private void addMethods(Class<?> objectType,
			Set<AnnotatedElement<?>> annotatedMethods) {
		
		for (Class<?> interfaceType : objectType.getInterfaces()) {
			addMethods(interfaceType, annotatedMethods);
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
				
				annotatedMethods.add(new AnnotatedMethod(
						method, method.getAnnotation(VALUE_ANNOTATION)));
				
			} else if (!Modifier.isPrivate(method.getModifiers())) {
				
				AnnotatedElement<?> annotatedMethod = searchMember(method);
				if (annotatedMethod != null) {
					annotatedMethods.add(new AnnotatedMethod(
							method, annotatedMethod.getValueAnnotation()));
				}
			}
		}
	}

	private AnnotatedElement<?> searchMember(Member member) {
		for (AnnotatedElement<?> annotatedMember : valueMembers) {
			if (annotatedMember.hasMember(member)) {
				return annotatedMember;
			}
		}
		
		return null;
	}	
}
