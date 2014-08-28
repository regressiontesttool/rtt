package rtt.annotations.processing2;

import java.lang.reflect.Member;

import rtt.annotations.Node.Value;
import rtt.core.archive.output.Type;

public abstract class ValueMember<T extends Member> 
	implements Comparable<ValueMember<?>> {
	
	private Value valueAnnotation;
	
	private int index;
	private String name;
	private boolean informational;
	
	private T member;
	private Type type;	
	private String signature;	

	protected ValueMember(T member, Type type, Value valueAnnotation) {
		
		this.member = member;
		this.type = type;
		this.signature = getSignature(member);
		
		this.valueAnnotation = valueAnnotation;		
		this.index = valueAnnotation.index();
		this.name = valueAnnotation.name();
		this.informational = valueAnnotation.informational();			
	}
	
	protected abstract String getSignature(T member);
	protected abstract Object getResult(T member, Object object) 
			throws ReflectiveOperationException;
	
	@SuppressWarnings("unchecked")
	public boolean equalSignature(Member searchedMember) {
		if (searchedMember.getClass().isInstance(member)) {
			String ownSignature = getSignature(this.member);
			String searchedSignature = getSignature((T) searchedMember);
			
			return ownSignature.equals(searchedSignature); 
		}
		
		return false;
	}
	
	public Member getMember() {
		return member;
	}

	public Value getAnnotation() {
		return valueAnnotation;
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
	
	public final Type getType() {
		return type;
	}
	
	public final boolean isInformational() {
		return informational;
	}
	
	public final Object getResult(Object object) throws Exception {
		return getResult(member, object);
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
