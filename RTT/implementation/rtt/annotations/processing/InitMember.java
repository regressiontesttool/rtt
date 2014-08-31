package rtt.annotations.processing;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import rtt.annotations.Node.Initialize;

public abstract class InitMember<T extends Member> 
	implements Comparable<InitMember<?>> {
	
	private static final Class<Initialize> INIT_ANNOTATION = Initialize.class;

	protected static class InitialConstructor extends InitMember<Constructor<?>> {

		private Constructor<?> constructor;
		
		public InitialConstructor(Constructor<?> constructor, Initialize initAnnotation) {
			super(constructor, constructor.getParameterTypes(), initAnnotation);
			this.constructor = constructor;
		}

		@Override
		public Object getResult(InputStream input, List<String> params) throws ReflectiveOperationException {
			
			constructor.setAccessible(true);
			try {
				if (isWithParams()) {
					return constructor.newInstance(input, params);
				} else {
					return constructor.newInstance(input);
				}
			} catch (IllegalAccessException | IllegalArgumentException 
					| InstantiationException constructorException) {
				
				throw new RuntimeException("Could not access initializing constructor.", 
						constructorException);			
			}
		}
	}
	
	protected static class InitialMethod extends InitMember<Method> {

		private Method method;

		public InitialMethod(Method method, Initialize initAnnotation) {
			super(method, method.getParameterTypes(), initAnnotation);
			this.method = method;
		}

		@Override
		public Object getResult(InputStream input, List<String> params) throws ReflectiveOperationException {

			method.setAccessible(true);
			Class<?> declaringClass = method.getDeclaringClass();
			
			try {
				Constructor<?> constructor = declaringClass.getDeclaredConstructor();			
				constructor.setAccessible(true);
				
				Object initialObject = constructor.newInstance();
				if (isWithParams()) {
					method.invoke(initialObject, input, params);
				} else {
					method.invoke(initialObject, input);
				}
				
				return initialObject;
				
			} catch (IllegalAccessException | IllegalArgumentException methodException) {
				throw new RuntimeException("Could not access initializing method.", methodException);
			} catch (NoSuchMethodException | InstantiationException constructorException) {
				throw new RuntimeException("Could not get parameter-less constructor.", constructorException);
			}
		}
	}	
	
	private T member;
	private String signature;
	private Class<?>[] parameterTypes;
	
	private Initialize initAnnotation;	

	public InitMember(T member, Class<?>[] parameterTypes, Initialize initAnnotation) {		
		this.member = member;
		this.parameterTypes = parameterTypes;
		
		this.signature = member.getDeclaringClass().getSimpleName()
				+ "." + member.getName() + Arrays.toString(parameterTypes);	
		
		this.initAnnotation = initAnnotation;					
	}
	
	public T getMember() {
		return member;
	}
	
	public final String getSignature() {
		return signature;
	}
	
	public Class<?>[] getParameterTypes() {
		return parameterTypes;
	}

	public Initialize getAnnotation() {
		return initAnnotation;
	}
	
	public boolean isWithParams() {
		return initAnnotation.withParams();
	}
	
	public abstract Object getResult(InputStream input, List<String> params) throws ReflectiveOperationException;
	
	public static final InitMember<Constructor<?>> create(Constructor<?> constructor) {
		return new InitialConstructor(constructor, constructor.getAnnotation(INIT_ANNOTATION));
	}
	
	public static final InitMember<Method> create(Method method) {
		return new InitialMethod(method, method.getAnnotation(INIT_ANNOTATION));
	}

	@Override
	public int compareTo(InitMember<?> other) {
		if (this.equals(other)) {
			return 0;
		}
		
		return this.signature.compareTo(other.signature);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + signature.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof InitMember)) {
			return false;
		}
		InitMember<?> other = (InitMember<?>) obj;
		if (!signature.equals(other.signature)) {
			return false;
		}
		return true;
	}		
}
