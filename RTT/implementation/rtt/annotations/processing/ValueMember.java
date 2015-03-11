package rtt.annotations.processing;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

import rtt.annotations.Node.Value;
import rtt.core.archive.output.GeneratorType;

public abstract class ValueMember<T extends Member> 
	implements Comparable<ValueMember<?>> {
	
	private static final class ValueField extends ValueMember<Field> {
		private final Field field;

		private ValueField(Field field) {
			super(field, GeneratorType.FIELD, 
					field.getAnnotation(VALUE_ANNOTATION));
			
			this.field = field;
		}

		@Override
		public Object getResult(Object object)
				throws ReflectiveOperationException {
			
			field.setAccessible(true);
			return field.get(object);
		}
		
		@Override
		public String getReturnType() {
			return field.getType().getName();
		}
	}

	private static final class ValueMethod extends ValueMember<Method> {
		private final Method method;

		private ValueMethod(Method method) {
			super(method, GeneratorType.METHOD,
					method.getAnnotation(VALUE_ANNOTATION));
			
			this.method = method;
		}

		@Override
		public Object getResult(Object object)
				throws ReflectiveOperationException {
			
			method.setAccessible(true);
			return method.invoke(object);
		}
		
		@Override
		public String getReturnType() {
			return method.getReturnType().getName();
		}
	}

	private static final Class<Value> VALUE_ANNOTATION = Value.class;
	private Value valueAnnotation;
	private T member;
	
	private int index;
	private String name;
	private boolean informational;
	
	private GeneratorType type;	
	private String signature;	

	private ValueMember(T member, GeneratorType type, Value valueAnnotation) {		
		this.member = member;
		this.type = type;
		
		this.signature = member.getDeclaringClass().getSimpleName()
				+ "." + member.getName();		
		
		this.valueAnnotation = valueAnnotation;		
		this.index = valueAnnotation.index();
		this.informational = valueAnnotation.informational();
		this.name = valueAnnotation.name();
		if (this.name == null) {
			this.name = "";
		}
	}
	
	public abstract Object getResult(Object object) 
			throws ReflectiveOperationException;
	
	public abstract String getReturnType();
	
	public static final ValueMember<Field> create(final Field field) {
		return new ValueField(field);
	}
	
	public static final ValueMember<Method> create(final Method method) {
		return new ValueMethod(method);
	}
	
	public T getMember() {
		return member;
	}
	
	public Value getAnnotation() {
		return valueAnnotation;
	}
	
	public final boolean isInformational() {
		return informational;
	}
	
	public final GeneratorType getType() {
		return type;
	}
	
	public final String getSignature() {
		return signature;
	}
	
	public final String getName() {
		if (name == null || name.equals("")) {
			return signature;
		}
		
		return name;
	}

	@Override
	public int compareTo(ValueMember<?> other) {
		if (this.equals(other)) {
			return 0;
		}
		
		if (this.index != other.index) {
			return Integer.compare(this.index, other.index);
		}
		
		if (this.informational != other.informational) {
			return Boolean.compare(this.informational, other.informational);
		}
		
		if (this.type != other.type) {
			return type.compareTo(other.type);
		}				
		
		if (!this.name.equals(other.name)) {
			return this.name.compareTo(other.name);
		}
		
		return this.signature.compareTo(other.signature);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + (informational ? 1231 : 1237);
		result = prime * result + type.hashCode();
		result = prime * result + name.hashCode();
		result = prime * result + signature.hashCode();
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ValueMember)) {
			return false;
		}
		ValueMember<?> other = (ValueMember<?>) obj;
		if (index != other.index) {
			return false;
		}
		if (informational != other.informational) {
			return false;
		}		
		if (type != other.type) {
			return false;
		}		
		if (!name.equals(other.name)) {
			return false;
		}
		if (!signature.equals(other.signature)) {
			return false;
		}
		
		return true;
	}
}
